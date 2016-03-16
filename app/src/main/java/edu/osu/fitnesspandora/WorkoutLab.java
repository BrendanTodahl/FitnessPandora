package edu.osu.fitnesspandora;


import android.content.Context;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WorkoutLab {

    private ArrayList<Workout> mWorkouts;

    private static WorkoutLab sWorkoutLab = null;

    private WorkoutLab(){

        mWorkouts = new ArrayList<Workout>();

        // Get workouts from Firebase

        Log.i("Firebase", "Starting loading Workouts.");

        Firebase firebaseWorkoutRef = new Firebase("https://fitnesspandora.firebaseio.com/workouts/");
        firebaseWorkoutRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    // For each workout,
                    for(DataSnapshot workoutSnapshotRaw : snapshot.getChildren()){
                        Workout workout = new Workout();
                        // For each of the workout's attributes,
                        for(DataSnapshot workoutSnapshot : workoutSnapshotRaw.getChildren()){
                            if(workoutSnapshot.getKey().equals("workoutExerciseIDs")){
                                workout.setWorkoutExerciseIDs((ArrayList<Integer>) workoutSnapshot.getValue());
                            }else if(workoutSnapshot.getKey().equals("workoutTitle")){
                                workout.setWorkoutTitle((String) workoutSnapshot.getValue());
                            }
                        }
                        // Add the workout
                        mWorkouts.add(workout);
                        Log.i("Firebase", "Added new workout: " + workout + " with eids: " + workout.getWorkoutExerciseIDs());
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

    public static WorkoutLab get(){
        if(sWorkoutLab == null){
            sWorkoutLab = new WorkoutLab();
        }
        return sWorkoutLab;
    }

    public ArrayList<Workout> getWorkouts(){ return mWorkouts; }

    public Workout getWorkout(String workoutTitle){
        for (Workout w : mWorkouts){
            if(w.getWorkoutTitle().equals(workoutTitle)){
                return w;
            }
        }
        return null;
    }

}
