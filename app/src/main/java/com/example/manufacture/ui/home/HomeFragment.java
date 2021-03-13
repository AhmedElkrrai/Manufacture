package com.example.manufacture.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manufacture.ui.ProductAdapter;
import com.example.manufacture.ui.ProductDialog;
import com.example.manufacture.R;
import com.example.manufacture.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FragmentHomeBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_home, container, false);
        View view = binding.getRoot();

        ProductViewModel productViewModel = new ViewModelProvider(getActivity()).get(ProductViewModel.class);
        binding.setLifecycleOwner(this);
        binding.addProductButton.setOnClickListener(v -> {
            assert getFragmentManager() != null;
            new ProductDialog().show(getFragmentManager(), "Add_Product_Dialog");
        });

        RecyclerView mRecyclerView = binding.homeRecyclerView;

        ProductAdapter mAdapter = new ProductAdapter();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        productViewModel.getAllProducts().observe(getActivity(), products -> mAdapter.setList(products));

        mAdapter.setOnItemClickListener(product -> {
            productViewModel.setProduct(product);
            new ProductDialog().show(getFragmentManager(), "Update_Product_Dialog");
        });

        return view;
    }
}