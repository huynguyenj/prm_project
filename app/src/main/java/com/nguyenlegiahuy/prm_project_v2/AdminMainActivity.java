package com.nguyenlegiahuy.prm_project_v2;

import android.view.MenuItem;

import androidx.fragment.app.Fragment;

import com.nguyenlegiahuy.prm_project_v2.fragments.admin.AdminDashboardFragment;
import com.nguyenlegiahuy.prm_project_v2.fragments.admin.AgencyManagementFragment;
import com.nguyenlegiahuy.prm_project_v2.fragments.admin.ReportFragment;
import com.nguyenlegiahuy.prm_project_v2.fragments.admin.StaffManagementFragment;

public class AdminMainActivity extends BaseBottomActivity {
    @Override
    protected int getMenuResourceId() {
        return R.menu.menu_bottom_admin;
    }

    @Override
    protected Fragment getDefaultFragment() {
        return new AdminDashboardFragment();
    }

    @Override
    protected boolean onNavigationSelected(MenuItem item) {
        Fragment fragment = null;
        if (item.getItemId() == R.id.nav_dashboard) {
            fragment = new AdminDashboardFragment();
        }
        else if (item.getItemId() == R.id.nav_users) {
            fragment = new StaffManagementFragment();
        }
//        else if (item.getItemId() == R.id.nav_agency) {
//            fragment = new AgencyManagementFragment();
//        }
//        else if (item.getItemId() == R.id.nav_reports) {
//            fragment = new ReportFragment();
//        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
