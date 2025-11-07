package com.nguyenlegiahuy.prm_project_v2.activities.admin;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.nguyenlegiahuy.prm_project_v2.R;
import com.nguyenlegiahuy.prm_project_v2.adapter.role.RoleAdapter;
import com.nguyenlegiahuy.prm_project_v2.api.AdminApiService;
import com.nguyenlegiahuy.prm_project_v2.api.ApiClient;
import com.nguyenlegiahuy.prm_project_v2.models.admin.staff.CreateStaffRequest;
import com.nguyenlegiahuy.prm_project_v2.models.admin.staff.CreateStaffResponse;
import com.nguyenlegiahuy.prm_project_v2.models.admin.staff.Role;
import com.nguyenlegiahuy.prm_project_v2.models.admin.staff.RoleResponse;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateStaffActivity extends AppCompatActivity {

    private static final String TAG = "CreateStaffActivity";

    private MaterialToolbar toolbar;
    private TextInputEditText etUsername, etPassword, etFullname, etEmail, etPhone, etAddress;
    private RecyclerView rolesRecyclerView;
    private ProgressBar progressBar;
    private MaterialButton btnCreate;

    private RoleAdapter roleAdapter;
    private AdminApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_staff);

        apiService = ApiClient.getClient().create(AdminApiService.class);

        initViews();
        setupToolbar();
        setupRecyclerView();
        loadRoles();
        setupClickListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etFullname = findViewById(R.id.etFullname);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        rolesRecyclerView = findViewById(R.id.rolesRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        btnCreate = findViewById(R.id.btnCreate);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        roleAdapter = new RoleAdapter();
        rolesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rolesRecyclerView.setAdapter(roleAdapter);
    }

    private void loadRoles() {
        showLoading(true);

        apiService.getAllRoles().enqueue(new Callback<RoleResponse>() {
            @Override
            public void onResponse(Call<RoleResponse> call, Response<RoleResponse> response) {
                showLoading(false);

                    RoleResponse roleResponse = response.body();

                    if (roleResponse.getStatusCode() == 200 && roleResponse.getData() != null) {
                        List<Role> roleList = roleResponse.getData();
                        roleList = roleList.stream().filter(role ->
                                !Objects.equals(role.getRoleName().toLowerCase(), "admin")
                                        && !Objects.equals(role.getRoleName().toLowerCase(), "customer")).collect(Collectors.toList());
                        roleAdapter.setRoleList(roleList);
                    } else {
                        Toast.makeText(CreateStaffActivity.this,
                                roleResponse.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
            }

            @Override
            public void onFailure(Call<RoleResponse> call, Throwable t) {
                showLoading(false);
                Toast.makeText(CreateStaffActivity.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Failed to load roles", t);
            }
        });
    }

    private void setupClickListeners() {
        btnCreate.setOnClickListener(v -> createStaff());
    }

    private void createStaff() {
        // Get input values
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String fullname = etFullname.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        List<Integer> selectedRoles = roleAdapter.getSelectedRoleIds();

        // Validate inputs
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Username is required");
            etUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(fullname)) {
            etFullname.setError("Full name is required");
            etFullname.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email");
            etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Phone is required");
            etPhone.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(address)) {
            etAddress.setError("Address is required");
            etAddress.requestFocus();
            return;
        }

        if (selectedRoles.isEmpty()) {
            Toast.makeText(this, "Please select at least one role", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create staff request
        CreateStaffRequest request = new CreateStaffRequest(
                username, password, fullname, email, phone, address, selectedRoles
        );

        // Show loading
        showLoading(true);
        btnCreate.setEnabled(false);

        // Make API call
        apiService.createStaff(request).enqueue(new Callback<CreateStaffResponse>() {
            @Override
            public void onResponse(Call<CreateStaffResponse> call, Response<CreateStaffResponse> response) {
                showLoading(false);
                btnCreate.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    CreateStaffResponse staffResponse = response.body();

                    String message = staffResponse.getMessage() != null ?
                            staffResponse.getMessage() : "Staff created successfully";

                    Toast.makeText(CreateStaffActivity.this, message, Toast.LENGTH_SHORT).show();

                    // Go back to staff list
                    finish();
                } else {
                    Toast.makeText(CreateStaffActivity.this,
                            "Failed to create staff",
                            Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Response error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<CreateStaffResponse> call, Throwable t) {
                showLoading(false);
                btnCreate.setEnabled(true);

                String errorMsg = t.getMessage() != null ?
                        "Error: " + t.getMessage() :
                        "Network error occurred";
                Toast.makeText(CreateStaffActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Failed to create staff", t);
            }
        });
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}