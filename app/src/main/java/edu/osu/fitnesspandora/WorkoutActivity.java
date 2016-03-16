package edu.osu.fitnesspandora;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

public class WorkoutActivity  extends AppCompatActivity {

    public static final String EXTRA_WORKOUT_TITLE =
            "com.bignerdranch.android.criminalintent.workout_title";


    private ArrayList<Exercise> mExercises;

    private Workout mWorkout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_workout);

        setTitle("Workout");

        mWorkout = WorkoutLab.get().getWorkout((String) getIntent().getSerializableExtra(EXTRA_WORKOUT_TITLE));






    }







}
