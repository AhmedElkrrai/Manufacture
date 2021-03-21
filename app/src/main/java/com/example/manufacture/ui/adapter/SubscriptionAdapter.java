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
    private static List<String> mSubscriptionList = new ArrayList<>();
    private static OnItemClickListener listener;

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
        mSubscriptionList = subscriptions;
        notifyDataSetChanged();
    }

    public static class SubscriptionHolder extends RecyclerView.ViewHolder {
        public TextView subscription;

        public SubscriptionHolder(@NonNull View itemView) {
            super(itemView);
            subscription = itemView.findViewById(R.id.subscribed_product_TV);

            subscription.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION)
                        listener.onSubscriptionClicked(mSubscriptionList.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onSubscriptionClicked(String productName);
    }

    public void setOnSubscriptionClicked(OnItemClickListener listener) {
        SubscriptionAdapter.listener = listener;
    }
}