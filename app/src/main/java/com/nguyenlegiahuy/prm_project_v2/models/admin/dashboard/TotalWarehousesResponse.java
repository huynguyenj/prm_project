package com.nguyenlegiahuy.prm_project_v2.models.admin.dashboard;

public class TotalWarehousesResponse {
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
        private int totalWarehouses;
        public int getTotalWarehouses() { return totalWarehouses; }
    }

    public Data getData() { return data; }
}
