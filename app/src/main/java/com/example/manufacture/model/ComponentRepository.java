package com.example.manufacture.model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.manufacture.data.ComponentDAO;

import java.util.List;

public class ComponentRepository {

    private ComponentDAO componentDAO;
    private LiveData<List<Component>> allComponents;

    public ComponentRepository(Application application) {

        ComponentDatabase componentDatabase = ComponentDatabase.getInstance(application);

        componentDAO = componentDatabase.componentDAO();

        allComponents = componentDAO.getAllComponents();
    }

    public void insert(Component component) {
        new InsertCustomerAsyncTask(componentDAO).execute(component);
    }

    public void update(Component component) {
        new UpdateCustomerAsyncTask(componentDAO).execute(component);
    }

    public void delete(Component component) {
        new DeleteCustomerAsyncTask(componentDAO).execute(component);
    }

    public LiveData<List<Component>> getAllComponents() {
        return allComponents;
    }

    private static class InsertCustomerAsyncTask extends AsyncTask<Component, Void, Void> {
        private ComponentDAO componentDAO;

        public InsertCustomerAsyncTask(ComponentDAO componentDAO) {
            this.componentDAO = componentDAO;
        }

        @Override
        protected Void doInBackground(Component... components) {
            componentDAO.insert(components[0]);
            return null;
        }
    }

    private static class UpdateCustomerAsyncTask extends AsyncTask<Component, Void, Void> {
        private ComponentDAO componentDAO;

        public UpdateCustomerAsyncTask(ComponentDAO componentDAO) {
            this.componentDAO = componentDAO;
        }

        @Override
        protected Void doInBackground(Component... components) {
            componentDAO.update(components[0]);
            return null;
        }
    }

    private static class DeleteCustomerAsyncTask extends AsyncTask<Component, Void, Void> {
        private ComponentDAO componentDAO;

        public DeleteCustomerAsyncTask(ComponentDAO componentDAO) {
            this.componentDAO = componentDAO;
        }

        @Override
        protected Void doInBackground(Component... components) {
            componentDAO.delete(components[0]);
            return null;
        }
    }
}