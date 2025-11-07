package com.nguyenlegiahuy.prm_project_v2.fragments.admin;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.nguyenlegiahuy.prm_project_v2.R;
import com.nguyenlegiahuy.prm_project_v2.activities.admin.CreateStaffActivity;
import com.nguyenlegiahuy.prm_project_v2.adapter.staff.StaffAdapter;
import com.nguyenlegiahuy.prm_project_v2.api.AdminApiService;
import com.nguyenlegiahuy.prm_project_v2.api.ApiClient;
import com.nguyenlegiahuy.prm_project_v2.models.admin.staff.CreateStaffResponse;
import com.nguyenlegiahuy.prm_project_v2.models.admin.staff.DeleteStaffResponse;
import com.nguyenlegiahuy.prm_project_v2.models.admin.staff.Staff;
import com.nguyenlegiahuy.prm_project_v2.models.admin.staff.StaffListResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffManagementFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvError;
    private MaterialButton btnCreateStaff;

    private StaffAdapter adapter;
    private LinearLayoutManager layoutManager;

    private int currentPage = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private final int LIMIT = 8;
    private final List<Call<?>> activeCalls = new ArrayList<>(); // lưu các request để cancel

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_staff_management, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        tvError = view.findViewById(R.id.tvError);
        btnCreateStaff = view.findViewById(R.id.btnCreateStaff);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new StaffAdapter(requireContext(), new ArrayList<>(), new StaffAdapter.OnStaffActionListener() {
            @Override
            public void onUpdate(Staff staff) {
                showUpdateDialog(staff);
            }

            @Override
            public void onDelete(Staff staff) {
                showDeleteConfirmDialog(staff);
            }
        });

        recyclerView.setAdapter(adapter);

        setupClickListeners();
        setupScrollListener();
        loadStaffs(currentPage);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh list when returning from CreateStaffActivity
        refreshStaffList();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Cancel all active API calls when fragment destroyed
        for (Call<?> call : activeCalls) {
            if (!call.isCanceled()) call.cancel();
        }
        activeCalls.clear();
    }

    //============================ CLICK LISTENERS ============================
    private void setupClickListeners() {
        btnCreateStaff.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), CreateStaffActivity.class);
            startActivity(intent);
        });
    }

    //============================ SCROLL FETCH DATA ============================
    private void setupScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView rv, int dx, int dy) {
                super.onScrolled(rv, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        loadStaffs(currentPage + 1);
                    }
                }
            }
        });
    }

    //============================ LOAD DATA ============================
    private void loadStaffs(int page) {
        isLoading = true;
        if (page == 1) showLoading(true);
        else adapter.addLoadingFooter();

        AdminApiService api = ApiClient.getClient().create(AdminApiService.class);
        api.getStaffList(LIMIT, page).enqueue(new Callback<StaffListResponse>() {
            @Override
            public void onResponse(@NonNull Call<StaffListResponse> call,
                                   @NonNull Response<StaffListResponse> response) {
                if (page == 1) showLoading(false);
                else adapter.removeLoadingFooter();

                isLoading = false;

                if (response.body().getStatusCode() == 200 && response.body() != null) {
                    List<Staff> newStaffs = response.body().getData();
                    if (newStaffs.isEmpty()) {
                        isLastPage = true;
                        return;
                    }

                    if (page == 1)
                        adapter.addAll(newStaffs);
                    else
                        adapter.addAll(newStaffs);

                    currentPage = page;
                } else {
                    Toast.makeText(requireContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<StaffListResponse> call, @NonNull Throwable t) {
                activeCalls.remove(call);
                if (page == 1) showLoading(false);
                else adapter.removeLoadingFooter();
                isLoading = false;
                showError("Error: " + t.getMessage());
            }
        });
    }

    private void refreshStaffList() {
        currentPage = 1;
        isLastPage = false;
        adapter.clear();
        loadStaffs(currentPage);
    }

    private void showLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(loading ? View.GONE : View.VISIBLE);
        tvError.setVisibility(View.GONE);
    }

    private void showError(String msg) {
        tvError.setVisibility(View.VISIBLE);
        tvError.setText(msg);
    }

    // ============================ UPDATE STAFF ============================
    private void showUpdateDialog(Staff staff) {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_update_staff, null);
        EditText etUsername = dialogView.findViewById(R.id.etUsername);
        EditText etFullname = dialogView.findViewById(R.id.etFullname);
        EditText etEmail = dialogView.findViewById(R.id.etEmail);
        EditText etPhone = dialogView.findViewById(R.id.etPhone);
        EditText etAddress = dialogView.findViewById(R.id.etAddress);

        etUsername.setText(staff.getUsername());
        etFullname.setText(staff.getFullname());
        etEmail.setText(staff.getEmail());
        etPhone.setText(staff.getPhone());
        etAddress.setText(staff.getAddress());

        new AlertDialog.Builder(requireContext())
                .setTitle("Update Staff")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    staff.setUsername(etUsername.getText().toString());
                    staff.setFullname(etFullname.getText().toString());
                    staff.setEmail(etEmail.getText().toString());
                    staff.setPhone(etPhone.getText().toString());
                    staff.setAddress(etAddress.getText().toString());
                    updateStaff(staff);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void updateStaff(Staff staff) {
        AdminApiService api = ApiClient.getClient().create(AdminApiService.class);
        api.updateStaff(staff.getId(), staff).enqueue(new Callback<CreateStaffResponse>() {
            @Override
            public void onResponse(@NonNull Call<CreateStaffResponse> call, @NonNull Response<CreateStaffResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStatusCode() == 200) {
                    Toast.makeText(requireContext(), "Updated successfully!", Toast.LENGTH_SHORT).show();
                    refreshStaffList();
                } else {
                    Toast.makeText(requireContext(), "Update failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CreateStaffResponse> call, @NonNull Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ============================ DELETE STAFF ============================
    private void showDeleteConfirmDialog(Staff staff) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete " + staff.getFullname() + "?")
                .setPositiveButton("Delete", (dialog, which) -> deleteStaff(staff.getId()))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteStaff(int id) {
        AdminApiService api = ApiClient.getClient().create(AdminApiService.class);
        api.deleteStaff(id).enqueue(new Callback<DeleteStaffResponse>() {
            @Override
            public void onResponse(@NonNull Call<DeleteStaffResponse> call, @NonNull Response<DeleteStaffResponse> response) {
                Log.d("Delete staff status code", "onResponse: " +response.body().getStatusCode());
                if (response.body().getStatusCode() == 200) {
                    Toast.makeText(requireContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    refreshStaffList();
                } else {
                    Toast.makeText(requireContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<DeleteStaffResponse> call, @NonNull Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

