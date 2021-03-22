package com.example.manufacture.repository;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.manufacture.data.ProductionDAO;
import com.example.manufacture.model.Production;
import com.example.manufacture.database.ProductionDatabase;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ProductionRepository {

    private final ProductionDAO productionDAO;
    private final LiveData<List<Production>> allProductions;

    public static final String SHARED_PREFS = "sharedPrefs";
    private static SharedPreferences sharedPreferences;

    public ProductionRepository(Application application) {

        ProductionDatabase productionDatabase = ProductionDatabase.getInstance(application);

        productionDAO = productionDatabase.productionDAO();

        allProductions = productionDAO.getAllProductions();

        sharedPreferences = application.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
    }

    public LiveData<List<Production>> getProductionsByProductId(int productId) {
        return productionDAO.getProductionsByProductId(productId);
    }

    public LiveData<List<Production>> getAllProductions() {
        return allProductions;
    }


    public int insert(Production production) {
        new InsertCustomerAsyncTask(productionDAO).execute(production);
        return sharedPreferences.getInt("ProductionRowID", 1);
    }

    public void update(Production production) {
        new UpdateCustomerAsyncTask(productionDAO).execute(production);
    }

    public void delete(Production production) {
        new DeleteCustomerAsyncTask(productionDAO).execute(production);
    }

    private static class InsertCustomerAsyncTask extends AsyncTask<Production, Void, Void> {
        private final ProductionDAO productionDAO;

        public InsertCustomerAsyncTask(ProductionDAO productionDAO) {
            this.productionDAO = productionDAO;
        }

        @Override
        protected Void doInBackground(Production... productions) {
            long productionRowID = productionDAO.insert(productions[0]);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("ProductionRowID", (int) productionRowID + 1);
            editor.apply();
            return null;
        }
    }

    private static class UpdateCustomerAsyncTask extends AsyncTask<Production, Void, Void> {
        private final ProductionDAO productionDAO;

        public UpdateCustomerAsyncTask(ProductionDAO productionDAO) {
            this.productionDAO = productionDAO;
        }

        @Override
        protected Void doInBackground(Production... productions) {
            productionDAO.update(productions[0]);
            return null;
        }
    }

    private static class DeleteCustomerAsyncTask extends AsyncTask<Production, Void, Void> {
        private final ProductionDAO productionDAO;

        public DeleteCustomerAsyncTask(ProductionDAO productionDAO) {
            this.productionDAO = productionDAO;
        }

        @Override
        protected Void doInBackground(Production... productions) {
            productionDAO.delete(productions[0]);
            return null;
        }
    }
}