package edu.osu.fitnesspandora;


import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

// SINGLETON for all exercises
public class ExerciseLab {

    private ArrayList<Exercise> mExercises;

    private static  ExerciseLab sExerciseLab = null;


    private ExerciseLab() {

        mExercises = new ArrayList<Exercise>();

        // Get exercises from Firebase
        Log.i("Firebase", "Starting loading Exercises.");
        Firebase firebaseWorkoutRef = new Firebase("https://fitnesspandora.firebaseio.com/exercises");
        firebaseWorkoutRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    // For each exercise,
                    for (DataSnapshot exerciseSnapshotRaw : snapshot.getChildren()) {
                        Exercise exercise = new Exercise();
                        // For each of the exercise's attributes,
                        for (DataSnapshot exerciseSnapshot : exerciseSnapshotRaw.getChildren()) {
                            if (exerciseSnapshot.getKey().equals("exerciseID")) {
                                exercise.setExerciseID(Math.round((Long) exerciseSnapshot.getValue()));
                            } else if (exerciseSnapshot.getKey().equals("exerciseTitle")) {
                                exercise.setExerciseTitle((String) exerciseSnapshot.getValue());
                            }
                        }

                        // Add the exercise
                        mExercises.add(exercise);
                        Log.i("Firebase", "Added new exercise: " + exercise.getExerciseTitle() + " with EID: " + Integer.toString(exercise.getExerciseID()));
                    }
                } else {
                    Log.e("Firebase", snapshot.toString());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

    }

    public static ExerciseLab get(){
        if(sExerciseLab == null){
            sExerciseLab = new ExerciseLab();
        }
        return sExerciseLab;
    }

    public ArrayList<Exercise> getExercises(){ return mExercises; }

    public Exercise getExercise(int exerciseID){
        for (Exercise e : mExercises){
            if(e.getExerciseID() == exerciseID){
                return e;
            }
        }
        return null;
    }
}

