package com.example.manufacture.ui.components;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.manufacture.model.Component;
import com.example.manufacture.model.ComponentRepository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ComponentsViewModel extends AndroidViewModel {

    private final ComponentRepository repository;
    private final LiveData<List<Component>> allComponents;

    private Component component;
    public MutableLiveData<Component> componentMLD;

    public ComponentsViewModel(@NonNull Application application) {
        super(application);
        repository = new ComponentRepository(application);
        allComponents = repository.getAllComponents();
        componentMLD = new MutableLiveData<>();
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