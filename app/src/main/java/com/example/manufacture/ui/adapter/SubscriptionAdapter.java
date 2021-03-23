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
import com.example.manufacture.model.Subscription;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.SubscriptionHolder> {
    private static List<Subscription> mSubscriptionList = new ArrayList<>();
    private static OnItemClickListener listener;

    @NonNull
    @Override
    public SubscriptionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubscriptionHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.subscription_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SubscriptionHolder holder, int position) {
        holder.subscription.setText(mSubscriptionList.get(position).getProduct().getProductName());
        if (mSubscriptionList.get(position).isLowStock())
            holder.caution.setVisibility(View.VISIBLE);
        else holder.caution.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return mSubscriptionList.size();
    }

    public void setList(List<Subscription> subscriptions) {
        mSubscriptionList = subscriptions;
        notifyDataSetChanged();
    }

    public static class SubscriptionHolder extends RecyclerView.ViewHolder {
        public TextView subscription;
        public ImageButton caution;

        public SubscriptionHolder(@NonNull View itemView) {
            super(itemView);
            subscription = itemView.findViewById(R.id.subscribed_product_TV);
            caution = itemView.findViewById(R.id.subscription_caution);

            subscription.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION)
                        listener.onSubscriptionClicked(mSubscriptionList.get(position).getProduct());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onSubscriptionClicked(Product product);
    }

    public void setOnSubscriptionClicked(OnItemClickListener listener) {
        SubscriptionAdapter.listener = listener;
    }
}