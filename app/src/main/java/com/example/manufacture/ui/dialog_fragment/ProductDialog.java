package com.example.manufacture.ui.dialog_fragment;

import android.graphics.Color;
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

import com.example.manufacture.R;
import com.example.manufacture.databinding.ProductDialogBinding;
import com.example.manufacture.model.Component;
import com.example.manufacture.model.Product;
import com.example.manufacture.model.Production;
import com.example.manufacture.ui.components.ComponentsViewModel;
import com.example.manufacture.ui.dashboard.ProductionViewModel;
import com.example.manufacture.ui.home.ProductViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class ProductDialog extends DialogFragment {
    private ProductViewModel productViewModel;
    private ComponentsViewModel componentsViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ProductDialogBinding binding = DataBindingUtil.inflate(inflater, R.layout.product_dialog, container, false);
        View view = binding.getRoot();

        binding.backButton.setOnClickListener(v -> Objects.requireNonNull(getDialog()).dismiss());

        if (getTag().equals("Add_Product_Dialog")) {
            binding.addComponent.setVisibility(View.GONE);
            binding.previousComponent.setVisibility(View.GONE);

            binding.cancelBT.setOnClickListener(v -> Objects.requireNonNull(getDialog()).dismiss());

            //----------------------------------------------------------

            HashMap<String, Integer> componentMap = new HashMap<>();
            HashMap<Integer, Component> componentIdMap = new HashMap<>();

            componentsViewModel = ViewModelProviders.of(getActivity()).get(ComponentsViewModel.class);
            List<Component> list = new ArrayList<>();
            componentsViewModel.getAllComponents().observe(getActivity(), components -> {
                list.addAll(components);
                for (int i = 0; i < list.size(); i++) {
                    componentMap.put(list.get(i).getComponentName(), list.get(i).getId());
                    componentIdMap.put(list.get(i).getId(), list.get(i));
                }
            });

            //----------------------------------------------------------

            StringBuilder componentsStringBuilder = new StringBuilder();

            binding.addComponentProduct.setOnClickListener(v -> {
                String productName = binding.productNameEditText.getEditText().getText().toString();

                String componentName = binding.componentsEditText.getEditText().getText().toString();
                String componentAmount = binding.componentAmountEditText.getEditText().getText().toString();

                if (Integer.parseInt(componentAmount) <= 0)
                    componentAmount = "1";

                int id;

                if (componentName.isEmpty() || componentAmount.isEmpty() || productName.isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!componentMap.containsKey(componentName)) {
                    Component newComponent =
                            new Component(componentName, "Provider", "0");
                    id = componentsViewModel.insert(newComponent);
                } else id = componentMap.get(componentName);

                componentsStringBuilder.append(id).append(":").append(componentAmount).append(":");

                binding.componentsEditText.getEditText().setText("");
                binding.componentAmountEditText.getEditText().setText("");
            });

            //----------------------------------------------------------

            binding.addProductBT.setOnClickListener(v -> {
                String productName = binding.productNameEditText.getEditText().getText().toString();
                String components = componentsStringBuilder.toString();

                if (productName.isEmpty() || components.isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                int productId = productViewModel.insert(new Product(productName, components));

                String[] componentsId = components.split(":");

                for (int i = 0; i < componentsId.length; i += 2) {
                    int componentId = Integer.parseInt(componentsId[i]);
                    Component newComponent = componentIdMap.get(componentId);

                    newComponent.setSubscribedProducts(newComponent.getSubscribedProducts() + productId + ":");
                    componentsViewModel.update(newComponent);
                }

                Toast.makeText(getActivity(), "Product Saved", Toast.LENGTH_SHORT).show();
                Objects.requireNonNull(getDialog()).dismiss();

            });
        }

        if (getTag().equals("Update_Product_Dialog")) {
            binding.addComponentProduct.setVisibility(View.GONE);
            binding.componentsEditText.setEnabled(false);
            binding.componentsEditText.getEditText().setTextColor(Color.parseColor("#93aee6"));


            //get Product
            productViewModel = ViewModelProviders.of(getActivity()).get(ProductViewModel.class);
            Product product = productViewModel.getProduct();

            //display product
            binding.productNameEditText.getEditText().setText(product.getProductName());

            String componentsStr = product.getComponents();
            String[] componentsArray = componentsStr.split(":");

            componentsViewModel = ViewModelProviders.of(getActivity()).get(ComponentsViewModel.class);
            componentsViewModel.getComponentById(Integer.parseInt(componentsArray[0]))
                    .observe(getActivity(), component -> binding.componentsEditText.getEditText().setText(component.getComponentName()));
            binding.componentAmountEditText.getEditText().setText(componentsArray[1]);

            AtomicInteger x = new AtomicInteger(0);
            AtomicInteger y = new AtomicInteger(1);

            binding.previousComponent.setOnClickListener(v -> {
                if (y.get() == componentsArray.length - 1) {
                    x.set(0);
                    y.set(1);
                } else {
                    x.addAndGet(2);
                    y.addAndGet(2);
                }
                componentsViewModel.getComponentById(Integer.parseInt(componentsArray[x.get()]))
                        .observe(getActivity(), component -> binding.componentsEditText.getEditText().setText(component.getComponentName()));
                binding.componentAmountEditText.getEditText().setText(componentsArray[y.get()]);
            });

            //update product

            List<Production> productions = new ArrayList<>();

            ProductionViewModel productionViewModel =
                    ViewModelProviders.of(getActivity()).get(ProductionViewModel.class);

            productionViewModel.getProductionsByProductId(product.getId()).observe(getActivity(), productionsList -> productions.addAll(productionsList));

            binding.addComponent.setOnClickListener(v -> {
                String componentName = binding.componentsEditText.getEditText().getText().toString();
                String componentAmount = binding.componentAmountEditText.getEditText().getText().toString();

                if (componentName.isEmpty() || componentAmount.isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                componentsArray[y.get()] = componentAmount;

                binding.componentsEditText.getEditText().setText("");
                binding.componentAmountEditText.getEditText().setText("");
            });

            binding.addProductBT.setOnClickListener(v -> {
                String productName = binding.productNameEditText.getEditText().getText().toString();
                StringBuilder components = new StringBuilder();

                for (String s : componentsArray)
                    components.append(s).append(":");

                if (productName.isEmpty() || components.length() == 0) {
                    Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                product.setProductName(productName);
                product.setComponents(components.toString());

                productViewModel.update(product);

                for (Production production : productions) {
                    production.setProductName(productName);
                    productionViewModel.update(production);
                }

                Toast.makeText(getActivity(), "Product Updated", Toast.LENGTH_SHORT).show();
                Objects.requireNonNull(getDialog()).dismiss();
            });

            //cancel product

            binding.cancelBT.setOnClickListener(v -> getDialog().dismiss());

        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getDialog()).getWindow().setBackgroundDrawableResource(R.drawable.edit_round_2);
        int width = getResources().getDimensionPixelSize(R.dimen._329sdp);
        int height = getResources().getDimensionPixelSize(R.dimen._355sdp);
        getDialog().getWindow().setLayout(width, height);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        productViewModel = ViewModelProviders.of(getActivity()).get(ProductViewModel.class);
        componentsViewModel = ViewModelProviders.of(getActivity()).get(ComponentsViewModel.class);
    }
}