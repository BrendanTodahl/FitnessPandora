package edu.osu.fitnesspandora;


import java.util.ArrayList;

public class Workout {

    private String workoutTitle;
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
    public void setWorkoutTitle(String workoutTitle) {
        workoutTitle = workoutTitle;
    }

    public ArrayList<Integer> getWorkoutExerciseIDs() {
        return workoutExerciseIDs;
    }
    public void setWorkoutExerciseIDs(ArrayList<Integer> workoutExerciseIDs) {
        workoutExerciseIDs = workoutExerciseIDs;
    }

    @Override
    public String toString() {
        return workoutTitle;
    }
}
