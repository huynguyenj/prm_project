package com.nguyenlegiahuy.prm_project_v2;

import android.view.MenuItem;

import androidx.fragment.app.Fragment;

public class DealerManagerMainActivity extends BaseBottomActivity{
    @Override
    protected int getMenuResourceId() {
        return 0;
    }

    @Override
    protected Fragment getDefaultFragment() {
        return null;
    }

    @Override
    protected boolean onNavigationSelected(MenuItem item) {
        return false;
    }
}
