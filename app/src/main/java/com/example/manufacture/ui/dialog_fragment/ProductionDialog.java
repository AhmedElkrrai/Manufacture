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
import com.example.manufacture.databinding.ProductionDialogBinding;
import com.example.manufacture.model.Component;
import com.example.manufacture.model.Consumption;
import com.example.manufacture.model.Product;
import com.example.manufacture.model.Production;
import com.example.manufacture.ui.adapter.ConsumptionAdapter;
import com.example.manufacture.ui.components.ComponentsViewModel;
import com.example.manufacture.ui.dashboard.ProductionViewModel;
import com.example.manufacture.ui.home.ProductViewModel;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProductionDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ProductionDialogBinding binding = DataBindingUtil.inflate(inflater, R.layout.production_dialog, container, false);
        View view = binding.getRoot();

        //init view models
        ProductionViewModel productionViewModel = ViewModelProviders.of(getActivity()).get(ProductionViewModel.class);
        ProductViewModel productViewModel = ViewModelProviders.of(getActivity()).get(ProductViewModel.class);
        ComponentsViewModel componentsViewModel = ViewModelProviders.of(this).get(ComponentsViewModel.class);

        //display product name
        Product product = productionViewModel.getProduct();
        binding.productionDialogProductName.setText(productionViewModel.getProduct().getProductName());

        //set up consumption adapter
        RecyclerView mRecyclerView = binding.consumptionRecyclerView;

        ConsumptionAdapter mAdapter = new ConsumptionAdapter();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ArrayList<Consumption> consumptions = new ArrayList<>();

        mAdapter.setList(consumptions);
        mRecyclerView.setAdapter(mAdapter);

        //get component-amount map
        String[] componentsArr = product.getComponents().split(":");
        HashMap<Component, String> componentsAmountMap = new HashMap<>();

        product.setLowStock(false);

        for (int i = 0; i < componentsArr.length; i += 2) {
            String componentID = componentsArr[i];
            String amount = componentsArr[i + 1];

            componentsViewModel.getComponentById(Integer.parseInt(componentID))
                    .observe(this, component -> {
                        Log.i("TAG", "sadbugs: production_dialog");
                        if (component != null) {
                            componentsAmountMap.put(component, amount);

                            //add consumption to the adapter
                            double availableAmount = Double.parseDouble(component.getAvailableAmount());
                            double consumedAmount = Double.parseDouble(amount);

                            String then = String.valueOf(availableAmount - consumedAmount);
                            if (availableAmount - consumedAmount < 0.0)
                                then = "-";

                            double batchesAmount = availableAmount / consumedAmount;
                            DecimalFormat twoDForm = new DecimalFormat("#.#");
                            batchesAmount = Double.parseDouble(twoDForm.format(batchesAmount));

                            if (batchesAmount <= 3.0)
                                product.setLowStock(true);

                            consumptions.add(new Consumption(component.getComponentName(), String.valueOf(availableAmount), then, String.format("%s", batchesAmount)));

                            mAdapter.notifyDataSetChanged();
                        }
                    });
        }

        //add production
        binding.addProductionBT.setOnClickListener(v -> {

            //get patch number
            String patchNumber = binding.patchNumberET.getEditText().getText().toString();

            //get date
            String batchDate = binding.patchDateET.getEditText().getText().toString();
            if (batchDate.isEmpty())
                batchDate = getCurrentDate();

            if (patchNumber.isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Batch Number First " + patchNumber, Toast.LENGTH_SHORT).show();
                return;
            }

            //check stock before consuming

            for (Map.Entry<Component, String> entry : componentsAmountMap.entrySet()) {
                Component component = entry.getKey();
                double amount = Double.parseDouble(entry.getValue());
                double availableAmount = Double.parseDouble(component.getAvailableAmount());

                if (availableAmount == 0.0) {
                    String warningMessage = "Material(s) are Out Of Stock \n \t \tProduction Canceled.";
                    Toast.makeText(getActivity(), warningMessage, Toast.LENGTH_LONG).show();
                    getDialog().dismiss();
                    return;
                }

                if (availableAmount / amount < 1.0) {
                    String warningMessage = "Material(s) Required. \nProduction Canceled.";
                    Toast.makeText(getActivity(), warningMessage, Toast.LENGTH_LONG).show();
                    getDialog().dismiss();
                    return;
                }

                if (availableAmount / amount <= 2.0) {
                    String warningMessage = " WARNING : Material(s) are On Low Stock.";
                    Toast.makeText(getActivity(), warningMessage, Toast.LENGTH_LONG).show();
                }
            }

            // consume material(s)
            for (Map.Entry<Component, String> entry : componentsAmountMap.entrySet()) {
                Component component = entry.getKey();
                double amount = Double.parseDouble(entry.getValue());
                double availableAmount = Double.parseDouble(component.getAvailableAmount());

                availableAmount -= amount;
                component.setAvailableAmount(String.valueOf(availableAmount));

                if ((availableAmount / amount) <= 2.0) {
                    component.setLowStock(true);
                    product.setLowStock(true);
                }
                componentsViewModel.update(component);
            }

            //start production
            productionViewModel.insert(new Production(product.getId(), batchDate, patchNumber, product.getProductName()));

            //check is low stock and show caution
            productViewModel.update(product);

            getDialog().dismiss();
        });

        binding.cancelProductionBT.setOnClickListener(v -> Objects.requireNonNull(getDialog()).dismiss());

        return view;
    }

    private String getCurrentDate() {
        return LocalDate.now().toString();
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getDialog()).getWindow().setBackgroundDrawableResource(R.drawable.edit_round_2);
        int width = getResources().getDimensionPixelSize(R.dimen._329sdp);
        int height = getResources().getDimensionPixelSize(R.dimen._555sdp);
        getDialog().getWindow().setLayout(width, height);
    }
}