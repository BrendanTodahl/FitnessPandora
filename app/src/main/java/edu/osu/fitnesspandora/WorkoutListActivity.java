package edu.osu.fitnesspandora;

import android.app.Fragment;
import android.app.ListActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class WorkoutListActivity extends ListActivity {

    WorkoutLab test = new WorkoutLab();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, this.getWorkoutNames()));
    }



    private static String[] getWorkoutNames(){
        WorkoutLab workouts = new WorkoutLab();

        //for some reason getWorkouts.size is size 0, it isn't returning anything
        String[] workoutNames = {((Integer)workouts.getWorkouts().size()).toString(),"hey"};

        //I'm having trouble accessing the workoutlab arraylist, this works with the test array though
        /*for(int i = 0; i < workouts.getWorkouts().size(); i++) {
            workoutNames[i] = workouts.getWorkouts().get(i).getWorkoutTitle();
        }*/
        String[] test = {"this", "tests","the", "dynamic", "listview"};

        //to see that the mWorkouts size is zero, change this to return workoutNames and see the 0 in the listview
        return test;
    }
}
