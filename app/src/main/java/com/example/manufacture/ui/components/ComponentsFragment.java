package com.example.manufacture.ui.components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manufacture.R;
import com.example.manufacture.databinding.FragmentComponentsBinding;
import com.example.manufacture.ui.adapter.ComponentAdapter;
import com.example.manufacture.ui.dialog_fragment.ComponentDialog;

public class ComponentsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentComponentsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_components, container, false);
        View view = binding.getRoot();

        ComponentsViewModel componentsViewModel = new ViewModelProvider(getActivity()).get(ComponentsViewModel.class);
        binding.setLifecycleOwner(this);

        RecyclerView mRecyclerView = binding.componentRecyclerView;

        ComponentAdapter mAdapter = new ComponentAdapter();

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        mRecyclerView.setAdapter(mAdapter);

        componentsViewModel.getAllComponents().observe(getActivity(), components -> mAdapter.setList(components));

        mAdapter.setOnItemClickListener(component -> {
            componentsViewModel.setComponent(component);
            new ComponentDialog().show(getFragmentManager(), "Update_Component");
        });

        return view;
    }
}