package com.example.manufacture.ui.dialog_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.manufacture.R;
import com.example.manufacture.databinding.DeleteDialogBinding;
import com.example.manufacture.model.Component;
import com.example.manufacture.model.Product;
import com.example.manufacture.model.Production;
import com.example.manufacture.ui.components.ComponentsViewModel;
import com.example.manufacture.ui.dashboard.ProductionViewModel;
import com.example.manufacture.ui.home.ProductViewModel;

import java.util.ArrayList;
import java.util.Objects;

public class DeleteDialog extends DialogFragment {
    private ProductViewModel productViewModel;
    private ProductionViewModel productionViewModel;
    private ComponentsViewModel componentsViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DeleteDialogBinding binding = DataBindingUtil.inflate(inflater, R.layout.delete_dialog, container, false);
        View view = binding.getRoot();

        binding.cancelDeleteBT.setOnClickListener(v -> getDialog().dismiss());

        if (getTag().equals("Delete_Product_Dialog")) {
            productViewModel = ViewModelProviders.of(getActivity()).get(ProductViewModel.class);

            Product product = productViewModel.getProduct();
            binding.deleteItemName.setText(product.getProductName());

            binding.deleteItemBT.setOnClickListener(v -> {
                productViewModel.delete(product);
                getDialog().dismiss();
            });
        }

        if (getTag().equals("Delete_Component_Dialog")) {
            componentsViewModel = ViewModelProviders.of(getActivity()).get(ComponentsViewModel.class);
            productViewModel = ViewModelProviders.of(getActivity()).get(ProductViewModel.class);

            Component component = componentsViewModel.getComponent();
            binding.deleteItemName.setText(component.getComponentName());

            String[] subscriptions = component.getSubscribedProducts().split(":");
            ArrayList<Product> subscribedProducts = new ArrayList<>();

            for (String subscription : subscriptions) {
                int productId = Integer.parseInt(subscription);
                productViewModel.getProductById(productId).observe(getActivity(), product -> subscribedProducts.add(product));
            }

            binding.deleteItemBT.setOnClickListener(v -> {
                for (Product sub : subscribedProducts) {
                    if (sub != null) {
                        StringBuilder componentBuilder = new StringBuilder();
                        String[] components = sub.getComponents().split(":");
                        for (int i = 0; i < components.length; i += 2) {
                            int componentId = Integer.parseInt(components[i]);
                            if (component.getId() != componentId)
                                componentBuilder.append(components[i]).append(":").append(components[i + 1]).append(":");
                        }
                        sub.setComponents(componentBuilder.toString());
                        if (componentBuilder.length() == 0)
                            productViewModel.delete(sub);
                        else productViewModel.update(sub);
                    }
                }
                componentsViewModel.delete(component);
                getDialog().dismiss();
            });
        }

        if (getTag().equals("Delete_Production_Dialog")) {
            productionViewModel = ViewModelProviders.of(getActivity()).get(ProductionViewModel.class);

            Production production = productionViewModel.getProduction();
            binding.deleteItemName.setText(production.getProductName());

            binding.deleteItemBT.setOnClickListener(v -> {
                productionViewModel.delete(production);
                getDialog().dismiss();
            });
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getDialog()).getWindow().setBackgroundDrawableResource(R.drawable.edit_round_2);
        int width = getResources().getDimensionPixelSize(R.dimen._329sdp);
        int height = getResources().getDimensionPixelSize(R.dimen._235sdp);
        getDialog().getWindow().setLayout(width, height);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        productViewModel = ViewModelProviders.of(getActivity()).get(ProductViewModel.class);
        productionViewModel = ViewModelProviders.of(getActivity()).get(ProductionViewModel.class);
        componentsViewModel = ViewModelProviders.of(this).get(ComponentsViewModel.class);
    }
}
