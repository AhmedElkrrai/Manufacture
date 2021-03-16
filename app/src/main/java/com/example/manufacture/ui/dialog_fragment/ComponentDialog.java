package com.example.manufacture.ui.dialog_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manufacture.R;
import com.example.manufacture.databinding.ComponentDialogBinding;
import com.example.manufacture.model.Component;
import com.example.manufacture.model.Product;
import com.example.manufacture.ui.adapter.SubscriptionAdapter;
import com.example.manufacture.ui.components.ComponentsViewModel;
import com.example.manufacture.ui.home.ProductViewModel;

import java.util.ArrayList;
import java.util.Objects;

public class ComponentDialog extends DialogFragment {
    private ComponentsViewModel componentsViewModel;
    private ProductViewModel productViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ComponentDialogBinding binding = DataBindingUtil.inflate(inflater, R.layout.component_dialog, container, false);
        View view = binding.getRoot();

        binding.backButton.setOnClickListener(v -> Objects.requireNonNull(getDialog()).dismiss());

        // get component
        componentsViewModel = ViewModelProviders.of(getActivity()).get(ComponentsViewModel.class);
        Component component = componentsViewModel.getComponent();

        // display component
        binding.componentNameEditText.getEditText().setText(component.getComponentName());
        binding.componentProviderNameEditText.getEditText().setText(component.getProviderName());
        binding.componentAvailableAmountEditText.getEditText().setText(String.valueOf(component.getAvailableAmount()));
        binding.componentMinAmountEditText.getEditText().setText(String.valueOf(component.getMinAmount()));

        //display subscriptions
        RecyclerView mRecyclerView = binding.subscriptionsRecyclerView;

        SubscriptionAdapter mAdapter = new SubscriptionAdapter();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ArrayList<String> subscriptions = new ArrayList<>();
        mAdapter.setList(subscriptions);

        String[] subscribedProductsArr = component.getSubscribedProducts().split(":");
        productViewModel = ViewModelProviders.of(getActivity()).get(ProductViewModel.class);

        for (String s : subscribedProductsArr) {
            productViewModel.getProductById(Integer.parseInt(s))
                    .observe(getActivity(), product -> {
                        subscriptions.add(product.getProductName());
                        mAdapter.notifyDataSetChanged();
                    });
        }

        mRecyclerView.setAdapter(mAdapter);


        //update component
        binding.updateComponentBT.setOnClickListener(v -> {
            String componentName = binding.componentNameEditText.getEditText().getText().toString();
            String availableAmount = binding.componentAvailableAmountEditText.getEditText().getText().toString();
            String minAmount = binding.componentMinAmountEditText.getEditText().getText().toString();
            String providerName = binding.componentProviderNameEditText.getEditText().getText().toString();

            component.setComponentName(componentName);
            component.setAvailableAmount(availableAmount);
            component.setMinAmount(minAmount);
            component.setProviderName(providerName);

            componentsViewModel.update(component);

            Toast.makeText(getActivity(), "Component Updated", Toast.LENGTH_SHORT).show();
            getDialog().dismiss();
        });

        //delete component
        binding.deleteComponentBT.setOnClickListener(v -> {
            componentsViewModel.delete(component);
            Toast.makeText(getActivity(), "Component Deleted", Toast.LENGTH_SHORT).show();
            getDialog().dismiss();
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getDialog()).getWindow().setBackgroundDrawableResource(R.drawable.edit_round_2);
        int width = getResources().getDimensionPixelSize(R.dimen._329sdp);
        int height = getResources().getDimensionPixelSize(R.dimen._555sdp);
        getDialog().getWindow().setLayout(width, height);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        componentsViewModel = ViewModelProviders.of(getActivity()).get(ComponentsViewModel.class);
        productViewModel = ViewModelProviders.of(getActivity()).get(ProductViewModel.class);
    }
}