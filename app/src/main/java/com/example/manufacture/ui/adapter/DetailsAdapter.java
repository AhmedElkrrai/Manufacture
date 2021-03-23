package com.example.manufacture.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.manufacture.R;
import com.example.manufacture.model.Details;
import com.example.manufacture.model.Production;

import java.util.ArrayList;
import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.DetailsHolder> {
    private static List<Details> mDetailsList = new ArrayList<>();

    @NonNull
    @Override
    public DetailsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DetailsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.details_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsHolder holder, int position) {
        String amount = mDetailsList.get(position).getProductAmount();
        String availableAmount = mDetailsList.get(position).getAvailableAmount();

        holder.name.setText(mDetailsList.get(position).getComponentName());
        holder.amount.setText(amount);
        holder.availableAmount.setText(availableAmount);

        if (((Double.parseDouble(availableAmount)) / (Double.parseDouble(amount)) <= 2.0))
            holder.availableAmount.setTextColor(Color.parseColor("#ff4b4b"));
        else holder.availableAmount.setTextColor(Color.parseColor("#51dc90"));
    }

    @Override
    public int getItemCount() {
        return mDetailsList.size();
    }

    public void setList(List<Details> detailsList) {
        mDetailsList = detailsList;
        notifyDataSetChanged();
    }

    public static class DetailsHolder extends RecyclerView.ViewHolder {
        public TextView name, amount, availableAmount;

        public DetailsHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.details_component_name);
            amount = itemView.findViewById(R.id.details_component_amount);
            availableAmount = itemView.findViewById(R.id.details_component_available_amount);
        }
    }
}