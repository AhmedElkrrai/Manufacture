package com.example.manufacture.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manufacture.R;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.SubscriptionHolder> {
    private List<String> mSubscriptionList = new ArrayList<>();

    @NonNull
    @Override
    public SubscriptionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubscriptionHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.subscription_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SubscriptionHolder holder, int position) {
        holder.subscription.setText(mSubscriptionList.get(position));
    }

    @Override
    public int getItemCount() {
        return mSubscriptionList.size();
    }

    public void setList(List<String> subscriptions) {
        this.mSubscriptionList = subscriptions;
        notifyDataSetChanged();
    }

    public static class SubscriptionHolder extends RecyclerView.ViewHolder {
        public Button subscription;

        public SubscriptionHolder(@NonNull View itemView) {
            super(itemView);
            subscription = itemView.findViewById(R.id.subscribed_product_TV);
        }
    }
}