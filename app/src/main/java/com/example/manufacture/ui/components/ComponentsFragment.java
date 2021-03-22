package com.example.manufacture.ui.components;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manufacture.R;
import com.example.manufacture.databinding.FragmentComponentsBinding;
import com.example.manufacture.model.Component;
import com.example.manufacture.ui.adapter.ComponentAdapter;
import com.example.manufacture.ui.dialog_fragment.ComponentDialog;
import com.example.manufacture.ui.dialog_fragment.DeleteDialog;

public class ComponentsFragment extends Fragment {

    public ComponentsFragment() {

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentComponentsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_components, container, false);
        View view = binding.getRoot();

        ComponentsViewModel componentsViewModel = new ViewModelProvider(getActivity()).get(ComponentsViewModel.class);
        binding.setLifecycleOwner(this);

        RecyclerView mRecyclerView = binding.componentRecyclerView;

        ComponentAdapter mAdapter = new ComponentAdapter();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        componentsViewModel.getAllComponents().observe(getActivity(), components -> {
            Log.i("TAG", "sadbugs: 1");
            if (components.isEmpty())
                binding.emptyComponents.setVisibility(View.VISIBLE);
            else binding.emptyComponents.setVisibility(View.GONE);
            mAdapter.setList(components);
        });

        mAdapter.setOnItemClickListener(component -> {
            componentsViewModel.setComponent(component);
            new ComponentDialog().show(getFragmentManager(), "Update_Component");
        });

        mAdapter.setOnItemLongClickListener(component -> {
            componentsViewModel.setComponent(component);
            new DeleteDialog().show(getFragmentManager(), "Delete_Component_Dialog");
        });


        binding.searchBar.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            String query = binding.searchBar.getText().toString();
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mAdapter.getFilter().filter(query);
                return true;
            }
            return false;
        });

        return view;
    }
}