package com.example.manufacture.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.manufacture.R;
import com.example.manufacture.model.Product;
import com.example.manufacture.model.Production;

import java.util.ArrayList;
import java.util.List;

public class ProductionAdapter extends RecyclerView.Adapter<ProductionAdapter.ProductionHolder> {
    private static List<Production> mProductionList = new ArrayList<>();
    private static OnItemClickListener listener;

    @NonNull
    @Override
    public ProductionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductionHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.production_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductionHolder holder, int position) {
        holder.patchNumber.setText(mProductionList.get(position).getPatchNumber());
        holder.productName.setText(mProductionList.get(position).getProductName());
        holder.productionDate.setText(mProductionList.get(position).getProductionEnrolmentDate());
    }

    @Override
    public int getItemCount() {
        return mProductionList.size();
    }

    public void setList(List<Production> productions) {
        mProductionList = productions;
        notifyDataSetChanged();
    }

    public static class ProductionHolder extends RecyclerView.ViewHolder {
        public TextView patchNumber, productName, productionDate;

        public ProductionHolder(@NonNull View itemView) {
            super(itemView);
            patchNumber = itemView.findViewById(R.id.production_item_patch_number);
            productName = itemView.findViewById(R.id.production_item_product_name);
            productionDate = itemView.findViewById(R.id.production_item_production_date);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION)
                        listener.onItemClicked(mProductionList.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(Production production);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        ProductionAdapter.listener = listener;
    }
}