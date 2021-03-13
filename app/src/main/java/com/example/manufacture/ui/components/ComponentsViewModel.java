package com.example.manufacture.ui.components;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.manufacture.model.Component;
import com.example.manufacture.model.ComponentRepository;

import java.util.List;

public class ComponentsViewModel extends AndroidViewModel {

    private final ComponentRepository repository;
    private final LiveData<List<Component>> allComponents;

    private Component component;

    public ComponentsViewModel(@NonNull Application application) {
        super(application);
        repository = new ComponentRepository(application);
        allComponents = repository.getAllComponents();
    }

    public void insert(Component component) {
        repository.insert(component);
    }

    public void update(Component component) {
        repository.update(component);
    }

    public void delete(Component component) {
        repository.delete(component);
    }

    public LiveData<List<Component>> getAllComponents() {
        return allComponents;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }
}