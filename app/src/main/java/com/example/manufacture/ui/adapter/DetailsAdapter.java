package com.example.manufacture.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.manufacture.R;
import com.example.manufacture.model.Details;

import java.util.ArrayList;
import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.DetailsHolder> {
    private List<Details> mDetailsList = new ArrayList<>();

    @NonNull
    @Override
    public DetailsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DetailsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.details_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsHolder holder, int position) {
        holder.name.setText(mDetailsList.get(position).getComponentName());
        holder.amount.setText(mDetailsList.get(position).getProductAmount());
        holder.minAmount.setText(mDetailsList.get(position).getMinAmount());
        holder.availableAmount.setText(mDetailsList.get(position).getAvailableAmount());
    }

    @Override
    public int getItemCount() {
        return mDetailsList.size();
    }

    public void setList(List<Details> detailsList) {
        this.mDetailsList = detailsList;
        notifyDataSetChanged();
    }

    public static class DetailsHolder extends RecyclerView.ViewHolder {
        public TextView name, amount, minAmount, availableAmount;

        public DetailsHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.details_component_name);
            amount = itemView.findViewById(R.id.details_component_amount);
            minAmount = itemView.findViewById(R.id.details_component_min_amount);
            availableAmount = itemView.findViewById(R.id.details_component_available_amount);
        }
    }
}