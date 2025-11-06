package com.nguyenlegiahuy.prm_project_v2.api;

import com.nguyenlegiahuy.prm_project_v2.models.admin.dashboard.TotalAgenciesResponse;
import com.nguyenlegiahuy.prm_project_v2.models.admin.dashboard.TotalMotorbikesResponse;
import com.nguyenlegiahuy.prm_project_v2.models.admin.dashboard.TotalWarehousesResponse;
import com.nguyenlegiahuy.prm_project_v2.models.admin.staff.StaffListResponse;
import com.nguyenlegiahuy.prm_project_v2.models.authentication.LoginRequest;
import com.nguyenlegiahuy.prm_project_v2.models.authentication.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("auth/signin")
    Call<LoginResponse> login(@Body LoginRequest body);

    //Admin api
    @GET("report/total/agencies")
    Call<TotalAgenciesResponse> getTotalAgencies();

    @GET("report/total/warehouses")
    Call<TotalWarehousesResponse> getTotalWarehouses();

    @GET("report/total/motorbikes")
    Call<TotalMotorbikesResponse> getTotalMotorbikes();

    @GET("admin/staff/list")
    Call<StaffListResponse> getStaffList(
            @Query("limit") int limit,
            @Query("page") int page
    );
}
