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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manufacture.R;
import com.example.manufacture.databinding.DetailsDialogBinding;

import com.example.manufacture.model.Details;
import com.example.manufacture.model.Product;
import com.example.manufacture.ui.adapter.DetailsAdapter;
import com.example.manufacture.ui.components.ComponentsViewModel;
import com.example.manufacture.ui.home.ProductViewModel;

import java.util.ArrayList;
import java.util.Objects;

public class DetailsDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DetailsDialogBinding binding = DataBindingUtil.inflate(inflater, R.layout.details_dialog, container, false);
        View view = binding.getRoot();

        ComponentsViewModel componentsViewModel = ViewModelProviders.of(this).get(ComponentsViewModel.class);

        //get Product
        ProductViewModel productViewModel = ViewModelProviders.of(getActivity()).get(ProductViewModel.class);
        Product product = productViewModel.getProduct();

        //display product name
        binding.detailsProductName.setText(product.getProductName());

        //display product components
        RecyclerView mRecyclerView = binding.detailsRecyclerView;

        DetailsAdapter mAdapter = new DetailsAdapter();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ArrayList<Details> details = new ArrayList<>();

        String componentsStr = product.getComponents();
        String[] componentsArray = componentsStr.split(":");

        mAdapter.setList(details);

        for (int i = 0; i < componentsArray.length; i += 2) {
            int id = Integer.parseInt(componentsArray[i]);
            String amount = componentsArray[i + 1];

            componentsViewModel
                    .getComponentById(id)
                    .observe(this, component -> {
                        Log.i("TAG", "sadbugs: details_dialog");
                        if (component != null) {
                            details.add(new Details(component.getComponentName(), component.getAvailableAmount(), amount));
                            mAdapter.notifyDataSetChanged();
                        }
                    });
        }

        mRecyclerView.setAdapter(mAdapter);
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
}