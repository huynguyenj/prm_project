package com.nguyenlegiahuy.prm_project_v2.models.admin.dashboard;

public class TotalMotorbikesResponse {
    private int statusCode;
    private String message;
    private Data data;

    public static class Data {
        private int totalMotorbikes;
        public int getTotalMotorbikes() { return totalMotorbikes; }
    }

    public Data getData() { return data; }
}
