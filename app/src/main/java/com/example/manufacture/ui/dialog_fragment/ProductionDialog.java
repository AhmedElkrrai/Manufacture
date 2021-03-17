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
import com.example.manufacture.model.Product;
import com.example.manufacture.model.Production;
import com.example.manufacture.ui.dashboard.ProductionViewModel;
import com.example.manufacture.ui.home.ProductViewModel;

import java.time.LocalDate;
import java.util.Objects;

public class ProductionDialog extends DialogFragment {
    private ProductionViewModel productionViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ProductionDialogBinding binding = DataBindingUtil.inflate(inflater, R.layout.production_dialog, container, false);
        View view = binding.getRoot();

        productionViewModel = ViewModelProviders.of(getActivity()).get(ProductionViewModel.class);

        Product product = productionViewModel.getProduct();

        binding.productionDialogProductName.setText(productionViewModel.getProduct().getProductName());

        binding.addProductionBT.setOnClickListener(v -> {

            String patchNumber = binding.patchNumberET.getEditText().getText().toString();

            if (patchNumber.isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Patch Number First " + patchNumber, Toast.LENGTH_SHORT).show();
                return;
            }

            productionViewModel.insert(new Production(product.getId(), getCurrentDate(), patchNumber, product.getProductName()));

            Toast.makeText(getActivity(), "On Production", Toast.LENGTH_SHORT).show();
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
        int height = getResources().getDimensionPixelSize(R.dimen._305sdp);
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