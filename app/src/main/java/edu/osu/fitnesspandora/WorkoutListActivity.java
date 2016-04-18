package edu.osu.fitnesspandora;

import android.app.Fragment;

public class WorkoutListActivity extends SingleFragmentActivity  {
    @Override
    protected Fragment createFragment() {
        return new WorkoutListFragment();
    }
}
