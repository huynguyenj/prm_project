package com.nguyenlegiahuy.prm_project_v2.activities.evm_staff;

import android.view.MenuItem;

import androidx.fragment.app.Fragment;

import com.nguyenlegiahuy.prm_project_v2.BaseBottomActivity;

public class EvmStaffMainActivity extends BaseBottomActivity {
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
