package com.nguyenlegiahuy.prm_project_v2.models.admin.agency;

import java.util.List;

public class AgencyListResponse {
    private int statusCode;
    private String message;
    private List<Agency> data;
    private PaginationInfo paginationInfo;

    public int getStatusCode() { return statusCode; }
    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<Agency> getData() { return data; }
    public void setData(List<Agency> data) { this.data = data; }

    public PaginationInfo getPaginationInfo() { return paginationInfo; }
    public void setPaginationInfo(PaginationInfo paginationInfo) { this.paginationInfo = paginationInfo; }

    public static class PaginationInfo {
        private int page;
        private int limit;
        private int totalItems;

        public int getPage() { return page; }
        public void setPage(int page) { this.page = page; }

        public int getLimit() { return limit; }
        public void setLimit(int limit) { this.limit = limit; }

        public int getTotalItems() { return totalItems; }
        public void setTotalItems(int totalItems) { this.totalItems = totalItems; }
    }
}
