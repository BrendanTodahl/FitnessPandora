package edu.osu.fitnesspandora;


public class Exercise {

    private int exerciseID;
    private String exerciseTitle;

    public Exercise(){
        // Empty for Firebase
    }

    public int getExerciseID() {
        return exerciseID;
    }

    public void setExerciseID(int exerciseID) {
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
