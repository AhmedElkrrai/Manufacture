package com.example.manufacture.ui.dialog_fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manufacture.R;
import com.example.manufacture.databinding.ComponentDialogBinding;
import com.example.manufacture.model.Component;
import com.example.manufacture.model.Product;
import com.example.manufacture.ui.adapter.SubscriptionAdapter;
import com.example.manufacture.ui.components.ComponentsViewModel;
import com.example.manufacture.ui.home.ProductViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ComponentDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //set up
        ComponentDialogBinding binding = DataBindingUtil.inflate(inflater, R.layout.component_dialog, container, false);
        View view = binding.getRoot();

        binding.backButton.setOnClickListener(v -> Objects.requireNonNull(getDialog()).dismiss());
        HashMap<Product, Double> productAmountMap = new HashMap<>();

        // get component
        ComponentsViewModel componentsViewModel = ViewModelProviders.of(getActivity()).get(ComponentsViewModel.class);
        Component component = componentsViewModel.getComponent();

        // display component
        binding.componentNameEditText.getEditText().setText(component.getComponentName());
        binding.componentProviderNameEditText.getEditText().setText(component.getProviderName());
        binding.componentAvailableAmountEditText.getEditText().setText(String.valueOf(component.getAvailableAmount()));
        binding.componentBatchesText.getEditText().setText("-");

        //display subscriptions
        RecyclerView mRecyclerView = binding.subscriptionsRecyclerView;

        SubscriptionAdapter mAdapter = new SubscriptionAdapter();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ArrayList<Product> subscriptions = new ArrayList<>();
        mAdapter.setList(subscriptions);

        String[] subscribedProductsArr = component.getSubscribedProducts().split(":");
        ProductViewModel productViewModel = ViewModelProviders.of(getActivity()).get(ProductViewModel.class);

        for (String subID : subscribedProductsArr) {
            productViewModel.getProductById(Integer.parseInt(subID))
                    .observe(this, product -> {
                        Log.i("TAG", "sadbugs: comp_dialog");
                        if (product != null) {
                            subscriptions.add(product);
                            mAdapter.notifyDataSetChanged();

                            //fill productAmountMap
                            String[] componentsAmounts = product.getComponents().split(":");
                            int componentId = component.getId();
                            double amount;

                            for (int i = 0; i < componentsAmounts.length; i += 2) {
                                if (Integer.parseInt(componentsAmounts[i]) == componentId) {
                                    amount = Double.parseDouble(componentsAmounts[i + 1]);
                                    productAmountMap.put(product, amount);
                                    break;
                                }
                            }
                        }
                    });
        }

        //display subscription available batches
        mAdapter.setOnSubscriptionClicked(product -> {
            if (product != null) {
                double availableAmount = Double.parseDouble(component.getAvailableAmount());
                double amount = productAmountMap.get(product);
                binding.componentBatchesText.getEditText().setText(String.format("%s", getAvailableBatches(availableAmount, amount)));
            }
        });

        mRecyclerView.setAdapter(mAdapter);

        //update component
        binding.updateComponentBT.setOnClickListener(v -> {
            String componentName = binding.componentNameEditText.getEditText().getText().toString();
            String availableAmount = binding.componentAvailableAmountEditText.getEditText().getText().toString();
            String providerName = binding.componentProviderNameEditText.getEditText().getText().toString();

            component.setComponentName(componentName);
            component.setAvailableAmount(availableAmount);
            component.setProviderName(providerName);

            if (componentName.isEmpty() || availableAmount.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // check if low stock
            component.setLowStock(false);

            double available = Double.parseDouble(availableAmount);
            for (Product sub : subscriptions) {
                boolean productState = sub.isLowStock();

                double amount = productAmountMap.get(sub);
                double batchesAmount = getAvailableBatches(available, amount);
                if (batchesAmount <= 2.0) {
                    component.setLowStock(true);
                    sub.setLowStock(true);
                } else sub.setLowStock(false);

                if (productState != sub.isLowStock())
                    productViewModel.update(sub);
            }

            componentsViewModel.update(component);

            getDialog().dismiss();
        });

        //cancel component
        binding.cancelComponentBT.setOnClickListener(v -> getDialog().dismiss());

        return view;
    }

    private double getAvailableBatches(double availableAmount, double amount) {
        double batchesAmount = availableAmount / amount;
        DecimalFormat twoDForm = new DecimalFormat("#.#");
        return Double.parseDouble(twoDForm.format(batchesAmount));
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getDialog()).getWindow().setBackgroundDrawableResource(R.drawable.edit_round_2);
        int width = getResources().getDimensionPixelSize(R.dimen._329sdp);
        int height = getResources().getDimensionPixelSize(R.dimen._455sdp);
        getDialog().getWindow().setLayout(width, height);
    }
}