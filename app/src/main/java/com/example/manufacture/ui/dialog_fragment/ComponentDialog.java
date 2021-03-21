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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manufacture.R;
import com.example.manufacture.databinding.ComponentDialogBinding;
import com.example.manufacture.model.Component;
import com.example.manufacture.ui.adapter.SubscriptionAdapter;
import com.example.manufacture.ui.components.ComponentsViewModel;
import com.example.manufacture.ui.home.ProductViewModel;

import java.text.DecimalFormat;
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
        binding.componentBatchesText.getEditText().setText("-");

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
                        if (product != null) {
                            subscriptions.add(product.getProductName());
                            mAdapter.notifyDataSetChanged();
                        }
                    });
        }

        mAdapter.setOnSubscriptionClicked(productName -> productViewModel.getProductByName(productName).observe(getActivity(), product -> {
            if (product != null) {
                String[] componentsAmounts = product.getComponents().split(":");
                int componentId = component.getId();
                int availableAmount = Integer.parseInt(component.getAvailableAmount());
                int amount = 0;

                for (int i = 0; i < componentsAmounts.length; i += 2) {
                    if (Integer.parseInt(componentsAmounts[i]) == componentId) {
                        amount = Integer.parseInt(componentsAmounts[i + 1]);
                        break;
                    }
                }

                double batchesAmount = availableAmount * 1.0 / amount * 1.0;
                DecimalFormat twoDForm = new DecimalFormat("#.#");
                batchesAmount = Double.parseDouble(twoDForm.format(batchesAmount));

                binding.componentBatchesText.getEditText().setText(String.format("%s", batchesAmount));
            }
        }));

        mRecyclerView.setAdapter(mAdapter);

        //update component
        binding.updateComponentBT.setOnClickListener(v -> {
            String componentName = binding.componentNameEditText.getEditText().getText().toString();
            String availableAmount = binding.componentAvailableAmountEditText.getEditText().getText().toString();
            String providerName = binding.componentProviderNameEditText.getEditText().getText().toString();

            component.setComponentName(componentName);
            component.setAvailableAmount(availableAmount);
            component.setProviderName(providerName);

            componentsViewModel.update(component);

            Toast.makeText(getActivity(), "Component Updated", Toast.LENGTH_SHORT).show();
            getDialog().dismiss();
        });

        //cancel component
        binding.cancelComponentBT.setOnClickListener(v -> getDialog().dismiss());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getDialog()).getWindow().setBackgroundDrawableResource(R.drawable.edit_round_2);
        int width = getResources().getDimensionPixelSize(R.dimen._329sdp);
        int height = getResources().getDimensionPixelSize(R.dimen._455sdp);
        getDialog().getWindow().setLayout(width, height);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        componentsViewModel = ViewModelProviders.of(getActivity()).get(ComponentsViewModel.class);
        productViewModel = ViewModelProviders.of(getActivity()).get(ProductViewModel.class);
    }
}