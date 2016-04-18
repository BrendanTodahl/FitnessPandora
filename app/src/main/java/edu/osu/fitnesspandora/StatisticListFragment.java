package edu.osu.fitnesspandora;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;


public class StatisticListFragment extends ListFragment {

    private static final String TAG = "StatisticListFragment";

    private static final String SAVED_STATE = "Saved_State";
    private boolean mStateIsNew;

    // User's credentials from SharedPreferences
    private User mUser;

    // Initialize data from Firebase
    private WorkoutLab mWorkoutLab;
    private ExerciseLab mExerciseLab;
    private ExerciseLogLab mExerciseLogLab;
    private ArrayList<ExerciseLog> mExerciseLogs;

    ExerciseLogConsolidatedLab mELCLab;

    private Button mDateButton;
    private Button mScoreButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the user's credentials
        mUser = User.get(getActivity());

        if(savedInstanceState == null){

            mStateIsNew = true;

            // Set the sort buttons to visible and hook em up
            getActivity().findViewById(R.id.stats_sort_buttons).setVisibility(View.VISIBLE);

            mDateButton = (Button) getActivity().findViewById(R.id.stats_date_button);
            mDateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Sort by the consolidated exercise dates, latest first
                    Collections.sort(mELCLab.mConsExerciseLogs, new CompareConsolidatedExerciseDates());
                    // Restart the adapter
                    ConsolidatedExerciseLogAdapter adapter = new ConsolidatedExerciseLogAdapter(mELCLab.mConsExerciseLogs);
                    setListAdapter(adapter);
                }
            });
            mScoreButton = (Button) getActivity().findViewById(R.id.stats_score_button);
            mScoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Sort by the consolidated exercise scores
                    Collections.sort(mELCLab.mConsExerciseLogs, new CompareConsolidatedExerciseScores());
                    // Restart the adapter
                    ConsolidatedExerciseLogAdapter adapter = new ConsolidatedExerciseLogAdapter(mELCLab.mConsExerciseLogs);
                    setListAdapter(adapter);
                }
            });

        }else{
            // DO NOTHING
            mStateIsNew = false;
        }

        // Initialize data from Firebase
        mWorkoutLab = WorkoutLab.get();
        mExerciseLab = ExerciseLab.get();
        mExerciseLogLab = ExerciseLogLab.get(mUser.getAuthUID());
        mExerciseLogs = mExerciseLogLab.getExerciseLogs();

        mELCLab = new ExerciseLogConsolidatedLab(mExerciseLogLab);

        // Sort by the consolidated exercise dates, latest first
        Collections.sort(mELCLab.mConsExerciseLogs, new CompareConsolidatedExerciseDates());

        // Restart the adapter
        ConsolidatedExerciseLogAdapter adapter = new ConsolidatedExerciseLogAdapter(mELCLab.mConsExerciseLogs);
        setListAdapter(adapter);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        mStateIsNew = false;
        savedInstanceState.putBoolean(SAVED_STATE, mStateIsNew);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mStateIsNew){
            ((ConsolidatedExerciseLogAdapter)getListAdapter()).notifyDataSetChanged();
        }else{
            // DO NOTHING
        }
    }

    private void discardExerciseLogs(){
        ArrayList<ExerciseLog> tempArray = new ArrayList<ExerciseLog>();
        for(ExerciseLog el : mExerciseLogs){
            if(el.getexerciseScore() > 0){
                tempArray.add(el);
            }
        }
        mExerciseLogs = tempArray;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {


    }

    private class ExerciseLogAdapter extends ArrayAdapter<ExerciseLog> {

        public ExerciseLogAdapter(ArrayList<ExerciseLog> exerciseLogs) {
            super(getActivity(), 0, exerciseLogs);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // If we weren't given a view, inflate one
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_exercise_log, null);
            }
            // Configure the view for this Workout
            ExerciseLog e = getItem(position);

            Exercise exercise = mExerciseLab.getExercise(e.getExerciseID());
            Workout workout = mWorkoutLab.getWorkout(e.getWorkoutID());

            TextView exerciseTitle = (TextView) convertView.findViewById(R.id.exercise_log_list_item_exercisetitle);
            exerciseTitle.setText(exercise.getExerciseTitle());

            TextView workoutTitle = (TextView) convertView.findViewById(R.id.exercise_log_list_item_workouttitle);
            workoutTitle.setText(workout.getWorkoutTitle() + " Workout");

            TextView date = (TextView) convertView.findViewById(R.id.exercise_log_list_item_date);
            Date theDate = new Date(e.getExerciseDate());
            Calendar theCal = Calendar.getInstance();
            theCal.setTime(theDate);

            String dateFormat = "";
            dateFormat += theCal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) + " ";
            dateFormat += theCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + ", ";
            dateFormat += theCal.get(Calendar.DAY_OF_MONTH) + ", ";
            dateFormat += theCal.get(Calendar.YEAR) + " @ ";
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
            dateFormat += sdf.format(theDate) + " ";
            dateFormat += theCal.getDisplayName(Calendar.AM_PM, Calendar.LONG, Locale.getDefault());
            date.setText(dateFormat);

            return convertView;
        }
    }

    private class ConsolidatedExerciseLogAdapter extends ArrayAdapter<ExerciseLogConsolidated> {

        public ConsolidatedExerciseLogAdapter(ArrayList<ExerciseLogConsolidated> exerciseLogs) {
            super(getActivity(), 0, exerciseLogs);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // If we weren't given a view, inflate one
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_exercise_log, null);
            }
            // Configure the view for this Workout
            ExerciseLogConsolidated e = getItem(position);

            Exercise exercise = mExerciseLab.getExercise(e.getExerciseID());
            Workout workout = mWorkoutLab.getWorkout(e.getWorkoutID());

            TextView exerciseTitle = (TextView) convertView.findViewById(R.id.exercise_log_list_item_exercisetitle);
            exerciseTitle.setText(exercise.getExerciseTitle());

            TextView workoutTitle = (TextView) convertView.findViewById(R.id.exercise_log_list_item_workouttitle);
            workoutTitle.setText(workout.getWorkoutTitle() + " Workout");

            TextView likesTitle = (TextView) convertView.findViewById(R.id.exercise_log_list_item_exerciseLikes);
            likesTitle.setText("Likes: " + e.mSumLikes);

            TextView skipsTitle = (TextView) convertView.findViewById(R.id.exercise_log_list_item_exerciseSkips);
            skipsTitle.setText("Skips: " + e.mSumSkips);

            TextView repCount = (TextView) convertView.findViewById(R.id.reps);
            repCount.setText(String.valueOf(e.getExerciseReps())+" ");

            TextView weight = (TextView) convertView.findViewById(R.id.weight);
            weight.setText(String.valueOf(e.getExerciseWeight()));

            TextView date = (TextView) convertView.findViewById(R.id.exercise_log_list_item_date);
            Date theDate = new Date(e.getExerciseDate());
            Calendar theCal = Calendar.getInstance();
            theCal.setTime(theDate);

            String dateFormat = "Most recent: ";
            dateFormat += theCal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) + " ";
            dateFormat += theCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + ", ";
            dateFormat += theCal.get(Calendar.DAY_OF_MONTH) + ", ";
            dateFormat += theCal.get(Calendar.YEAR) + " @ ";
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
            dateFormat += sdf.format(theDate) + " ";
            dateFormat += theCal.getDisplayName(Calendar.AM_PM, Calendar.LONG, Locale.getDefault());
            date.setText(dateFormat);

            return convertView;
        }
    }

    public class CompareExerciseScores implements Comparator<ExerciseLog> {
        @Override
        public int compare(ExerciseLog e1, ExerciseLog e2) {

            int result =  Integer.parseInt(Long.toString(mExerciseLogLab.exerciseScore(e2.getExerciseID()) - mExerciseLogLab.exerciseScore(e1.getExerciseID())));
            // Tie breaker is the id of the exercise
            if(result == 0){
                result = Integer.parseInt(Long.toString(e2.getExerciseID() - e1.getExerciseID()));
            }
            return result;
        }
    }

    public class CompareExerciseDates implements Comparator<ExerciseLog> {
        @Override
        public int compare(ExerciseLog e1, ExerciseLog e2) {
            return Integer.parseInt(Long.toString(e2.getExerciseDate() - e1.getExerciseDate()));
        }
    }


    public class CompareConsolidatedExerciseScores implements Comparator<ExerciseLogConsolidated> {
        @Override
        public int compare(ExerciseLogConsolidated e1, ExerciseLogConsolidated e2) {

            int result =  Integer.parseInt(Long.toString(e2.mSumLikes - e1.mSumLikes));

            if(result == 0){
                result = Integer.parseInt(Long.toString(e1.mSumSkips - e2.mSumSkips));
            }

            // Tie breaker is the id of the exercise
            if(result == 0){
                result = Integer.parseInt(Long.toString(e2.getExerciseID() - e1.getExerciseID()));
            }
            return result;
        }
    }

    public class CompareConsolidatedExerciseDates implements Comparator<ExerciseLogConsolidated> {
        @Override
        public int compare(ExerciseLogConsolidated e1, ExerciseLogConsolidated e2) {
            return Integer.parseInt(Long.toString(e2.getExerciseDate() - e1.getExerciseDate()));
        }
    }

    public class ExerciseLogConsolidatedLab{

        private ArrayList<ExerciseLogConsolidated> mConsExerciseLogs;

        public ExerciseLogConsolidatedLab(ExerciseLogLab exerciseLogLab){
            mConsExerciseLogs = new ArrayList<ExerciseLogConsolidated>();

            for(ExerciseLog el : exerciseLogLab.getExerciseLogs()){
                this.addExerciseLog(el);
            }

        }

        public void addExerciseLog(ExerciseLog el){
            boolean foundExercise = false;
            for(ExerciseLogConsolidated elc : mConsExerciseLogs){
                if(elc.mExerciseID == el.getExerciseID()){
                    foundExercise = true;
                    // Add the exercise to the consolidation object
                    elc.addSameExercise(el);
                }
            }

            if(!foundExercise){
                // Make a new consolidated object if the exercise id is not found
                mConsExerciseLogs.add(new ExerciseLogConsolidated(el));
            }


        }

    }

    public class ExerciseLogConsolidated{

        public long mExerciseID;
        public long mWorkoutID;
        public long mSumLikes;
        public long mSumSkips;
        public long mMostRecentDate;
        public long mExerciseReps;
        public long mExerciseWeight;

        public ExerciseLogConsolidated(ExerciseLog el){
            this.mExerciseID = el.getExerciseID();
            this.mWorkoutID = el.getWorkoutID();
            this.mSumLikes = 0;
            this.mSumSkips = 0;
            this.addSameExercise(el);
            this.mExerciseReps = el.getExerciseReps();
            this.mExerciseWeight = el.getExerciseWeight();
        }

        public void addSameExercise(ExerciseLog el){
            // Sum up the exercise score
            if(el.getexerciseScore() > 0){
                this.mSumLikes++;
            }else{
                this.mSumSkips++;
            }
            // If the date is more recent, store it
            if(this.mMostRecentDate < el.getExerciseDate()){
                this.mMostRecentDate = el.getExerciseDate();
            }
        }

        public long getExerciseID(){
            return mExerciseID;
        }

        public long getExerciseDate(){
            return mMostRecentDate;
        }

        public long getWorkoutID(){
            return mWorkoutID;
        }

        public long getExerciseReps() {return mExerciseReps;}

        public long getExerciseWeight() {return mExerciseWeight;}


    }

}