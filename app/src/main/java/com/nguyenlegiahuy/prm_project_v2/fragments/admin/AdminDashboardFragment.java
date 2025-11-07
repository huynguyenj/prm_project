package com.nguyenlegiahuy.prm_project_v2.fragments.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nguyenlegiahuy.prm_project_v2.R;
import com.nguyenlegiahuy.prm_project_v2.api.AdminApiService;
import com.nguyenlegiahuy.prm_project_v2.api.ApiClient;
import com.nguyenlegiahuy.prm_project_v2.models.admin.dashboard.TotalAgenciesResponse;
import com.nguyenlegiahuy.prm_project_v2.models.admin.dashboard.TotalMotorbikesResponse;
import com.nguyenlegiahuy.prm_project_v2.models.admin.dashboard.TotalWarehousesResponse;
import com.nguyenlegiahuy.prm_project_v2.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDashboardFragment extends Fragment {

    private LinearLayout cardContainer;
    private ProgressBar progressBar;
    private TextView tvError;
    private AdminApiService apiService;
    private SessionManager sessionManager;

    private int pendingRequests = 0;
    private final List<Call<?>> activeCalls = new ArrayList<>(); // lưu các request để cancel

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cardContainer = view.findViewById(R.id.cardContainer);
        progressBar = view.findViewById(R.id.progressBar);
        tvError = view.findViewById(R.id.tvError);

        apiService = ApiClient.getClient().create(AdminApiService.class);
        sessionManager = new SessionManager(requireContext());

        loadAllData();
    }

    private void loadAllData() {
        showLoading(true);
        pendingRequests = 3;

        loadTotalAgencies();
        loadTotalWarehouses();
        loadTotalMotorbikes();
    }

    private void requestCompleted() {
        if (--pendingRequests <= 0 && isAdded()) {
            showLoading(false);
        }
    }

    // ============================ LOAD DATA ============================

    private void loadTotalAgencies() {
        apiService.getTotalAgencies().enqueue(new Callback<TotalAgenciesResponse>() {
            @Override
            public void onResponse(Call<TotalAgenciesResponse> call, Response<TotalAgenciesResponse> response) {
                if (!isAdded()) return;
                if (response.body().getStatusCode() == 200 && response.body().getData() != null) {
                    int count = response.body().getData().getTotalAgencies();
                    addStatCard("Total Agencies", String.valueOf(count), R.drawable.outline_assignment_globe_24);
                } else {
                    Toast.makeText(requireContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                requestCompleted();
            }

            @Override
            public void onFailure(Call<TotalAgenciesResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Agencies error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                requestCompleted();
            }
        });
    }


    private void loadTotalWarehouses() {
        apiService.getTotalWarehouses().enqueue(new Callback<TotalWarehousesResponse>() {
            @Override
            public void onResponse(Call<TotalWarehousesResponse> call, Response<TotalWarehousesResponse> response) {
                if (!isAdded()) return;
                if (response.body().getData() != null) {
                    int count = response.body().getData().getTotalWarehouses();
                    addStatCard("Total Warehouses", String.valueOf(count), R.drawable.baseline_add_location_24);
                } else {
                    Toast.makeText(requireContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                requestCompleted();
            }

            @Override
            public void onFailure(Call<TotalWarehousesResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Warehouses error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                requestCompleted();
            }
        });
    }


    private void loadTotalMotorbikes() {
        apiService.getTotalMotorbikes().enqueue(new Callback<TotalMotorbikesResponse>() {
            @Override
            public void onResponse(Call<TotalMotorbikesResponse> call, Response<TotalMotorbikesResponse> response) {
                if (!isAdded()) return;
                if (response.body().getData() != null) {
                    int count = response.body().getData().getTotalMotorbikes();
                    addStatCard("Total Motorbikes", String.valueOf(count), R.drawable.outline_bike_scooter_24);
                } else {
                    Toast.makeText(requireContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                requestCompleted();
            }

            @Override
            public void onFailure(Call<TotalMotorbikesResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Motorbikes error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                requestCompleted();
            }
        });
    }

    // ============================ UI HELPERS ============================

    private void addStatCard(String title, String value, int iconRes) {
        if (!isAdded()) return;
        View card = LayoutInflater.from(requireContext()).inflate(R.layout.item_dashboard_stat, cardContainer, false);
        TextView tvTitle = card.findViewById(R.id.tvTitle);
        TextView tvValue = card.findViewById(R.id.tvValue);
        ImageView ivIcon = card.findViewById(R.id.ivIcon);

        tvTitle.setText(title);
        tvValue.setText(value);
        ivIcon.setImageResource(iconRes);

        cardContainer.addView(card);
    }


    private void showLoading(boolean show) {
        if (!isAdded()) return;
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        cardContainer.setVisibility(show ? View.GONE : View.VISIBLE);
        tvError.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Hủy toàn bộ request đang chạy khi fragment bị hủy
        for (Call<?> call : activeCalls) {
            if (!call.isCanceled()) call.cancel();
        }
        activeCalls.clear();
    }
}
