package com.nguyenlegiahuy.prm_project_v2.models.admin.staff;

public class Role {
    private int id;

    private String roleName;

    private boolean isActive;

    private boolean isDeleted;

    private boolean isSelected; // For UI selection

    public int getId() {
        return id;
    }

    public String getRoleName() {
        return roleName;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
