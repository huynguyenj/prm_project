package com.nguyenlegiahuy.prm_project_v2;

import android.view.MenuItem;

import androidx.fragment.app.Fragment;

public class EvmStaffMainActivity extends BaseBottomActivity{
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
