package edu.osu.fitnesspandora;

import android.app.Fragment;

public class StatisticListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new StatisticListFragment();
    }

}
