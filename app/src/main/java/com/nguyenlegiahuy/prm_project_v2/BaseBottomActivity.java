package com.nguyenlegiahuy.prm_project_v2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nguyenlegiahuy.prm_project_v2.R;

public abstract class BaseBottomActivity extends AppCompatActivity {

    protected abstract int getMenuResourceId();
    protected abstract Fragment getDefaultFragment();
    protected abstract boolean onNavigationSelected(MenuItem item);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_bottom);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.inflateMenu(getMenuResourceId());

        // Load default fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, getDefaultFragment())
                .commit();

        bottomNav.setOnItemSelectedListener(item -> onNavigationSelected(item));
    }
}
