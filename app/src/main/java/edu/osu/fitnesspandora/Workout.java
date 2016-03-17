package edu.osu.fitnesspandora;


import java.util.ArrayList;

public class Workout {

    private String workoutTitle;
    private long workoutID;
    private ArrayList<Long> workoutExerciseIDs;

    public Workout() {
        // Leaning on Firebase
    }

    public Workout(String workoutTitle, ArrayList<Long> workoutExerciseIDs) {
        this.workoutTitle = workoutTitle;
        this.workoutExerciseIDs = workoutExerciseIDs;
    }

    public String getWorkoutTitle() {
        return workoutTitle;
    }
    public void setWorkoutTitle(String newWorkoutTitle) {
        workoutTitle = newWorkoutTitle;
    }

    public ArrayList<Long> getWorkoutExerciseIDs() {
        return workoutExerciseIDs;
    }
    public void setWorkoutExerciseIDs(ArrayList<Long> newWorkoutExerciseIDs) {
        workoutExerciseIDs = newWorkoutExerciseIDs;
    }

    public Long getWorkoutID() {
        return workoutID;
    }
    public void setWorkoutID(Long workoutID) {
        this.workoutID = workoutID;
    }

    @Override
    public String toString() {
        return workoutTitle;
    }

    public boolean isValidExerciseID(long exerciseID){
        for(int i = 0; i < workoutExerciseIDs.size(); i++){
            long param = exerciseID;
            long local = (long) workoutExerciseIDs.get(i);
            if(param == local){
                return true;
            }

        }
        return false;
    }

}
