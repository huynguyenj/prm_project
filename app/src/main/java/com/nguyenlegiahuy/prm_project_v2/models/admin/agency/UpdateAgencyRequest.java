package com.nguyenlegiahuy.prm_project_v2.models.admin.agency;

public class UpdateAgencyRequest {
    private String name;
    private String location;
    private String address;
    private String contactInfo;

    public UpdateAgencyRequest(String name, String location, String address, String contactInfo) {
        this.name = name;
        this.location = location;
        this.address = address;
        this.contactInfo = contactInfo;
    }

    // Getters & Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }
}
