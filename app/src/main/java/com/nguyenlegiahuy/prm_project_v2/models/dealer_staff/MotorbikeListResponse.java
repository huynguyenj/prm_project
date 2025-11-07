package com.nguyenlegiahuy.prm_project_v2.models.dealer_staff;

import java.util.List;

public class MotorbikeListResponse {
    private int statusCode;

    private String message;

    private List<Motorbike> data;

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public List<Motorbike> getData() {
        return data;
    }
}
