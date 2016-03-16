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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;

public class WorkoutActivity  extends AppCompatActivity {

    public static final String EXTRA_WORKOUT_TITLE =
            "com.bignerdranch.android.criminalintent.workout_title";

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

        setTitle("Workout");

        // Get the one workout the user has selected to do
        mWorkout = WorkoutLab.get().getWorkout((String) getIntent().getSerializableExtra(EXTRA_WORKOUT_TITLE));
        // Get our required data
        mUser = User.get(this);
        mExercises = ExerciseLab.get().getExercises();

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
            if(mWorkout.getWorkoutExerciseIDs().contains(tempExercise.getExerciseID())){
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

        mCurrentExerciseIndex++;
        // If the end of the exercises has been reached, roll back to zero
        if(mCurrentExerciseIndex >= mExercises.size()){
            mCurrentExerciseIndex = 0;
        }

        Exercise newExercise = mExercises.get(mCurrentExerciseIndex);

        mExerciseTitle.setText(newExercise.getExerciseTitle());
        mExerciseInstructions.setText("This is how you do this trivially easy and understandable exercises...");

        if(userLikes){
            // TODO Edit Firebase and log the users like
        }else{
            // TODO Edit Firebase and log the users dislike
        }


    }



}
