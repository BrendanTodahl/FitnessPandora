package edu.osu.fitnesspandora;

import android.app.Fragment;
import android.app.ListActivity;

public class StatisticListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new StatisticListFragment();
    }

}
