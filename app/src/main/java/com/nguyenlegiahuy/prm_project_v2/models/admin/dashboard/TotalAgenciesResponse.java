package com.nguyenlegiahuy.prm_project_v2.models.admin.dashboard;

public class TotalAgenciesResponse {
    private int statusCode;
    private String message;
    private Data data;

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
    public static class Data {
        private int totalAgencies;
        public int getTotalAgencies() { return totalAgencies; }
    }

    public Data getData() { return data; }
}
