package edu.osu.fitnesspandora;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;

public class WorkoutActivity  extends AppCompatActivity {

    public static final String EXTRA_WORKOUT_ID =
            "com.bignerdranch.android.criminalintent.workout_id";

    private User mUser;
    private ArrayList<Exercise> mExercises;
    private Workout mWorkout;
    private ExerciseLogLab mExerciseLogLab;

    private TextView mExerciseTitle;
    //private TextView mExerciseInstructions;
    private Button mSkipButton;
    private Button mLikeButton;
    private Button mInstructionButton;
    private EditText mEditReps;
    private EditText mEditWeight;

    private int mCurrentExerciseIndex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_workout);

        // Get the one workout the user has selected to do
        mWorkout = WorkoutLab.get().getWorkout((long) getIntent().getSerializableExtra(EXTRA_WORKOUT_ID));
        // Get our required data
        mUser = User.get(this);
        mExercises = ExerciseLab.get().getExercises();
        mExerciseLogLab = ExerciseLogLab.get(mUser.getAuthUID());

        setTitle(mWorkout.getWorkoutTitle() + " Workout");

        // Attach to the layout
        mExerciseTitle = (TextView) findViewById(R.id.exercise_title);
        //mExerciseInstructions = (TextView) findViewById(R.id.exercise_instructions);
        mSkipButton = (Button) findViewById(R.id.button_skip);
        mLikeButton = (Button) findViewById(R.id.button_like);
        mInstructionButton = (Button) findViewById(R.id.exercise_instructions);

        // TODO Read the index back from bundle state save

        // Build the priority exercise queue
        buildExercisePriorityQueue();
        // Initialize the first exercise
        mCurrentExerciseIndex = 0;
        Exercise newExercise = mExercises.get(mCurrentExerciseIndex);
        mExerciseTitle.setText(newExercise.getExerciseTitle());
        //mExerciseInstructions.setText("This is how you do this trivially easy and understandable exercises...");

        mEditReps = (EditText) findViewById(R.id.editReps);
        mEditWeight = (EditText) findViewById(R.id.editWeight);

        // Setup the button responses
        mSkipButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // The user dislikes the exercise
                nextExercise(false);
            }
        });
        mLikeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // The user likes the exercise
                nextExercise(true);
            }
        });

        //launch a youtube instructional video
        mInstructionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent youtube = new Intent(Intent.ACTION_VIEW, Uri.parse(mExercises.get(mCurrentExerciseIndex).getExerciseURL()));
                startActivity(youtube);
            }
        });



    }





    // EXERCISE SELECTION ALGORITHMS

    // Alter mExercises so the lower indices are the exercises up next
    private void buildExercisePriorityQueue(){

        // TODO Make this waaaaaaay better.

        ArrayList<Exercise> exercisePriorityQueue = new ArrayList<Exercise>();
        ArrayList<Exercise> exerciseBadQueue = new ArrayList<Exercise>();

        // Get all exercises that are linked to the current workout
        for(int i =0; i < mExercises.size(); i++){
            Exercise tempExercise = mExercises.get(i);
            Log.i("Firebase", mWorkout.getWorkoutExerciseIDs().toString());
            Log.i("Firebase", ""+tempExercise.getExerciseID());
            Log.i("Firebase", "" + mWorkout.getWorkoutExerciseIDs().contains(tempExercise.getExerciseID()));
            if(mWorkout.isValidExerciseID(tempExercise.getExerciseID())){
                exercisePriorityQueue.add(tempExercise);
            }else{
                //comment this out so exercises that aren't part of the category aren't cycled through
                //exerciseBadQueue.add(tempExercise);
            }
        }

        // Sort the priority queue with the highest scored exercise being first
        Collections.sort(exercisePriorityQueue, new CompareExerciseScores());

        // Now add all of the bad exercises to the tail of the priority queue
        for(Exercise e : exerciseBadQueue){
            exercisePriorityQueue.add(e);
        }

        // Now set the current set of exercises to the priority queue
        mExercises = exercisePriorityQueue;

    }



    private void nextExercise(boolean userLikes){

        // If the user 'Likes', value is 1. Otherwise, if the user 'Skips', the value is -1.
        long score = 0;
        if(userLikes){
            score = 1;
        }else{
            score = -1;
        }

        // Make the new exercise log for the user
        // Date will be in raw milliseconds. PREFERRED
        ExerciseLog newExerciseLog = new ExerciseLog(new Date().getTime(), mExercises.get(mCurrentExerciseIndex).getExerciseID(), mWorkout.getWorkoutID(), score, Long.valueOf(mEditReps.getText().toString()),Long.valueOf(mEditWeight.getText().toString()));

        // Add the exercise log to the users account
        Firebase firebaseRef = new Firebase(getString(R.string.firebase_url) + "users/" + mUser.getAuthUID() + "/exerciseLog");
        Firebase newLogRef = firebaseRef.push();
        newLogRef.setValue(newExerciseLog);


        mCurrentExerciseIndex++;
        // roll back to the first exercise index in the category
        if(mCurrentExerciseIndex >= mExercises.size()){
            mCurrentExerciseIndex = 0;
        }

        Exercise newExercise = mExercises.get(mCurrentExerciseIndex);

        mExerciseTitle.setText(newExercise.getExerciseTitle());

        //mExerciseInstructions.setText("This is how you do this trivially easy and understandable exercises...");




    }

    public class CompareExerciseScores implements Comparator<Exercise> {
        @Override
        public int compare(Exercise e1, Exercise e2) {
            return Integer.parseInt(Long.toString(mExerciseLogLab.exerciseScore(e2.getExerciseID()) - mExerciseLogLab.exerciseScore(e1.getExerciseID())));
        }
    }



}
