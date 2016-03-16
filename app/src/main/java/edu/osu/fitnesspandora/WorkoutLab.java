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

    private static WorkoutLab sWorkoutLab;
    private Context mAppContext;

    private WorkoutLab(Context appContext){

        mAppContext = appContext;

        mWorkouts = new ArrayList<Workout>();

        // Get workouts from Firebase

        // TESTING WORKOUTS
        Firebase firebaseWorkoutRef = new Firebase("https://fitnesspandora.firebaseio.com/workouts/");
        firebaseWorkoutRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    Log.i("Firebase Workouts", snapshot.getValue().toString());
                    Log.i("Firebase Workouts", snapshot.child("full_body").getValue().toString());


                    // For each workout,
                    for(DataSnapshot workoutSnapshotRaw : snapshot.getChildren()){

                        Workout workout = new Workout();

                        // For each of the workout's arrtibutes,
                        for(DataSnapshot workoutSnapshot : workoutSnapshotRaw.getChildren()){
                            if(workoutSnapshot.getKey().equals("workoutExerciseIDs")){
                                workout.setWorkoutExerciseIDs((ArrayList<Integer>) workoutSnapshot.getValue());
                            }else if(workoutSnapshot.getKey().equals("workoutTitle")){
                                workout.setWorkoutTitle((String) workoutSnapshot.getValue());
                            }
                        }

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

        // TODO - Make the wait on the network a little better/faster
        // Wait for the data to load in
        try {
            // Delay to simulate network access.
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        }

    }

    public static WorkoutLab get(Context c){
        if(sWorkoutLab == null){
            sWorkoutLab = new WorkoutLab(c.getApplicationContext());
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
