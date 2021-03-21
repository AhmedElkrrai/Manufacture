package com.example.manufacture.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manufacture.R;
import com.example.manufacture.databinding.FragmentDashboardBinding;
import com.example.manufacture.model.Product;
import com.example.manufacture.model.Production;
import com.example.manufacture.ui.adapter.ProductionAdapter;
import com.example.manufacture.ui.dialog_fragment.DeleteDialog;
import com.example.manufacture.ui.dialog_fragment.ProductDialog;
import com.example.manufacture.ui.dialog_fragment.ViewProductionDialog;
import com.example.manufacture.ui.home.ProductViewModel;

public class DashboardFragment extends Fragment {

    public DashboardFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentDashboardBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);
        View view = binding.getRoot();

        ProductionViewModel productionViewModel = new ViewModelProvider(getActivity()).get(ProductionViewModel.class);

        RecyclerView mRecyclerView = binding.dashBoardRecyclerView;

        ProductionAdapter mAdapter = new ProductionAdapter();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        productionViewModel.getAllProductions().observe(getActivity(), productions -> mAdapter.setList(productions));

        mAdapter.setOnItemClickListener(production -> {
            productionViewModel.setProduction(production);
            new ViewProductionDialog().show(getFragmentManager(), "View_Production_Dialog");
        });

        mAdapter.setOnItemLongClickListener(production -> {
            productionViewModel.setProduction(production);
            new DeleteDialog().show(getFragmentManager(), "Delete_Production_Dialog");
        });

        return view;
    }
}