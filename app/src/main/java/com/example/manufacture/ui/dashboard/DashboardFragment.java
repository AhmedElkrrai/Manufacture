package com.example.manufacture.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manufacture.R;
import com.example.manufacture.databinding.FragmentDashboardBinding;
import com.example.manufacture.model.Product;
import com.example.manufacture.model.Production;
import com.example.manufacture.ui.adapter.ProductionAdapter;
import com.example.manufacture.ui.components.ComponentsViewModel;
import com.example.manufacture.ui.home.HomeFragment;
import com.example.manufacture.ui.home.ProductViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DashboardFragment extends Fragment {

    public DashboardFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentDashboardBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);
        View view = binding.getRoot();

        ProductionViewModel productionViewModel = new ViewModelProvider(getActivity()).get(ProductionViewModel.class);

        binding.setLifecycleOwner(this);

        RecyclerView mRecyclerView = binding.dashBoardRecyclerView;

        ProductionAdapter mAdapter = new ProductionAdapter();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        productionViewModel.getAllProductions().observe(getActivity(), productions -> mAdapter.setList(productions));

        return view;
    }
}