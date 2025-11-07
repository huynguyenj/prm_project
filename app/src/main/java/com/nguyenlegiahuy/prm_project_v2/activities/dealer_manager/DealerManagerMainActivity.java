package com.nguyenlegiahuy.prm_project_v2.activities.dealer_manager;

import android.content.Intent;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.nguyenlegiahuy.prm_project_v2.BaseBottomActivity;
import com.nguyenlegiahuy.prm_project_v2.LoginActivity;
import com.nguyenlegiahuy.prm_project_v2.R;
import com.nguyenlegiahuy.prm_project_v2.fragments.admin.AdminDashboardFragment;
import com.nguyenlegiahuy.prm_project_v2.fragments.admin.AgencyManagementFragment;
import com.nguyenlegiahuy.prm_project_v2.fragments.admin.StaffManagementFragment;
import com.nguyenlegiahuy.prm_project_v2.utils.SessionManager;

public class DealerManagerMainActivity extends BaseBottomActivity {
    @Override
    protected int getMenuResourceId() {
        return R.menu.menu_bottom_dealer_staff;
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

