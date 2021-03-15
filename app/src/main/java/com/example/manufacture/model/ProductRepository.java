package com.example.manufacture.model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.manufacture.data.ProductDAO;

import java.util.List;

public class ProductRepository {

    private final ProductDAO productDAO;
    private final LiveData<List<Product>> allProducts;

    public ProductRepository(Application application) {

        ProductDatabase productDatabase = ProductDatabase.getInstance(application);

        productDAO = productDatabase.productDAO();

        allProducts = productDAO.getAllProducts();
    }

    public void insert(Product product) {
        new InsertCustomerAsyncTask(productDAO).execute(product);
    }

    public void update(Product product) {
        new UpdateCustomerAsyncTask(productDAO).execute(product);
    }

    public void delete(Product product) {
        new DeleteCustomerAsyncTask(productDAO).execute(product);
    }

    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    private static class InsertCustomerAsyncTask extends AsyncTask<Product, Void, Void> {
        private ProductDAO productDAO;

        public InsertCustomerAsyncTask(ProductDAO productDAO) {
            this.productDAO = productDAO;
        }

        @Override
        protected Void doInBackground(Product... products) {
            productDAO.insert(products[0]);
            return null;
        }
    }

    private static class UpdateCustomerAsyncTask extends AsyncTask<Product, Void, Void> {
        private ProductDAO productDAO;

        public UpdateCustomerAsyncTask(ProductDAO productDAO) {
            this.productDAO = productDAO;
        }

        @Override
        protected Void doInBackground(Product... products) {
            productDAO.update(products[0]);
            return null;
        }
    }

    private static class DeleteCustomerAsyncTask extends AsyncTask<Product, Void, Void> {
        private ProductDAO productDAO;

        public DeleteCustomerAsyncTask(ProductDAO productDAO) {
            this.productDAO = productDAO;
        }

        @Override
        protected Void doInBackground(Product... products) {
            productDAO.delete(products[0]);
            return null;
        }
    }
}