package edu.osu.fitnesspandora;


public class Exercise {

    private long exerciseID;
    private String exerciseTitle;

    public Exercise(){
        // Empty for Firebase
    }

    public long getExerciseID() {
        return exerciseID;
    }

    public void setExerciseID(long exerciseID) {
        this.exerciseID = exerciseID;
    }

    public String getExerciseTitle() {
        return exerciseTitle;
    }

    public void setExerciseTitle(String exerciseTitle) {
        this.exerciseTitle = exerciseTitle;
    }

    @Override
    public String toString() {
        return ("Exercise ID: " + exerciseID + " Title: " + exerciseTitle);
    }
}
