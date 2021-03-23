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

            ComponentsViewModel componentsViewModel = ViewModelProviders.of(this).get(ComponentsViewModel.class);
            List<Component> list = new ArrayList<>();
            componentsViewModel.getAllComponents().observe(this, components -> {
                Log.i("TAG", "sadbugs: pro 1");
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

                if (Double.parseDouble(componentAmount) <= 0.0)
                    componentAmount = "1.0";

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

                boolean stockState = false;
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < componentsId.length; i += 2) {
                    int componentId = Integer.parseInt(componentsId[i]);
                    Component newComponent = componentIdMap.get(componentId);

                    double available = Double.parseDouble(newComponent.getAvailableAmount());
                    double amount = Double.parseDouble(componentsId[i + 1]);

                    double availableBatches = getAvailableBatches(available, amount);

                    if (availableBatches <= 2.0) {
                        newComponent.setLowStock(true);
                        stockState = true;
                        builder.append("1").append(":");
                    } else builder.append("0").append(":");
                }

                newProduct.setLowStock(stockState);
                newProduct.setStockBatches(builder.toString());
                int productId = productViewModel.insert(newProduct);

                //subscribe the product to its components
                for (int i = 0; i < componentsId.length; i += 2) {
                    int componentId = Integer.parseInt(componentsId[i]);
                    Component newComponent = componentIdMap.get(componentId);

                    newComponent.setSubscribedProducts(newComponent.getSubscribedProducts() + productId + ":");

                    double available = Double.parseDouble(newComponent.getAvailableAmount());
                    double amount = Double.parseDouble(componentsId[i + 1]);

                    double availableBatches = getAvailableBatches(available, amount);

                    //check all newComponent subscribed products to set low stock
                    int state = 0;
                    StringBuilder batchBuilder = new StringBuilder();
                    String[] prodArr = newComponent.getSubscribedProducts().split(":");
                    String[] stateArr = newComponent.getStockBatches().split(":");

                    for (int j = 0; j < prodArr.length; j++) {
                        int subId = Integer.parseInt(prodArr[j]);
                        if (productId == subId) {
                            if (availableBatches <= 2.0) {
                                batchBuilder.append("1").append(":");
                                ++state;
                            } else batchBuilder.append("0").append(":");
                        } else {
                            String s = stateArr[j];
                            batchBuilder.append(s).append(":");
                            state += Integer.parseInt(s);
                        }
                    }

                    newComponent.setStockBatches(batchBuilder.toString());

                    newComponent.setLowStock(state != 0);

                    componentsViewModel.update(newComponent);
                }

                Objects.requireNonNull(getDialog()).dismiss();
            });
        }

        if (getTag().equals("Update_Product_Dialog")) {
            //set up
            binding.addComponentProduct.setVisibility(View.GONE);
            binding.componentsEditText.setEnabled(false);
            binding.componentsEditText.getEditText().setTextColor(Color.parseColor("#93aee6"));

            productViewModel = ViewModelProviders.of(getActivity()).get(ProductViewModel.class);
            componentsViewModel = ViewModelProviders.of(this).get(ComponentsViewModel.class);

            //fill componentIdMap
            HashMap<Integer, Component> componentIdMap = new HashMap<>();

            List<Component> list = new ArrayList<>();
            componentsViewModel.getAllComponents().observe(this, components -> {
                Log.i("TAG", "sadbugs: pro 9");
                list.addAll(components);
                for (int i = 0; i < list.size(); i++)
                    componentIdMap.put(list.get(i).getId(), list.get(i));
            });

            //get Product
            Product product = productViewModel.getProduct();

            //display product
            binding.productNameEditText.getEditText().setText(product.getProductName());

            String componentsStr = product.getComponents();
            String[] componentsArray = componentsStr.split(":");

            AtomicInteger x = new AtomicInteger(0);
            AtomicInteger y = new AtomicInteger(1);

            componentsViewModel.getComponentById(Integer.parseInt(componentsArray[x.get()]))
                    .observe(this, component -> {
                        Log.i("TAG", "sadbugs: pro 2");
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
                            Log.i("TAG", "sadbugs: pro 3");
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

            productionViewModel.getProductionsByProductId(product.getId()).observe(this, productionsList -> {
                Log.i("TAG", "sadbugs: pro 4");
                productions.addAll(productionsList);
            });

            //A list of components in case of component amount was changed
            ArrayList<Component> updatedComponents = new ArrayList<>();

            binding.addComponent.setOnClickListener(v -> {
                String componentName = binding.componentsEditText.getEditText().getText().toString();
                String componentAmount = binding.componentAmountEditText.getEditText().getText().toString();

                if (componentName.isEmpty() || componentAmount.isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Integer.parseInt(componentAmount) == 0)
                    componentAmount = "1";

                componentsArray[y.get()] = componentAmount;

                //check if low stock
                int componentID = Integer.parseInt(componentsArray[x.get()]);
                double amount = Double.parseDouble(componentAmount);

                Component component = componentIdMap.get(componentID);

                double available = Double.parseDouble(component.getAvailableAmount());
                double availableBatches = getAvailableBatches(available, amount);
                if (availableBatches <= 2.0) {
                    product.setLowStock(true);
                    component.setLowStock(true);
                }

                //check all newComponent subscribed products to set low stock
                int state = 0;
                StringBuilder batchBuilder = new StringBuilder();
                String[] prodArr = component.getSubscribedProducts().split(":");
                String[] stateArr = component.getStockBatches().split(":");

                for (int j = 0; j < prodArr.length; j++) {
                    int subId = Integer.parseInt(prodArr[j]);
                    if (product.getId() == subId) {
                        if (availableBatches <= 2.0) {
                            batchBuilder.append("1").append(":");
                            ++state;
                        } else batchBuilder.append("0").append(":");
                    } else {
                        String s = stateArr[j];
                        batchBuilder.append(s).append(":");
                        state += Integer.parseInt(s);
                    }
                }

                component.setStockBatches(batchBuilder.toString());

                component.setLowStock(state != 0);

                updatedComponents.add(component);

                binding.componentsEditText.getEditText().setText("");
                binding.componentAmountEditText.getEditText().setText("");
            });

            binding.addProductBT.setOnClickListener(v -> {
                String productName = binding.productNameEditText.getEditText().getText().toString();
                StringBuilder components = new StringBuilder();

                //check if any component is low stock
                boolean productStockState = false;

                for (int i = 0; i < componentsArray.length; i += 2) {
                    String id = componentsArray[i];
                    String componentAmount = componentsArray[i + 1];
                    components.append(id).append(":").append(componentAmount).append(":");

                    int componentId = Integer.parseInt(id);
                    double amount = Double.parseDouble(componentAmount);

                    Component component = componentIdMap.get(componentId);
                    double available = Double.parseDouble(component.getAvailableAmount());
                    if (getAvailableBatches(available, amount) <= 2.0)
                        productStockState = true;
                }

                product.setLowStock(productStockState);

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
        int height = getResources().getDimensionPixelSize(R.dimen._355sdp);
        getDialog().getWindow().setLayout(width, height);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
    }
}