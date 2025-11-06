package com.nguyenlegiahuy.prm_project_v2.models.admin.dashboard;

public class TotalWarehousesResponse {
    private int statusCode;
    private String message;
    private Data data;

    public static class Data {
        private int totalWarehouses;
        public int getTotalWarehouses() { return totalWarehouses; }
    }

    public Data getData() { return data; }
}
