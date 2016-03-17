package edu.osu.fitnesspandora;


import java.util.ArrayList;

public class Workout {

    private String workoutTitle;
    private int workoutID;
    private ArrayList<Integer> workoutExerciseIDs;

    public Workout() {
        // Leaning on Firebase
    }

    public Workout(String workoutTitle, ArrayList<Integer> workoutExerciseIDs) {
        this.workoutTitle = workoutTitle;
        this.workoutExerciseIDs = workoutExerciseIDs;
    }

    public String getWorkoutTitle() {
        return workoutTitle;
    }
    public void setWorkoutTitle(String newWorkoutTitle) {
        workoutTitle = newWorkoutTitle;
    }

    public ArrayList<Integer> getWorkoutExerciseIDs() {
        return workoutExerciseIDs;
    }
    public void setWorkoutExerciseIDs(ArrayList<Integer> newWorkoutExerciseIDs) {
        workoutExerciseIDs = newWorkoutExerciseIDs;
    }

    public int getWorkoutID() {
        return workoutID;
    }
    public void setWorkoutID(int workoutID) {
        this.workoutID = workoutID;
    }

    @Override
    public String toString() {
        return workoutTitle;
    }
}
