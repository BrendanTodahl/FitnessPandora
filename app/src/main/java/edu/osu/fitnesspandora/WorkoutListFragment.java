package edu.osu.fitnesspandora;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class WorkoutListFragment extends ListFragment {

    private static final String TAG = "WorkoutListFragment";
    private static Workout mPedometer;
    private ArrayList<Workout> mWorkouts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Select Workout");

        mWorkouts = WorkoutLab.get().getWorkouts();
        // -- Start added code. NEEDS TO BE DONE ANOTHER WAY
        if (mPedometer == null) {
            mPedometer = new Workout("Pedometer", new ArrayList<Long>());
            mWorkouts.add(mPedometer);
        }
        // -- end added code
        WorkoutAdapter adapter = new WorkoutAdapter(mWorkouts);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Workout w = ((WorkoutAdapter)getListAdapter()).getItem(position);
        if (w.getWorkoutTitle().equals("Pedometer")) { // Special case for the pedometer activity for now...
            Intent i = new Intent(getActivity(), PedometerActivity.class);
            startActivity(i);
        } else {
            // Start a Workout with the selected workout
            Intent i = new Intent(getActivity(), WorkoutActivity.class);
            i.putExtra(WorkoutActivity.EXTRA_WORKOUT_ID, w.getWorkoutID());
            startActivity(i);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((WorkoutAdapter)getListAdapter()).notifyDataSetChanged();
    }

    private class WorkoutAdapter extends ArrayAdapter<Workout> {

        public WorkoutAdapter(ArrayList<Workout> workouts) {
            super(getActivity(), 0, workouts);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // If we weren't given a view, inflate one
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_workout, null);
            }
            // Configure the view for this Workout
            Workout w = getItem(position);
            TextView titleTextView =
                    (TextView)convertView.findViewById(R.id.workout_list_item_title);
            titleTextView.setText(w.getWorkoutTitle());
            return convertView;
        }
    }
}