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
import com.example.manufacture.databinding.ProductDialogBinding;
import com.example.manufacture.model.Component;
import com.example.manufacture.model.Product;
import com.example.manufacture.ui.components.ComponentsViewModel;
import com.example.manufacture.ui.home.ProductViewModel;

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

            StringBuilder componentsList = new StringBuilder();

            binding.addComponentProduct.setOnClickListener(v -> {
                String componentName = binding.componentsEditText.getEditText().getText().toString();
                String componentAmount = binding.componentAmountEditText.getEditText().getText().toString();

                if (componentName.isEmpty() || componentAmount.isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                componentsList.append(componentName).append(":").append(componentAmount).append(":");
                componentsViewModel.insert(new Component(componentName, "provider", "0","0"));

                binding.componentsEditText.getEditText().setText("");
                binding.componentAmountEditText.getEditText().setText("");
            });

            binding.addProductBT.setOnClickListener(v -> {
                String productName = binding.productNameEditText.getEditText().getText().toString();
                String components = componentsList.toString();

                if (productName.isEmpty() || components.isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                productViewModel.insert(new Product(productName, components));

                Toast.makeText(getActivity(), "Product Saved", Toast.LENGTH_SHORT).show();
                Objects.requireNonNull(getDialog()).dismiss();

            });
        }

        if (getTag().equals("Update_Product_Dialog")) {
            binding.addComponentProduct.setVisibility(View.GONE);
            //get Product
            productViewModel = ViewModelProviders.of(getActivity()).get(ProductViewModel.class);
            Product product = productViewModel.getProduct();

            //display product
            binding.productNameEditText.getEditText().setText(product.getProductName());

            String componentsStr = product.getComponents();
            String[] componentsArray = componentsStr.split(":");


            binding.componentsEditText.getEditText().setText(componentsArray[0]);
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
                binding.componentsEditText.getEditText().setText(componentsArray[x.get()]);
                binding.componentAmountEditText.getEditText().setText(componentsArray[y.get()]);
            });

            //update product

            binding.addComponent.setOnClickListener(v -> {
                String componentName = binding.componentsEditText.getEditText().getText().toString();
                String componentAmount = binding.componentAmountEditText.getEditText().getText().toString();

                if (componentName.isEmpty() || componentAmount.isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                componentsArray[x.get()] = componentName;
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

                Toast.makeText(getActivity(), "Product Updated", Toast.LENGTH_SHORT).show();
                Objects.requireNonNull(getDialog()).dismiss();

            });

            //delete product
            binding.cancelBT.setText("مسح");

            binding.cancelBT.setOnClickListener(view1 -> {
                productViewModel.delete(product);
                Toast.makeText(getActivity(), "Product Deleted", Toast.LENGTH_SHORT).show();
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