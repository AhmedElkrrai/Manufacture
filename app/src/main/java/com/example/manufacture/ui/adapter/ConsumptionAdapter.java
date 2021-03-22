package com.example.manufacture.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.manufacture.R;
import com.example.manufacture.model.Consumption;

import java.util.ArrayList;
import java.util.List;

public class ConsumptionAdapter extends RecyclerView.Adapter<ConsumptionAdapter.ConsumptionHolder> {
    private List<Consumption> mConsumptionList = new ArrayList<>();

    @NonNull
    @Override
    public ConsumptionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConsumptionHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.consumption_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ConsumptionHolder holder, int position) {
        holder.material.setText(mConsumptionList.get(position).getMaterialName());
        holder.available.setText(mConsumptionList.get(position).getAvailableAmount());
        holder.then.setText(mConsumptionList.get(position).getPostProductionAmount());
        holder.batches.setText(mConsumptionList.get(position).getAvailableBatches());
        if (Double.parseDouble(mConsumptionList.get(position).getAvailableBatches()) <= 2.0)
            holder.batches.setTextColor(Color.parseColor("#ff4b4b"));
    }

    @Override
    public int getItemCount() {
        return mConsumptionList.size();
    }

    public void setList(List<Consumption> consumptions) {
        this.mConsumptionList = consumptions;
        notifyDataSetChanged();
    }

    public static class ConsumptionHolder extends RecyclerView.ViewHolder {
        public TextView material, available, then, batches;

        public ConsumptionHolder(@NonNull View itemView) {
            super(itemView);
            material = itemView.findViewById(R.id.consumption_material);
            available = itemView.findViewById(R.id.consumption_available);
            then = itemView.findViewById(R.id.consumption_then);
            batches = itemView.findViewById(R.id.consumption_batches);
        }
    }
}