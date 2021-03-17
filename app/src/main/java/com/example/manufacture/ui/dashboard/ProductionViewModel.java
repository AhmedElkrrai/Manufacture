package com.example.manufacture.ui.dashboard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.manufacture.model.Product;
import com.example.manufacture.model.ProductRepository;
import com.example.manufacture.model.Production;
import com.example.manufacture.model.ProductionRepository;

import java.util.List;

public class ProductionViewModel extends AndroidViewModel {

    private final ProductionRepository repository;
    private final LiveData<List<Production>> allProductions;

    private Product product;
    private String patchNumber;

    public ProductionViewModel(@NonNull Application application) {
        super(application);
        repository = new ProductionRepository(application);
        allProductions = repository.getAllProductions();
    }

    public int insert(Production production) {
        return repository.insert(production);
    }

    public void update(Production production) {
        repository.update(production);
    }

    public void delete(Production production) {
        repository.delete(production);
    }

    public LiveData<List<Production>> getAllProductions() {
        return allProductions;
    }

    public LiveData<Production> getProductionById(int id) {
        return repository.getProductionById(id);
    }

    public LiveData<List<Production>> getProductionsByProductId(int productId) {
        return repository.getProductionsByProductId(productId);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getPatchNumber() {
        return patchNumber;
    }

    public void setPatchNumber(String patchNumber) {
        this.patchNumber = patchNumber;
    }
}