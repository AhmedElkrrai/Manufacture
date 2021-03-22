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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.manufacture.R;
import com.example.manufacture.databinding.ProductDialogBinding;
import com.example.manufacture.model.Component;
import com.example.manufacture.model.Product;
import com.example.manufacture.model.Production;
import com.example.manufacture.ui.components.ComponentsViewModel;
import com.example.manufacture.ui.dashboard.ProductionViewModel;
import com.example.manufacture.ui.home.ProductViewModel;

import java.text.DecimalFormat;
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

            componentsViewModel = ViewModelProviders.of(this).get(ComponentsViewModel.class);
            List<Component> list = new ArrayList<>();
            componentsViewModel.getAllComponents().observe(this, components -> {
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

                Product newProduct = new Product(productName, components);

                String[] componentsId = components.split(":");

                if (productName.isEmpty() || components.isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                //check if low stock
                for (int i = 0; i < componentsId.length; i += 2) {
                    int componentId = Integer.parseInt(componentsId[i]);
                    Component newComponent = componentIdMap.get(componentId);

                    boolean componentState = newComponent.isLowStock();

                    int available = Integer.parseInt(newComponent.getAvailableAmount());
                    int amount = Integer.parseInt(componentsId[i + 1]);

                    double availableBatches = getAvailableBatches(available, amount);

                    if (availableBatches <= 2.0) {
                        newProduct.setLowStock(true);
                        newComponent.setLowStock(true);
                    } else {
                        newProduct.setLowStock(false);
                        newComponent.setLowStock(false);
                    }

                    if (componentState != newComponent.isLowStock())
                        componentsViewModel.update(newComponent);
                }

                int productId = productViewModel.insert(newProduct);

                //subscribe the product to its components
                for (int i = 0; i < componentsId.length; i += 2) {
                    int componentId = Integer.parseInt(componentsId[i]);
                    Component newComponent = componentIdMap.get(componentId);

                    newComponent.setSubscribedProducts(newComponent.getSubscribedProducts() + productId + ":");
                    componentsViewModel.update(newComponent);
                }

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

            AtomicInteger x = new AtomicInteger(0);
            AtomicInteger y = new AtomicInteger(1);

            componentsViewModel = ViewModelProviders.of(this).get(ComponentsViewModel.class);
            componentsViewModel.getComponentById(Integer.parseInt(componentsArray[x.get()]))
                    .observe(this, component -> {
                        if (component != null) {
                            binding.componentsEditText.getEditText().setText(component.getComponentName());
                            binding.componentAmountEditText.getEditText().setText(componentsArray[y.get()]);
                        }
                    });

            binding.previousComponent.setOnClickListener(v -> {
                if (y.get() == componentsArray.length - 1) {
                    x.set(0);
                    y.set(1);
                } else {
                    x.addAndGet(2);
                    y.addAndGet(2);
                }
                componentsViewModel.getComponentById(Integer.parseInt(componentsArray[x.get()]))
                        .observe(this, component -> {
                            if (component != null) {
                                binding.componentsEditText.getEditText().setText(component.getComponentName());
                                binding.componentAmountEditText.getEditText().setText(componentsArray[y.get()]);
                            }
                        });
            });

            //update product

            //A list of on going productions in case of product name was changed
            List<Production> productions = new ArrayList<>();

            ProductionViewModel productionViewModel = ViewModelProviders.of(getActivity()).get(ProductionViewModel.class);

            productionViewModel.getProductionsByProductId(product.getId()).observe(getActivity(), productionsList -> productions.addAll(productionsList));

            //A list of components in case of component amount was changed
            ArrayList<Component> updatedComponents = new ArrayList<>();

            binding.addComponent.setOnClickListener(v -> {
                String componentName = binding.componentsEditText.getEditText().getText().toString();
                String componentAmount = binding.componentAmountEditText.getEditText().getText().toString();

                if (componentName.isEmpty() || componentAmount.isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                componentsArray[y.get()] = componentAmount;

                //check if low stock
                int componentID = Integer.parseInt(componentsArray[x.get()]);
                int amount = Integer.parseInt(componentAmount);
                componentsViewModel.getComponentById(componentID).observe(this, component -> {
                    boolean componentState = component.isLowStock();

                    int available = Integer.parseInt(component.getAvailableAmount());
                    double availableBatches = getAvailableBatches(available, amount);
                    if (availableBatches <= 2.0) {
                        product.setLowStock(true);
                        component.setLowStock(true);
                    } else {
                        product.setLowStock(false);
                        component.setLowStock(false);
                    }

                    if (componentState != component.isLowStock())
                        updatedComponents.add(component);
                });

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

                if (!updatedComponents.isEmpty()) {
                    for (Component component : updatedComponents)
                        componentsViewModel.update(component);
                }

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
        componentsViewModel = ViewModelProviders.of(this).get(ComponentsViewModel.class);
    }

    private double getAvailableBatches(int availableAmount, int amount) {
        double batchesAmount = availableAmount * 1.0 / amount * 1.0;
        DecimalFormat twoDForm = new DecimalFormat("#.#");
        return Double.parseDouble(twoDForm.format(batchesAmount));
    }
}