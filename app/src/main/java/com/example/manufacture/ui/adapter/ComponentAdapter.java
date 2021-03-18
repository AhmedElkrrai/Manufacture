package com.example.manufacture.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manufacture.R;
import com.example.manufacture.model.Component;

import java.util.ArrayList;
import java.util.List;

public class ComponentAdapter extends RecyclerView.Adapter<ComponentAdapter.ComponentHolder> {
    private static List<Component> components = new ArrayList<>();
    private static OnItemClickListener listener;

    @NonNull
    @Override
    public ComponentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ComponentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.component_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ComponentHolder holder, int position) {
        holder.component_name.setText(components.get(position).getComponentName());
        holder.available_amount.setText(components.get(position).getAvailableAmount());
        if (!(components.get(position).getProviderName().equals("Provider")))
            holder.provider_name.setText(components.get(position).getProviderName());
    }

    @Override
    public int getItemCount() {
        return components.size();
    }

    public void setList(List<Component> components) {
        ComponentAdapter.components = components;
        notifyDataSetChanged();
    }

    public static class ComponentHolder extends RecyclerView.ViewHolder {
        public TextView component_name, available_amount, provider_name;

        public ComponentHolder(@NonNull View itemView) {
            super(itemView);
            component_name = itemView.findViewById(R.id.component_name_item);
            available_amount = itemView.findViewById(R.id.component_available_amount_item);
            provider_name = itemView.findViewById(R.id.provider_name_item);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION)
                        listener.onItemClicked(components.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(Component component);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        ComponentAdapter.listener = listener;
    }
}