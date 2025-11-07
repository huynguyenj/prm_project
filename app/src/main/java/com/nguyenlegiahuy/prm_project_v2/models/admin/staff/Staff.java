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

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getFullname() { return fullname; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getAvatar() { return avatar; }
    public boolean isActive() { return isActive; }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
