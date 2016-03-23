package edu.osu.fitnesspandora;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class WorkoutListActivity extends SingleFragmentActivity  {
    @Override
    protected Fragment createFragment() {
        return new WorkoutListFragment();
    }
}
