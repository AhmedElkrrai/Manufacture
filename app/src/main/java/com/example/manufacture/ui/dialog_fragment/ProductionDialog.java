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

import com.example.manufacture.R;
import com.example.manufacture.databinding.ProductionDialogBinding;
import com.example.manufacture.model.Component;
import com.example.manufacture.model.Product;
import com.example.manufacture.model.Production;
import com.example.manufacture.ui.components.ComponentsViewModel;
import com.example.manufacture.ui.dashboard.ProductionViewModel;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProductionDialog extends DialogFragment {
    private ProductionViewModel productionViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ProductionDialogBinding binding = DataBindingUtil.inflate(inflater, R.layout.production_dialog, container, false);
        View view = binding.getRoot();

        productionViewModel = ViewModelProviders.of(getActivity()).get(ProductionViewModel.class);
        ComponentsViewModel componentsViewModel = ViewModelProviders.of(getActivity()).get(ComponentsViewModel.class);

        Product product = productionViewModel.getProduct();

        String[] componentsArr = product.getComponents().split(":");
        HashMap<Component, String> componentsAmountMap = new HashMap<>();

        for (int i = 0; i < componentsArr.length; i += 2) {
            String componentID = componentsArr[i];
            int finalI = i;
            componentsViewModel.getComponentById(Integer.parseInt(componentID))
                    .observe(getActivity(), component -> componentsAmountMap.put(component, componentsArr[finalI + 1]));
        }

        binding.productionDialogProductName.setText(productionViewModel.getProduct().getProductName());

        binding.addProductionBT.setOnClickListener(v -> {

            //get patch number
            String patchNumber = binding.patchNumberET.getEditText().getText().toString();

            if (patchNumber.isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Batch Number First " + patchNumber, Toast.LENGTH_SHORT).show();
                return;
            }

            //check stock before consuming
            for (Map.Entry<Component, String> entry : componentsAmountMap.entrySet()) {
                Component component = entry.getKey();
                int amount = Integer.parseInt(entry.getValue());
                int availableAmount = Integer.parseInt(component.getAvailableAmount());

                if (availableAmount == 0) {
                    String warningMessage = "Material(s) are Out Of Stock \n \t \tProduction Canceled.";
                    Toast.makeText(getActivity(), warningMessage, Toast.LENGTH_LONG).show();
                    getDialog().dismiss();
                    return;
                }

                if (availableAmount / amount < 1) {
                    String warningMessage = "Material(s) Required. \nProduction Canceled.";
                    Toast.makeText(getActivity(), warningMessage, Toast.LENGTH_LONG).show();
                    getDialog().dismiss();
                    return;
                }

                if (availableAmount / amount <= 2) {
                    String warningMessage = " WARNING : Material(s) are On Low Stock. \n \t \t IMPORT \" " + component.getComponentName() + " \"";
                    Toast.makeText(getActivity(), warningMessage, Toast.LENGTH_LONG).show();
                }
            }

            //consume material(s)
            for (Map.Entry<Component, String> entry : componentsAmountMap.entrySet()) {
                Component component = entry.getKey();
                int amount = Integer.parseInt(entry.getValue());
                int availableAmount = Integer.parseInt(component.getAvailableAmount());

                availableAmount -= amount;
                component.setAvailableAmount(String.valueOf(availableAmount));

                componentsViewModel.update(component);
            }

            //start production
            productionViewModel.insert(new Production(product.getId(), getCurrentDate(), patchNumber, product.getProductName()));

            getDialog().dismiss();
        });

        binding.cancelProductionBT.setOnClickListener(v -> Objects.requireNonNull(getDialog()).dismiss());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getDialog()).getWindow().setBackgroundDrawableResource(R.drawable.edit_round_2);
        int width = getResources().getDimensionPixelSize(R.dimen._329sdp);
        int height = getResources().getDimensionPixelSize(R.dimen._505sdp);
        getDialog().getWindow().setLayout(width, height);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        productionViewModel = ViewModelProviders.of(getActivity()).get(ProductionViewModel.class);
    }

    private String getCurrentDate() {
        return LocalDate.now().toString();
    }
}