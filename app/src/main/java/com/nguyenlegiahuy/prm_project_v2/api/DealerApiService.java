package com.nguyenlegiahuy.prm_project_v2.api;
import com.nguyenlegiahuy.prm_project_v2.models.dealer_staff.MotorbikeListResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
public interface DealerApiService {
    @GET("motorbike")
    Call<MotorbikeListResponse> getMotorbikes(
            @Query("limit") int limit,
            @Query("page") int page
    );
}

