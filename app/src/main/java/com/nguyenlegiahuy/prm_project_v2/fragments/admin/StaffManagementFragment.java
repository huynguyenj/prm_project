package com.nguyenlegiahuy.prm_project_v2.fragments.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nguyenlegiahuy.prm_project_v2.R;
import com.nguyenlegiahuy.prm_project_v2.adapter.staff.StaffAdapter;
import com.nguyenlegiahuy.prm_project_v2.api.AdminApiService;
import com.nguyenlegiahuy.prm_project_v2.api.ApiClient;
import com.nguyenlegiahuy.prm_project_v2.api.ApiService;
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

    private StaffAdapter adapter;
    private LinearLayoutManager layoutManager;

    private int currentPage = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private final int LIMIT = 8;

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

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StaffAdapter(requireContext(), new ArrayList<>());
        recyclerView.setAdapter(adapter);

        setupScrollListener();
        loadStaffs(currentPage);
    }

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

                if (response.isSuccessful() && response.body() != null) {
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
                    showError("Failed to load staff list");
                }
            }

            @Override
            public void onFailure(@NonNull Call<StaffListResponse> call, @NonNull Throwable t) {
                if (page == 1) showLoading(false);
                else adapter.removeLoadingFooter();
                isLoading = false;
                showError("Error: " + t.getMessage());
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
