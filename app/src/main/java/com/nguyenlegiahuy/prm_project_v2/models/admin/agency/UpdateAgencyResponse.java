package com.nguyenlegiahuy.prm_project_v2.models.admin.agency;

public class UpdateAgencyResponse {
    private int statusCode;
    private String message;
    private Agency data;

    // Getters & Setters
    public int getStatusCode() { return statusCode; }
    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Agency getData() { return data; }
    public void setData(Agency data) { this.data = data; }
}
