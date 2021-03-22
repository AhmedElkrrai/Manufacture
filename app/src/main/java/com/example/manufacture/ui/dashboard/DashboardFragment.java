package com.example.manufacture.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manufacture.R;
import com.example.manufacture.databinding.FragmentDashboardBinding;
import com.example.manufacture.ui.adapter.ProductionAdapter;
import com.example.manufacture.ui.dialog_fragment.DeleteDialog;
import com.example.manufacture.ui.dialog_fragment.ViewProductionDialog;

public class DashboardFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentDashboardBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);
        View view = binding.getRoot();

        ProductionViewModel productionViewModel = new ViewModelProvider(getActivity()).get(ProductionViewModel.class);

        RecyclerView mRecyclerView = binding.dashBoardRecyclerView;

        ProductionAdapter mAdapter = new ProductionAdapter();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        productionViewModel.getAllProductions().observe(getActivity(), productions -> {
            Log.i("TAG", "sadbugs: 2");
            if (productions.isEmpty()) {
                binding.emptyDashboard.setVisibility(View.VISIBLE);
            } else binding.emptyDashboard.setVisibility(View.GONE);

            mAdapter.setList(productions);
        });

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