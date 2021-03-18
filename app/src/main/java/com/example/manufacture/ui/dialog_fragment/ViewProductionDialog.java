package com.example.manufacture.ui.dialog_fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manufacture.R;
import com.example.manufacture.databinding.ViewProductionDialogBinding;
import com.example.manufacture.model.Component;
import com.example.manufacture.model.ComponentBatch;
import com.example.manufacture.model.Product;
import com.example.manufacture.model.Production;
import com.example.manufacture.ui.adapter.BatchAdapter;
import com.example.manufacture.ui.adapter.DetailsAdapter;
import com.example.manufacture.ui.components.ComponentsViewModel;
import com.example.manufacture.ui.dashboard.ProductionViewModel;
import com.example.manufacture.ui.home.ProductViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

public class ViewProductionDialog extends DialogFragment {
    private ProductionViewModel productionViewModel;
    private ProductViewModel productViewModel;
    private ComponentsViewModel componentsViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewProductionDialogBinding binding = DataBindingUtil.inflate(inflater, R.layout.view_production_dialog, container, false);
        View view = binding.getRoot();

        //get production
        productionViewModel = ViewModelProviders.of(getActivity()).get(ProductionViewModel.class);
        Production production = productionViewModel.getProduction();

        //display product name
        binding.viewProductionProductName.setText(production.getProductName());

        //set up adapter
        RecyclerView mRecyclerView = binding.viewProductionRecyclerView;

        BatchAdapter mAdapter = new BatchAdapter();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ArrayList<ComponentBatch> batches = new ArrayList<>();

        mAdapter.setList(batches);
        mRecyclerView.setAdapter(mAdapter);

        //display product components
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        componentsViewModel = ViewModelProviders.of(this).get(ComponentsViewModel.class);

        int productID = production.getProductID();

        productViewModel.getProductById(productID).observe(getActivity(), product -> {
            String componentsStr = product.getComponents();
            String[] componentsArray = componentsStr.split(":");

            for (int i = 0; i < componentsArray.length; i += 2) {
                int id = Integer.parseInt(componentsArray[i]);
                String amount = componentsArray[i + 1];

                componentsViewModel
                        .getComponentById(id)
                        .observe(this, component -> {

                            double batchesAmount = Integer.parseInt(component.getAvailableAmount()) * 1.0 / Integer.parseInt(amount) * 1.0;
                            DecimalFormat twoDForm = new DecimalFormat("#.#");
                            batchesAmount = Double.parseDouble(twoDForm.format(batchesAmount));

                            batches.add(new ComponentBatch(component.getComponentName(), batchesAmount + "", amount));
                            mAdapter.notifyDataSetChanged();
                        });
            }

        });

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
        productionViewModel = ViewModelProviders.of(getActivity()).get(ProductionViewModel.class);
        productViewModel = ViewModelProviders.of(getActivity()).get(ProductViewModel.class);
        componentsViewModel = ViewModelProviders.of(getActivity()).get(ComponentsViewModel.class);
    }
}
