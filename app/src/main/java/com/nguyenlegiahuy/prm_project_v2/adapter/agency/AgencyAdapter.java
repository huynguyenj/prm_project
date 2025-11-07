package com.nguyenlegiahuy.prm_project_v2.adapter.agency;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nguyenlegiahuy.prm_project_v2.R;
import com.nguyenlegiahuy.prm_project_v2.models.admin.agency.Agency;

import java.util.List;

public class AgencyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    private final Context context;
    private final List<Agency> agencyList;
    private boolean isLoadingAdded = false;
    private final OnAgencyActionListener listener;

    public interface OnAgencyActionListener {
        void onUpdate(Agency agency);
        void onDelete(Agency agency);
    }

    public AgencyAdapter(Context context, List<Agency> agencyList, OnAgencyActionListener listener) {
        this.context = context;
        this.agencyList = agencyList;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == agencyList.size() - 1 && isLoadingAdded) ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_agency, parent, false);
            return new AgencyViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AgencyViewHolder) {
            Agency agency = agencyList.get(position);
            AgencyViewHolder viewHolder = (AgencyViewHolder) holder;

            // Bảo vệ null: nếu null thì hiện chuỗi rỗng
            viewHolder.tvName.setText(agency.getName() != null ? agency.getName() : "");
            viewHolder.tvLocation.setText(agency.getLocation() != null ? agency.getLocation() : "");
            viewHolder.tvAddress.setText(agency.getAddress() != null ? agency.getAddress() : "");
            viewHolder.tvContactInfo.setText(agency.getContactInfo() != null ? agency.getContactInfo() : "");
            viewHolder.tvStatus.setText(agency.getStatus());
            // Bảo vệ null cho status
            String status = agency.getStatus() != null ? agency.getStatus() : "";
            viewHolder.tvStatus.setText(status);
            if ("active".equalsIgnoreCase(status)) {
                viewHolder.tvStatus.setTextColor(context.getColor(R.color.success));
            } else {
                viewHolder.tvStatus.setTextColor(context.getColor(R.color.error));
            }

            viewHolder.btnUpdate.setOnClickListener(v -> listener.onUpdate(agency));
            viewHolder.btnDelete.setOnClickListener(v -> listener.onDelete(agency));
        }
    }

    @Override
    public int getItemCount() {
        return agencyList.size();
    }

    public void addAll(List<Agency> newAgencies) {
        int start = agencyList.size();
        agencyList.addAll(newAgencies);
        notifyItemRangeInserted(start, newAgencies.size());
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        agencyList.add(new Agency());
        notifyItemInserted(agencyList.size() - 1);
    }

    public void removeLoadingFooter() {
        if (isLoadingAdded && !agencyList.isEmpty()) {
            isLoadingAdded = false;
            int position = agencyList.size() - 1;
            agencyList.remove(position);
            notifyItemRemoved(position);
        }
    }

    static class AgencyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvLocation, tvAddress, tvContactInfo, tvStatus;
        Button btnUpdate, btnDelete;

        public AgencyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvContactInfo = itemView.findViewById(R.id.tvContactInfo);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnUpdate = itemView.findViewById(R.id.btnUpdateAgency);
            btnDelete = itemView.findViewById(R.id.btnDeleteAgency);
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
        agencyList.clear();
        notifyDataSetChanged();
    }
}
