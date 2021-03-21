package com.example.manufacture.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manufacture.model.Product;
import com.example.manufacture.ui.adapter.ProductAdapter;
import com.example.manufacture.ui.dashboard.ProductionViewModel;
import com.example.manufacture.ui.dialog_fragment.DeleteDialog;
import com.example.manufacture.ui.dialog_fragment.DetailsDialog;
import com.example.manufacture.ui.dialog_fragment.ProductDialog;
import com.example.manufacture.R;
import com.example.manufacture.databinding.FragmentHomeBinding;
import com.example.manufacture.ui.dialog_fragment.ProductionDialog;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentHomeBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        View view = binding.getRoot();

        ProductViewModel productViewModel = new ViewModelProvider(getActivity()).get(ProductViewModel.class);

        binding.addProductButton.setOnClickListener(v -> {
            assert getFragmentManager() != null;
            new ProductDialog().show(getFragmentManager(), "Add_Product_Dialog");
        });

        RecyclerView mRecyclerView = binding.homeRecyclerView;

        ProductAdapter mAdapter = new ProductAdapter();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        productViewModel.getAllProducts().observe(getActivity(), products -> {
            if (products.isEmpty())
                binding.emptyProducts.setVisibility(View.VISIBLE);
            else binding.emptyProducts.setVisibility(View.GONE);
            mAdapter.setList(products);
        });

        mAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(Product product) {
                productViewModel.setProduct(product);
                new ProductDialog().show(getFragmentManager(), "Update_Product_Dialog");
            }

            @Override
            public void onDetailsClicked(Product product) {
                productViewModel.setProduct(product);
                new DetailsDialog().show(getFragmentManager(), "Details_Dialog");
            }

            @Override
            public void onProduceClicked(Product product) {
                ProductionViewModel productionViewModel = new ViewModelProvider(getActivity()).get(ProductionViewModel.class);
                productionViewModel.setProduct(product);

                new ProductionDialog().show(getFragmentManager(), "Production_Dialog");
            }
        });

        mAdapter.setOnItemLongClickListener(product -> {
            productViewModel.setProduct(product);
            new DeleteDialog().show(getFragmentManager(), "Delete_Product_Dialog");
        });

        return view;
    }

}