package com.nguyenlegiahuy.prm_project_v2.fragments.admin;

import android.app.AlertDialog;
import android.os.Bundle;
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
import com.nguyenlegiahuy.prm_project_v2.adapter.agency.AgencyAdapter;
import com.nguyenlegiahuy.prm_project_v2.api.AdminApiService;
import com.nguyenlegiahuy.prm_project_v2.api.ApiClient;
import com.nguyenlegiahuy.prm_project_v2.models.admin.agency.Agency;
import com.nguyenlegiahuy.prm_project_v2.models.admin.agency.AgencyListResponse;
import com.nguyenlegiahuy.prm_project_v2.models.admin.agency.CreateAgencyRequest;
import com.nguyenlegiahuy.prm_project_v2.models.admin.agency.CreateAgencyResponse;
import com.nguyenlegiahuy.prm_project_v2.models.admin.agency.DeleteAgencyResponse;
import com.nguyenlegiahuy.prm_project_v2.models.admin.agency.UpdateAgencyRequest;
import com.nguyenlegiahuy.prm_project_v2.models.admin.agency.UpdateAgencyResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgencyManagementFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvError;
    private MaterialButton btnCreateAgency;

    private AgencyAdapter adapter;
    private LinearLayoutManager layoutManager;

    private int currentPage = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private final int LIMIT = 5;
    private final List<Call<?>> activeCalls = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_agency_management, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        tvError = view.findViewById(R.id.tvError);
        btnCreateAgency = view.findViewById(R.id.btnCreateAgency);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AgencyAdapter(requireContext(), new ArrayList<>(), new AgencyAdapter.OnAgencyActionListener() {
            @Override
            public void onUpdate(Agency agency) {
                showUpdateDialog(agency);
            }

            @Override
            public void onDelete(Agency agency) {
                showDeleteConfirmDialog(agency);
            }
        });

        recyclerView.setAdapter(adapter);

        setupClickListeners();
        setupScrollListener();
        loadAgencies(currentPage);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        for (Call<?> call : activeCalls) {
            if (!call.isCanceled()) call.cancel();
        }
        activeCalls.clear();
    }

    private void setupClickListeners() {
        btnCreateAgency.setOnClickListener(v -> showCreateDialog());
    }


    // ============================= SCROLL FETCH DATA ============================
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
                        loadAgencies(currentPage + 1);
                    }
                }
            }
        });
    }
    //============================ LOAD DATA ============================
    private void loadAgencies(int page) {
        isLoading = true;
        if (page == 1) showLoading(true);
        else adapter.addLoadingFooter();

        AdminApiService apiService = ApiClient.getClient().create(AdminApiService.class);
        Call<AgencyListResponse> call = apiService.getAgencyList(LIMIT, page);
        activeCalls.add(call);

        call.enqueue(new Callback<AgencyListResponse>() {
            @Override
            public void onResponse(Call<AgencyListResponse> call, Response<AgencyListResponse> response) {
                if (page == 1) showLoading(false);
                else adapter.removeLoadingFooter();
                isLoading = false;
                if (response.body().getStatusCode() == 200 && response.body() != null) {
                    List<Agency> agencies = response.body().getData();
                    if (agencies.isEmpty()) {
                        isLastPage = true;
                        return;
                    }

                    if (page == 1) adapter.addAll(agencies);
                    else adapter.addAll(agencies);
                    currentPage = page;
                } else {
                   Toast.makeText(requireContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AgencyListResponse> call, Throwable t) {
                activeCalls.remove(call);
                if (page == 1) showLoading(false);
                else adapter.removeLoadingFooter();
                isLoading = false;
                showError("Error: " + t.getMessage());
            }
        });
    }

    private void showCreateDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_create_update_agency, null);
        EditText etName = dialogView.findViewById(R.id.etName);
        EditText etLocation = dialogView.findViewById(R.id.etLocation);
        EditText etAddress = dialogView.findViewById(R.id.etAddress);
        EditText etContact = dialogView.findViewById(R.id.etContactInfo);

        new AlertDialog.Builder(getContext())
                .setTitle("Create Agency")
                .setView(dialogView)
                .setPositiveButton("Create", (dialog, which) -> {
                    CreateAgencyRequest request = new CreateAgencyRequest(
                            etName.getText().toString(),
                            etLocation.getText().toString(),
                            etAddress.getText().toString(),
                            etContact.getText().toString()
                    );
                    createAgency(request);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    private void refreshAgencyfList() {
        currentPage = 1;
        isLastPage = false;
        adapter.clear();
        loadAgencies(currentPage);
    }
    private void createAgency(CreateAgencyRequest request) {
        progressBar.setVisibility(View.VISIBLE);
        AdminApiService apiService = ApiClient.getClient().create(AdminApiService.class);
        Call<CreateAgencyResponse> call = apiService.createAgency(request);
        activeCalls.add(call);

        call.enqueue(new Callback<CreateAgencyResponse>() {
            @Override
            public void onResponse(Call<CreateAgencyResponse> call, Response<CreateAgencyResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    refreshAgencyfList();
                } else {
                    Toast.makeText(getContext(), "Failed to create agency", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CreateAgencyResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                if (!call.isCanceled()) {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showUpdateDialog(Agency agency) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_create_update_agency, null);
        EditText etName = dialogView.findViewById(R.id.etName);
        EditText etLocation = dialogView.findViewById(R.id.etLocation);
        EditText etAddress = dialogView.findViewById(R.id.etAddress);
        EditText etContact = dialogView.findViewById(R.id.etContactInfo);

        etName.setText(agency.getName());
        etLocation.setText(agency.getLocation());
        etAddress.setText(agency.getAddress());
        etContact.setText(agency.getContactInfo());

        new AlertDialog.Builder(getContext())
                .setTitle("Update Agency")
                .setView(dialogView)
                .setPositiveButton("Update", (dialog, which) -> {
                    UpdateAgencyRequest request = new UpdateAgencyRequest(
                            etName.getText().toString(),
                            etLocation.getText().toString(),
                            etAddress.getText().toString(),
                            etContact.getText().toString()
                    );
                    updateAgency(agency.getId(), request);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void updateAgency(int agencyId, UpdateAgencyRequest request) {
        progressBar.setVisibility(View.VISIBLE);
        AdminApiService apiService = ApiClient.getClient().create(AdminApiService.class);
        Call<UpdateAgencyResponse> call = apiService.updateAgency(agencyId, request);
        activeCalls.add(call);

        call.enqueue(new Callback<UpdateAgencyResponse>() {
            @Override
            public void onResponse(Call<UpdateAgencyResponse> call, Response<UpdateAgencyResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    refreshAgencyfList();
                } else {
                    Toast.makeText(getContext(), "Failed to update agency", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateAgencyResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                if (!call.isCanceled()) {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showDeleteConfirmDialog(Agency agency) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Agency")
                .setMessage("Are you sure you want to delete this agency?")
                .setPositiveButton("Delete", (dialog, which) -> deleteAgency(agency.getId()))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteAgency(int agencyId) {
        progressBar.setVisibility(View.VISIBLE);
        AdminApiService apiService = ApiClient.getClient().create(AdminApiService.class);
        Call<DeleteAgencyResponse> call = apiService.deleteAgency(agencyId);
        activeCalls.add(call);

        call.enqueue(new Callback<DeleteAgencyResponse>() {
            @Override
            public void onResponse(Call<DeleteAgencyResponse> call, Response<DeleteAgencyResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    refreshAgencyfList();
                } else {
                    Toast.makeText(getContext(), "Failed to delete agency", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteAgencyResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                if (!call.isCanceled()) {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
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


}
