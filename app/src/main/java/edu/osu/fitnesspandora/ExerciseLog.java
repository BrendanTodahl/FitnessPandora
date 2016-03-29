package edu.osu.fitnesspandora;

import java.util.Date;

/**
 * Created by Kyle on 3/16/2016.
 */
public class ExerciseLog {

    // Store the date as a long for milliseconds
    private Long exerciseDate;
    private Long exerciseID;
    private Long workoutID;
    // Score the likeness score: -1 dislikes, 1 likes
    private Long exerciseScore;
    private Long exerciseReps;
    private Long exerciseWeight;

    public ExerciseLog(Long exerciseDate, Long exerciseID, Long workoutID, Long exerciseScore, Long exerciseReps, Long exerciseWeight) {
        this.exerciseDate = exerciseDate;
        this.exerciseID = exerciseID;
        this.workoutID = workoutID;
        this.exerciseScore = exerciseScore;
        this.exerciseReps = exerciseReps;
        this.exerciseWeight = exerciseWeight;
    }

    public long getExerciseReps(){
        return this.exerciseReps;
    }

    public long getExerciseWeight(){
        return this.exerciseWeight;
    }

    public Long getExerciseDate() {
        return exerciseDate;
    }

    public void setExerciseDate(Long exerciseDate) {
        this.exerciseDate = exerciseDate;
    }

    public Long getExerciseID() {
        return exerciseID;
    }

    public void setExerciseID(Long exerciseID) {
        this.exerciseID = exerciseID;
    }

    public Long getWorkoutID() {
        return workoutID;
    }

    public void setWorkoutID(Long workoutID) {
        this.workoutID = workoutID;
    }

    public Long getexerciseScore() {
        return exerciseScore;
    }

    public void setexerciseScore(Long exerciseScore) {
        this.exerciseScore = exerciseScore;
    }

    @Override
    public String toString(){
        return "Date: " + exerciseDate + " EID: " + exerciseID + " WID: " + workoutID + " Score: " + exerciseScore;
    }
}
