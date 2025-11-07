package com.nguyenlegiahuy.prm_project_v2.models.admin.staff;

import java.util.List;

public class RoleResponse {
    private int statusCode;

    private String message;

    private List<Role> data;

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public List<Role> getData() {
        return data;
    }
}
