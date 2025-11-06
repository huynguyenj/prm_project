package com.nguyenlegiahuy.prm_project_v2.models.authentication;

public class LoginResponse {
    private int statusCode;
    private String message;
    private Data data;

    public int getStatusCode() { return statusCode; }
    public String getMessage() { return message; }
    public Data getData() { return data; }

    public class Data {
        private String accessToken;
        private int userId;
        private int agencyId;
        private String[] role;

        public String getAccessToken() { return accessToken; }
        public int getUserId() { return userId; }
        public int getAgencyId() { return agencyId; }
        public String[] getRole() { return role; }
    }
}
