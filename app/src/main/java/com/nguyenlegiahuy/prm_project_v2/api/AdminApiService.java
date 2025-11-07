package com.nguyenlegiahuy.prm_project_v2.api;

import com.nguyenlegiahuy.prm_project_v2.models.admin.agency.AgencyListResponse;
import com.nguyenlegiahuy.prm_project_v2.models.admin.agency.CreateAgencyRequest;
import com.nguyenlegiahuy.prm_project_v2.models.admin.agency.CreateAgencyResponse;
import com.nguyenlegiahuy.prm_project_v2.models.admin.agency.DeleteAgencyResponse;
import com.nguyenlegiahuy.prm_project_v2.models.admin.agency.UpdateAgencyRequest;
import com.nguyenlegiahuy.prm_project_v2.models.admin.agency.UpdateAgencyResponse;
import com.nguyenlegiahuy.prm_project_v2.models.admin.dashboard.TotalAgenciesResponse;
import com.nguyenlegiahuy.prm_project_v2.models.admin.dashboard.TotalMotorbikesResponse;
import com.nguyenlegiahuy.prm_project_v2.models.admin.dashboard.TotalWarehousesResponse;
import com.nguyenlegiahuy.prm_project_v2.models.admin.staff.CreateStaffRequest;
import com.nguyenlegiahuy.prm_project_v2.models.admin.staff.CreateStaffResponse;
import com.nguyenlegiahuy.prm_project_v2.models.admin.staff.DeleteStaffResponse;
import com.nguyenlegiahuy.prm_project_v2.models.admin.staff.RoleResponse;
import com.nguyenlegiahuy.prm_project_v2.models.admin.staff.Staff;
import com.nguyenlegiahuy.prm_project_v2.models.admin.staff.StaffListResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AdminApiService {
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

    @POST("admin/staff")
    Call<CreateStaffResponse> createStaff(@Body CreateStaffRequest request);

    @GET("role/all-role")
    Call<RoleResponse> getAllRoles();

    @PATCH("/admin/staff/{staffId}")
    Call<CreateStaffResponse> updateStaff(@Path("staffId") int staffId, @Body Staff staff);

    @DELETE("/admin/staff/{staffId}")
    Call<DeleteStaffResponse> deleteStaff(@Path("staffId") int staffId);

    // ================= Agency =================
    @POST("agency")
    Call<CreateAgencyResponse> createAgency(@Body CreateAgencyRequest request);

    @GET("agency/list")
    Call<AgencyListResponse> getAgencyList(
            @Query("limit") int limit,
            @Query("page") int page
    );

    @PATCH("agency/{agencyId}")
    Call<UpdateAgencyResponse> updateAgency(
            @Path("agencyId") int agencyId,
            @Body UpdateAgencyRequest request
    );

    @DELETE("agency/{agencyId}")
    Call<DeleteAgencyResponse> deleteAgency(@Path("agencyId") int agencyId);
}
