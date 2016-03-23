package edu.osu.fitnesspandora;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class WorkoutActivity  extends AppCompatActivity {

    public static final String EXTRA_WORKOUT_ID =
            "com.bignerdranch.android.criminalintent.workout_id";

    private User mUser;
    private ArrayList<Exercise> mExercises;
    private Workout mWorkout;

    private TextView mExerciseTitle;
    private TextView mExerciseInstructions;
    private Button mSkipButton;
    private Button mLikeButton;

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

        setTitle(mWorkout.getWorkoutTitle() + " Workout");

        // Attach to the layout
        mExerciseTitle = (TextView) findViewById(R.id.exercise_title);
        mExerciseInstructions = (TextView) findViewById(R.id.exercise_instructions);
        mSkipButton = (Button) findViewById(R.id.button_skip);
        mLikeButton = (Button) findViewById(R.id.button_like);

        // TODO Read the index back from bundle state save

        // Build the priority exercise queue
        buildExercisePriorityQueue();
        // Initialize the first exercise
        mCurrentExerciseIndex = 0;
        Exercise newExercise = mExercises.get(mCurrentExerciseIndex);
        mExerciseTitle.setText(newExercise.getExerciseTitle());
        mExerciseInstructions.setText("This is how you do this trivially easy and understandable exercises...");

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
                exerciseBadQueue.add(tempExercise);
            }
        }

        // Now add all of the bad exercises to the tail of the priority queue
        for(Exercise e : exerciseBadQueue){
            exercisePriorityQueue.add(e);
        }

        // Now set the current set of exercises to the priority queue
        mExercises = exercisePriorityQueue;

    }

    private void nextExercise(boolean userLikes){

        // TODO ADD DISLIKE - SKIP - LIKES
        long score = 0;
        if(userLikes){
            score = 1;
        }else{
            score = -1;
        }

        // Make the new exercise log for the user
        // Date will be in raw milliseconds. PREFERRED
        ExerciseLog newExerciseLog = new ExerciseLog(new Date().getTime(), mExercises.get(mCurrentExerciseIndex).getExerciseID(), mWorkout.getWorkoutID(), score);

        // Add the exercise log to the users account
        Firebase firebaseRef = new Firebase(getString(R.string.firebase_url) + "users/" + mUser.getAuthUID() + "/exerciseLog");
        Firebase newLogRef = firebaseRef.push();
        newLogRef.setValue(newExerciseLog);


        mCurrentExerciseIndex++;
        // If the current exercise index isn't an exercise in the workout category, roll back to first in the category
        if(!mWorkout.getWorkoutExerciseIDs().contains(mExercises.get(mCurrentExerciseIndex).getExerciseID())){
            mCurrentExerciseIndex = 0;
        }

        Exercise newExercise = mExercises.get(mCurrentExerciseIndex);

        mExerciseTitle.setText(newExercise.getExerciseTitle());

        mExerciseInstructions.setText("This is how you do this trivially easy and understandable exercises...");




    }



}
