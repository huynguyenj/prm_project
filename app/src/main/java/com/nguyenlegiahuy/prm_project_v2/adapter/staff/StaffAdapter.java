package com.nguyenlegiahuy.prm_project_v2.adapter.staff;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nguyenlegiahuy.prm_project_v2.R;
import com.nguyenlegiahuy.prm_project_v2.models.admin.staff.Staff;

import java.util.List;

public class StaffAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    private final Context context;
    private final List<Staff> staffList;
    private boolean isLoadingAdded = false;
    private final OnStaffActionListener listener;


    public interface OnStaffActionListener {
        void onUpdate(Staff staff);
        void onDelete(Staff staff);
    }

    public StaffAdapter(Context context, List<Staff> staffList, OnStaffActionListener listener) {
        this.context = context;
        this.staffList = staffList;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == staffList.size() - 1 && isLoadingAdded) ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_staff, parent, false);
            return new StaffViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof StaffViewHolder) {
            Staff staff = staffList.get(position);
            StaffViewHolder viewHolder = (StaffViewHolder) holder;

            viewHolder.tvName.setText(staff.getFullname() + " (" + staff.getUsername() + ")");
            viewHolder.tvEmail.setText(staff.getEmail());
            viewHolder.tvPhone.setText(staff.getPhone());
            viewHolder.tvStatus.setText(staff.isActive() ? "Active" : "Inactive");
            viewHolder.tvStatus.setTextColor(
                    staff.isActive() ? context.getColor(R.color.success) : context.getColor(R.color.error)
            );

            viewHolder.btnUpdate.setOnClickListener(v -> listener.onUpdate(staff));
            viewHolder.btnDelete.setOnClickListener(v -> listener.onDelete(staff));
        }
    }

    @Override
    public int getItemCount() {
        return staffList.size();
    }

    // === Pagination helpers ===
    public void addAll(List<Staff> newStaffs) {
        int start = staffList.size();
        staffList.addAll(newStaffs);
        notifyItemRangeInserted(start, newStaffs.size());
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        staffList.add(new Staff()); // dummy
        notifyItemInserted(staffList.size() - 1);
    }

    public void removeLoadingFooter() {
        if (isLoadingAdded && !staffList.isEmpty()) {
            isLoadingAdded = false;
            int position = staffList.size() - 1;
            staffList.remove(position);
            notifyItemRemoved(position);
        }
    }

    static class StaffViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvName, tvEmail, tvPhone, tvRole, tvStatus;
        Button btnUpdate, btnDelete;
        StaffViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            tvName = itemView.findViewById(R.id.tvName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnUpdate = itemView.findViewById(R.id.btnUpdateStaff);
            btnDelete = itemView.findViewById(R.id.btnDeleteStaff);
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.itemProgressBar);
        }
    }
    public void clear() {
        staffList.clear();
        notifyDataSetChanged();
    }
}
