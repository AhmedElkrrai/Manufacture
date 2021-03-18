package com.example.manufacture.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manufacture.R;
import com.example.manufacture.model.ComponentBatch;

import java.util.ArrayList;
import java.util.List;

public class BatchAdapter extends RecyclerView.Adapter<BatchAdapter.BatchHolder> {
    private List<ComponentBatch> mComponentBatchesList = new ArrayList<>();

    @NonNull
    @Override
    public BatchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BatchHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.batch_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BatchHolder holder, int position) {
        holder.componentName.setText(mComponentBatchesList.get(position).getComponentName());
        holder.componentBatches.setText(mComponentBatchesList.get(position).getBatchesAmount());
        holder.componentAmount.setText(mComponentBatchesList.get(position).getComponentAmount());
    }

    @Override
    public int getItemCount() {
        return mComponentBatchesList.size();
    }

    public void setList(List<ComponentBatch> moviesList) {
        this.mComponentBatchesList = moviesList;
        notifyDataSetChanged();
    }

    public static class BatchHolder extends RecyclerView.ViewHolder {
        public TextView componentName, componentBatches,componentAmount;
        public BatchHolder(@NonNull View itemView) {
            super(itemView);
            componentName = itemView.findViewById(R.id.view_production_component_name);
            componentBatches = itemView.findViewById(R.id.view_production_component_batches);
            componentAmount = itemView.findViewById(R.id.view_production_component_amount);
        }
    }

}