package edu.osu.fitnesspandora;


import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

// SINGLETON for all exercise logs
public class ExerciseLogLab {

    private ArrayList<ExerciseLog> mExerciseLogs;

    private static ExerciseLogLab sExerciseLogLab = null;

    private String mUserUID = "";


    private ExerciseLogLab(String userUID) {

        mExerciseLogs = new ArrayList<ExerciseLog>();

        mUserUID = userUID;

        // Get exercises from Firebase
        Log.i("Firebase", "Starting loading Exercise Logs.");
        Firebase firebaseRef = new Firebase("https://fitnesspandora.firebaseio.com/users/" + userUID + "/exerciseLog");
        if(firebaseRef != null){
            firebaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    mExerciseLogs = new ArrayList<ExerciseLog>();

                    if (snapshot.getValue() != null) {
                        // For each exercise log,
                        for (DataSnapshot exerciseLogSnapshotRaw : snapshot.getChildren()) {
                            Long exerciseDate = Long.MIN_VALUE;
                            Long exerciseID = Long.MIN_VALUE;
                            Long workoutID = Long.MIN_VALUE;
                            Long exerciseScore = Long.MIN_VALUE;
                            // For each of the exercise logs's attributes,
                            for (DataSnapshot exerciseSnapshot : exerciseLogSnapshotRaw.getChildren()) {
                                if (exerciseSnapshot.getKey().equals("exerciseDate")) {
                                    exerciseDate = ((long) exerciseSnapshot.getValue());
                                } else if (exerciseSnapshot.getKey().equals("exerciseID")) {
                                    exerciseID = ((long) exerciseSnapshot.getValue());
                                } else if (exerciseSnapshot.getKey().equals("workoutID")) {
                                    workoutID = ((long) exerciseSnapshot.getValue());
                                } else if (exerciseSnapshot.getKey().equals("exerciseScore")) {
                                    exerciseScore = ((long) exerciseSnapshot.getValue());
                                }

                                if(exerciseDate != Long.MIN_VALUE && exerciseID != Long.MIN_VALUE && workoutID != Long.MIN_VALUE && exerciseScore != Long.MIN_VALUE){
                                    // Construct the new exercise log
                                    ExerciseLog exerciseLog = new ExerciseLog(exerciseDate, exerciseID, workoutID, exerciseScore);
                                    // Log.i("Firebase", "Added new exercise log: " + exerciseLog.toString());
                                    // Add the exercise log
                                    mExerciseLogs.add(exerciseLog);
                                }
                            }
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
    }

    public static ExerciseLogLab get(String userUID){
        if(sExerciseLogLab == null){
            sExerciseLogLab = new ExerciseLogLab(userUID);
        }else if(!sExerciseLogLab.getUserUID().equals(userUID)){
            // If the current exercise log lab isn't for the current user, reinitialize it
            sExerciseLogLab = new ExerciseLogLab(userUID);
        }
        return sExerciseLogLab;
    }

    public ArrayList<ExerciseLog> getExerciseLogs(){ return mExerciseLogs; }

    public String getUserUID(){return mUserUID;}

    // Returns the total score of a specific excercise
    public long exerciseScore(long exerciseID){
        long score = 0;
        for(ExerciseLog el : mExerciseLogs){
            if(el.getExerciseID() == exerciseID){
                score += el.getexerciseScore();
            }
        }
        return score;
    }


}

