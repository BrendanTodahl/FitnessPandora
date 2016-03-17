package edu.osu.fitnesspandora;

import java.util.Date;

/**
 * Created by Kyle on 3/16/2016.
 */
public class ExerciseLog {

    // Store the date as a long for milliseconds
    private Long exerciseDate;
    private int exerciseID;
    private int workoutID;
    // Score the likeness score: -1 dislikes, 0 skipped, 1 likes
    private int exerciseScore;

    public ExerciseLog(Long exerciseDate, int exerciseID, int workoutID, int exerciseScore) {
        this.exerciseDate = exerciseDate;
        this.exerciseID = exerciseID;
        this.workoutID = workoutID;
        this.exerciseScore = exerciseScore;
    }

    public Long getExerciseDate() {
        return exerciseDate;
    }

    public void setExerciseDate(Long exerciseDate) {
        this.exerciseDate = exerciseDate;
    }

    public int getExerciseID() {
        return exerciseID;
    }

    public void setExerciseID(int exerciseID) {
        this.exerciseID = exerciseID;
    }

    public int getWorkoutID() {
        return workoutID;
    }

    public void setWorkoutID(int workoutID) {
        this.workoutID = workoutID;
    }

    public int getexerciseScore() {
        return exerciseScore;
    }

    public void setexerciseScore(int exerciseScore) {
        this.exerciseScore = exerciseScore;
    }
}
