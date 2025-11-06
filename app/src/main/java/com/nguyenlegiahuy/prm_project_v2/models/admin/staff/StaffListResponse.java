package com.nguyenlegiahuy.prm_project_v2.models.admin.staff;

import java.util.List;

public class StaffListResponse {
    private int statusCode;
    private String message;
    private List<Staff> data;

    public List<Staff> getData() { return data; }
}
