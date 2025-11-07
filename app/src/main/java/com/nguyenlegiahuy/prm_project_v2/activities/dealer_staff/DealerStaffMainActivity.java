package com.nguyenlegiahuy.prm_project_v2.activities.dealer_staff;

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
import com.nguyenlegiahuy.prm_project_v2.fragments.dealer_staff.CustomerContractFragment;
import com.nguyenlegiahuy.prm_project_v2.fragments.dealer_staff.CustomerFragment;
import com.nguyenlegiahuy.prm_project_v2.fragments.dealer_staff.DealerHomeFragment;
import com.nguyenlegiahuy.prm_project_v2.fragments.dealer_staff.DepositFragment;
import com.nguyenlegiahuy.prm_project_v2.fragments.dealer_staff.QuotationFragment;
import com.nguyenlegiahuy.prm_project_v2.utils.SessionManager;

public class DealerStaffMainActivity extends BaseBottomActivity {
    @Override
    protected int getMenuResourceId() {
        return R.menu.menu_bottom_dealer_staff;
    }

    @Override
    protected Fragment getDefaultFragment() {
        return new DealerHomeFragment();
    }

    @Override
    protected boolean onNavigationSelected(MenuItem item) {
        Fragment fragment = null;
        if (item.getItemId() == R.id.nav_home_dealer) {
            fragment = new DealerHomeFragment();
        }
        else if (item.getItemId() == R.id.nav_customers) {
            fragment = new CustomerFragment();
        }
        else if (item.getItemId() == R.id.nav_quotations) {
            fragment = new QuotationFragment();
        }
        else if (item.getItemId() == R.id.nav_deposit) {
            fragment = new DepositFragment();
        }
        else if (item.getItemId() == R.id.nav_customer_contract) {
            fragment = new CustomerContractFragment();
        }

        else if (item.getItemId() == R.id.nav_logout) {
            handleLogout();
            return true;
        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit();
            return true;
        }
        return false;
    }
    private void handleLogout() {
        new SessionManager(this).clearSession();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        // Quay lại LoginActivity
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
