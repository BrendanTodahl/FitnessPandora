package edu.osu.fitnesspandora;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;

public class WorkoutActivity extends SingleFragmentActivity  {

    @Override
    protected Fragment createFragment() {
        return new WorkoutListFragment();
    }


}
