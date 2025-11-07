package com.nguyenlegiahuy.prm_project_v2.models.admin.agency;

public class Agency {
    private int id;
    private String name;
    private String location;
    private String address;
    private String contactInfo;
    private String status;

    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
