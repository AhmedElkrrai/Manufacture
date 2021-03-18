package com.example.manufacture.repository;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.manufacture.data.ProductDAO;
import com.example.manufacture.database.ProductDatabase;
import com.example.manufacture.model.Product;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ProductRepository {

    private final ProductDAO productDAO;
    private final LiveData<List<Product>> allProducts;

    public static final String SHARED_PREFS = "sharedPrefs";
    private static SharedPreferences sharedPreferences;

    public ProductRepository(Application application) {

        ProductDatabase productDatabase = ProductDatabase.getInstance(application);

        productDAO = productDatabase.productDAO();

        allProducts = productDAO.getAllProducts();
        sharedPreferences = application.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
    }

    public LiveData<Product> getProductById(int id) {
        return productDAO.getProductById(id);
    }

    public int insert(Product product) {
        new InsertCustomerAsyncTask(productDAO).execute(product);
        return sharedPreferences.getInt("productRowID", 1);
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
        private final ProductDAO productDAO;

        public InsertCustomerAsyncTask(ProductDAO productDAO) {
            this.productDAO = productDAO;
        }

        @Override
        protected Void doInBackground(Product... products) {
            long productRowID = productDAO.insert(products[0]);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("productRowID", (int) productRowID + 1);
            editor.apply();
            return null;
        }
    }

    private static class UpdateCustomerAsyncTask extends AsyncTask<Product, Void, Void> {
        private final ProductDAO productDAO;

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
        private final ProductDAO productDAO;

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