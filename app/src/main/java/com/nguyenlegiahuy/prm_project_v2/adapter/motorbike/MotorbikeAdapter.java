package com.nguyenlegiahuy.prm_project_v2.adapter.motorbike;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nguyenlegiahuy.prm_project_v2.R;
import com.nguyenlegiahuy.prm_project_v2.models.dealer_staff.Motorbike;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MotorbikeAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    private final Context context;
    private final List<Motorbike> motorbikeList = new ArrayList<>();
    private boolean isLoadingAdded = false;

    public MotorbikeAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == motorbikeList.size() - 1 && isLoadingAdded)
                ? VIEW_TYPE_LOADING
                : VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_motorbike, parent, false);
            return new MotorbikeViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MotorbikeViewHolder) {
            Motorbike motorbike = motorbikeList.get(position);
            MotorbikeViewHolder vh = (MotorbikeViewHolder) holder;

            vh.tvName.setText(motorbike.getName());
            vh.tvPrice.setText(formatCurrency(motorbike.getPrice()));
            vh.tvModel.setText("Model: " + motorbike.getModel());
            vh.tvVersion.setText("Version: " + motorbike.getVersion());
            vh.tvDescription.setText(motorbike.getDescription());

            // Load ảnh đầu tiên nếu có
            if (motorbike.getImages() != null && !motorbike.getImages().isEmpty()) {
                Glide.with(context)
                        .load(motorbike.getImages().get(0).getImageUrl())
                        .placeholder(R.drawable.outline_bike_scooter_24)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(vh.ivMotorbike);
            } else {
                vh.ivMotorbike.setImageResource(R.drawable.outline_bike_scooter_24);
            }
        }
    }

    @Override
    public int getItemCount() {
        return motorbikeList.size();
    }

    public void addAll(List<Motorbike> newMotorbikes) {
        int start = motorbikeList.size();
        motorbikeList.addAll(newMotorbikes);
        notifyItemRangeInserted(start, newMotorbikes.size());
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        motorbikeList.add(new Motorbike());
        notifyItemInserted(motorbikeList.size() - 1);
    }

    public void removeLoadingFooter() {
        if (isLoadingAdded && !motorbikeList.isEmpty()) {
            isLoadingAdded = false;
            int position = motorbikeList.size() - 1;
            motorbikeList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        motorbikeList.clear();
        notifyDataSetChanged();
    }

    private String formatCurrency(double price) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(price);
    }

    static class MotorbikeViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMotorbike;
        TextView tvName, tvPrice, tvModel, tvVersion, tvDescription;
        CardView cardMotorbike;

        MotorbikeViewHolder(@NonNull View itemView) {
            super(itemView);
            cardMotorbike = itemView.findViewById(R.id.cardMotorbike);
            ivMotorbike = itemView.findViewById(R.id.ivMotorbike);
            tvName = itemView.findViewById(R.id.tvMotorbikeName);
            tvPrice = itemView.findViewById(R.id.tvMotorbikePrice);
            tvModel = itemView.findViewById(R.id.tvMotorbikeModel);
            tvVersion = itemView.findViewById(R.id.tvMotorbikeVersion);
            tvDescription = itemView.findViewById(R.id.tvMotorbikeDescription);
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.itemProgressBar);
        }
    }
}
