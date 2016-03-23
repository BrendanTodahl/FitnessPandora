package edu.osu.fitnesspandora;

import android.app.Fragment;
import android.app.ListActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

//this will load the workout names in a listview and will be dynamic based on what we have in workout lab
//will have to update it a bit to get the view right
/* I will also add click listeners that will launch intents and pass the data about which one
was clicked to the exercise activity so it knows which category of exercises to choose from
 */
public class WorkoutListActivity extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, this.getWorkoutNames()));
    }


    //this method gets the workout names from the Workout Lab and is used for the ListAdapter
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
