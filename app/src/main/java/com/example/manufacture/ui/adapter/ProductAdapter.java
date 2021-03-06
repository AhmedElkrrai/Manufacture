package com.example.manufacture.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manufacture.R;
import com.example.manufacture.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {
    private static List<Product> mProductsList = new ArrayList<>();
    private static OnItemClickListener listener;
    private static OnItemLongClickListener longListener;

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        holder.productName.setText(mProductsList.get(position).getProductName());
        if (mProductsList.get(position).isLowStock()) {
            holder.caution.setVisibility(View.VISIBLE);
        } else holder.caution.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return mProductsList.size();
    }

    public void setList(List<Product> products) {
        mProductsList = products;
        notifyDataSetChanged();
    }

    public static class ProductHolder extends RecyclerView.ViewHolder {
        public TextView productName;
        public ImageButton detailsBT, produceBT, caution;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name_item);
            detailsBT = itemView.findViewById(R.id.product_details_BT);
            produceBT = itemView.findViewById(R.id.produceBT);
            caution = itemView.findViewById(R.id.product_caution);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = ProductHolder.this.getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION)
                        listener.onItemClicked(mProductsList.get(position));
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (longListener != null) {
                    int position = ProductHolder.this.getAdapterPosition();
                    if (longListener != null && position != RecyclerView.NO_POSITION)
                        longListener.onItemLongClicked(mProductsList.get(position));
                }
                return false;
            });

            detailsBT.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION)
                        listener.onDetailsClicked(mProductsList.get(position));
                }
            });

            produceBT.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION)
                        listener.onProduceClicked(mProductsList.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(Product product);

        void onDetailsClicked(Product product);

        void onProduceClicked(Product product);
    }

    public interface OnItemLongClickListener {
        void onItemLongClicked(Product product);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        ProductAdapter.listener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener longListener) {
        ProductAdapter.longListener = longListener;
    }
}