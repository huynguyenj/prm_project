package com.nguyenlegiahuy.prm_project_v2.adapter.role;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.nguyenlegiahuy.prm_project_v2.R;
import com.nguyenlegiahuy.prm_project_v2.models.admin.staff.Role;

import java.util.ArrayList;
import java.util.List;

public class RoleAdapter extends RecyclerView.Adapter<RoleAdapter.RoleViewHolder> {

    private List<Role> roleList;

    public RoleAdapter() {
        this.roleList = new ArrayList<>();
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
        notifyDataSetChanged();
    }

    public List<Integer> getSelectedRoleIds() {
        List<Integer> selectedIds = new ArrayList<>();
        for (Role role : roleList) {
            if (role.isSelected()) {
                selectedIds.add(role.getId());
            }
        }
        return selectedIds;
    }

    @NonNull
    @Override
    public RoleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_role_checkbox, parent, false);
        return new RoleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoleViewHolder holder, int position) {
        Role role = roleList.get(position);
        holder.bind(role);
    }

    @Override
    public int getItemCount() {
        return roleList.size();
    }

    static class RoleViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkboxRole;

        public RoleViewHolder(@NonNull View itemView) {
            super(itemView);
            checkboxRole = itemView.findViewById(R.id.checkboxRole);
        }

        public void bind(Role role) {
            checkboxRole.setText(role.getRoleName());
            checkboxRole.setChecked(role.isSelected());

            checkboxRole.setOnCheckedChangeListener((buttonView, isChecked) -> {
                role.setSelected(isChecked);
            });
        }
    }
}