package edu.osu.fitnesspandora;

import android.app.Fragment;
import android.app.ListActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;


public class WorkoutListActivity extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_item_workout);

        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, this.getWorkoutNames()));
    }



    private static String[] getWorkoutNames(){
        WorkoutLab workoutLab = WorkoutLab.get();
        ArrayList<Workout> workouts = workoutLab.getWorkouts();


        String[] workoutNames = new String[workouts.size()];

        for(int i = 0; i < workouts.size(); i++) {
            workoutNames[i] = workouts.get(i).getWorkoutTitle();
        }

        return workoutNames;
    }
}
