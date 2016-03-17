package edu.osu.fitnesspandora;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;

public class WorkoutListActivity extends SingleFragmentActivity  {
    @Override
    protected Fragment createFragment() {
        return new WorkoutListFragment();
    }
}
