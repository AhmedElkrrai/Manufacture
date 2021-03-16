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

    public boolean contains(String componentName) {
        return repository.contains(componentName);
    }

    public int insert(Component component) {
        return repository.insert(component);
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

    public LiveData<Component> getComponentById(int id) {
        return repository.getComponentById(id);
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }
}