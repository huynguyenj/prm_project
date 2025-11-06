package com.nguyenlegiahuy.prm_project_v2.models.admin.staff;

import java.util.List;

public class Staff {
    private int id;
    private String username;
    private String fullname;
    private String email;
    private String phone;
    private String address;
    private String avatar;
    private String createAt;
    private boolean isActive;
    private boolean isDeleted;
    private int agencyId;
    private List<String> role;

    // Getters & Setters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getFullname() { return fullname; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getAvatar() { return avatar; }
    public boolean isActive() { return isActive; }
}
