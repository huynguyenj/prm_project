package com.nguyenlegiahuy.prm_project_v2.fragments.dealer_staff;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nguyenlegiahuy.prm_project_v2.R;
import com.nguyenlegiahuy.prm_project_v2.adapter.motorbike.MotorbikeAdapter;
import com.nguyenlegiahuy.prm_project_v2.api.AdminApiService;
import com.nguyenlegiahuy.prm_project_v2.api.ApiClient;
import com.nguyenlegiahuy.prm_project_v2.api.DealerApiService;
import com.nguyenlegiahuy.prm_project_v2.models.admin.agency.AgencyListResponse;
import com.nguyenlegiahuy.prm_project_v2.models.dealer_staff.Motorbike;
import com.nguyenlegiahuy.prm_project_v2.models.dealer_staff.MotorbikeListResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DealerHomeFragment  extends Fragment {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvError;
    private MotorbikeAdapter adapter;
    private LinearLayoutManager layoutManager;

    private int currentPage = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private static final int LIMIT = 5;

    private final List<Call<?>> activeCalls = new ArrayList<>();

    private DealerApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dealer_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recyclerMotorbikes);
        progressBar = view.findViewById(R.id.progressBar);
        tvError = view.findViewById(R.id.tvError);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MotorbikeAdapter(requireContext());
        recyclerView.setAdapter(adapter);

        apiService = ApiClient.getClient().create(DealerApiService.class);

        setupScrollListener();
        loadMotorbikes(currentPage);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        for (Call<?> call : activeCalls) {
            if (!call.isCanceled()) call.cancel();
        }
        activeCalls.clear();
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
                        loadMotorbikes(currentPage + 1);
                    }
                }
            }
        });
    }

    // ============================= LOAD DATA ============================
    private void loadMotorbikes(int page) {
        isLoading = true;

        if (page == 1) showLoading(true);
        else adapter.addLoadingFooter();

        Call<MotorbikeListResponse> call = apiService.getMotorbikes(LIMIT, page);
        activeCalls.add(call);

        call.enqueue(new Callback<MotorbikeListResponse>() {
            @Override
            public void onResponse(@NonNull Call<MotorbikeListResponse> call,
                                   @NonNull Response<MotorbikeListResponse> response) {

                if (page == 1) showLoading(false);
                else adapter.removeLoadingFooter();
                isLoading = false;

                if (response.isSuccessful() && response.body() != null
                        && response.body().getStatusCode() == 200) {

                    List<Motorbike> motorbikes = response.body().getData();

                    if (motorbikes == null || motorbikes.isEmpty()) {
                        isLastPage = true;
                        return;
                    }

                    adapter.addAll(motorbikes);
                    currentPage = page;

                } else {
                    showError("Failed to load data: " + (response.body() != null
                            ? response.body().getMessage()
                            : "Unknown error"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<MotorbikeListResponse> call, @NonNull Throwable t) {
                activeCalls.remove(call);
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
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }

}
