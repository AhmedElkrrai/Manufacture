package com.example.manufacture.ui.dashboard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.manufacture.model.Product;
import com.example.manufacture.model.Production;
import com.example.manufacture.repository.ProductionRepository;

import java.util.List;

public class ProductionViewModel extends AndroidViewModel {

    private final ProductionRepository repository;
    private final LiveData<List<Production>> allProductions;

    private Product product;
    private Production production;

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

    public LiveData<List<Production>> getProductionsByProductId(int productId) {
        return repository.getProductionsByProductId(productId);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Production getProduction() {
        return production;
    }

    public void setProduction(Production production) {
        this.production = production;
    }
}