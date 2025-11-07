package com.nguyenlegiahuy.prm_project_v2.models.admin.agency;

public class DeleteAgencyResponse {
    private int statusCode;
    private String message;
    private Object data;

    public int getStatusCode() { return statusCode; }
    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
}
