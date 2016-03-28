package edu.osu.fitnesspandora;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class StatisticListFragment extends ListFragment {

    private static final String TAG = "StatisticListFragment";

    // User's credentials from SharedPreferences
    private User mUser;

    // Initialize data from Firebase
    private WorkoutLab mWorkoutLab;
    private ExerciseLab mExerciseLab;
    private ArrayList<ExerciseLog> mExerciseLogs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Exercise Statistics");

        // Initialize the user's credentials
        mUser = User.get(getActivity());

        // Initialize data from Firebase
        mWorkoutLab = WorkoutLab.get();
        mExerciseLab = ExerciseLab.get();
        mExerciseLogs = ExerciseLogLab.get(mUser.getAuthUID()).getExerciseLogs();

        ExerciseLogAdapter adapter = new ExerciseLogAdapter(mExerciseLogs);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {


    }

    @Override
    public void onResume() {
        super.onResume();
        ((ExerciseLogAdapter)getListAdapter()).notifyDataSetChanged();
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

            Log.i("Exercise Log", "Exercise Log: " + e.toString());

            TextView exerciseTitle = (TextView) convertView.findViewById(R.id.exercise_log_list_item_exercisetitle);
            exerciseTitle.setText(exercise.getExerciseTitle());

            TextView workoutTitle = (TextView) convertView.findViewById(R.id.exercise_log_list_item_workouttitle);
            workoutTitle.setText(workout.getWorkoutTitle());

            TextView date = (TextView) convertView.findViewById(R.id.exercise_log_list_item_date);
            Date theDate = new Date(e.getExerciseDate());
            Calendar theCal = Calendar.getInstance();
            theCal.setTime(theDate);
            date.setText(theCal.toString());

            return convertView;
        }
    }
}