package com.example.manufacture.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manufacture.R;
import com.example.manufacture.model.Component;

import java.util.ArrayList;
import java.util.List;

public class ComponentAdapter extends RecyclerView.Adapter<ComponentAdapter.ComponentHolder> implements Filterable {
    private static List<Component> mComponentsList = new ArrayList<>();
    private List<Component> mSearchListFull;
    private static OnItemClickListener listener;
    private static OnItemLongClickListener longListener;
    public Context context;

    @NonNull
    @Override
    public ComponentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ComponentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.component_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ComponentHolder holder, int position) {
        context = holder.itemView.getContext();
        holder.component_name.setText(mComponentsList.get(position).getComponentName());
        holder.available_amount.setText(mComponentsList.get(position).getAvailableAmount());

        if (!(mComponentsList.get(position).getProviderName().equals("Provider")))
            holder.provider_name.setText(mComponentsList.get(position).getProviderName());

        if (mComponentsList.get(position).isLowStock()) {
            holder.caution.setVisibility(View.VISIBLE);
        } else holder.caution.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return mComponentsList.size();
    }

    public void setList(List<Component> components) {
        ComponentAdapter.mComponentsList = components;
        mSearchListFull = new ArrayList<>(components);
        notifyDataSetChanged();
    }

    public static class ComponentHolder extends RecyclerView.ViewHolder {
        public TextView component_name, available_amount, provider_name;
        public ImageButton caution;

        public ComponentHolder(@NonNull View itemView) {
            super(itemView);
            component_name = itemView.findViewById(R.id.component_name_item);
            available_amount = itemView.findViewById(R.id.component_available_amount_item);
            provider_name = itemView.findViewById(R.id.provider_name_item);
            caution = itemView.findViewById(R.id.component_caution);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION)
                        listener.onItemClicked(mComponentsList.get(position));
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (longListener != null) {
                    int position = ComponentHolder.this.getAdapterPosition();
                    if (longListener != null && position != RecyclerView.NO_POSITION)
                        longListener.onItemLongClicked(mComponentsList.get(position));
                }
                return false;
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(Component component);
    }

    public interface OnItemLongClickListener {
        void onItemLongClicked(Component component);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        ComponentAdapter.listener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener longListener) {
        ComponentAdapter.longListener = longListener;
    }

    private void showMessage() {
        Toast.makeText(context, "Material Does Not Exist", Toast.LENGTH_SHORT).show();
    }

    @Override
    public Filter getFilter() {
        return searchFilter;
    }

    private final Filter searchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Component> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0)
                filteredList.addAll(mSearchListFull);
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Component customer : mSearchListFull) {
                    if (customer.getComponentName().toLowerCase().contains(filterPattern))
                        filteredList.add(customer);
                }
            }
            FilterResults results = new FilterResults();

            if (filteredList.isEmpty()) {
                results.values = mSearchListFull;
                showMessage();
            } else results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mComponentsList.clear();
            mComponentsList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}