package com.example.manufacture.model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.manufacture.data.ComponentDAO;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Observable;

public class ComponentRepository {

    private final ComponentDAO componentDAO;
    private final LiveData<List<Component>> allComponents;
    private static Component component = new Component("", "", "", "");
    private static final String TAG = "ComponentRepository";

    public ComponentRepository(Application application) {

        ComponentDatabase componentDatabase = ComponentDatabase.getInstance(application);

        componentDAO = componentDatabase.componentDAO();

        allComponents = componentDAO.getAllComponents();
    }

    public void insert(Component component) {
        new InsertComponentAsyncTask(componentDAO).execute(component);
    }

    public void update(Component component) {
        new UpdateComponentAsyncTask(componentDAO).execute(component);
    }

    public void delete(Component component) {
        new DeleteComponentAsyncTask(componentDAO).execute(component);
    }

    public Observable<Component> getComponent(String componentName) {
//        new QueryComponentAsyncTask(componentDAO).execute(componentName);
        return componentDAO.get(componentName);
    }

//    private static class QueryComponentAsyncTask extends AsyncTask<String, Void, Void> {
//        private final ComponentDAO componentDAO;
//
//        public QueryComponentAsyncTask(ComponentDAO componentDAO) {
//            this.componentDAO = componentDAO;
//        }
//
//        @Override
//        protected Void doInBackground(String... strings) {
//            component = componentDAO.get(strings[0]);
//            return null;
//        }
//    }

    public LiveData<List<Component>> getAllComponents() {
        return allComponents;
    }

    private static class InsertComponentAsyncTask extends AsyncTask<Component, Void, Void> {
        private final ComponentDAO componentDAO;

        public InsertComponentAsyncTask(ComponentDAO componentDAO) {
            this.componentDAO = componentDAO;
        }

        @Override
        protected Void doInBackground(Component... components) {
            componentDAO.insert(components[0]);
            return null;
        }
    }

    private static class UpdateComponentAsyncTask extends AsyncTask<Component, Void, Void> {
        private final ComponentDAO componentDAO;

        public UpdateComponentAsyncTask(ComponentDAO componentDAO) {
            this.componentDAO = componentDAO;
        }

        @Override
        protected Void doInBackground(Component... components) {
            componentDAO.update(components[0]);
            return null;
        }
    }

    private static class DeleteComponentAsyncTask extends AsyncTask<Component, Void, Void> {
        private final ComponentDAO componentDAO;

        public DeleteComponentAsyncTask(ComponentDAO componentDAO) {
            this.componentDAO = componentDAO;
        }

        @Override
        protected Void doInBackground(Component... components) {
            componentDAO.delete(components[0]);
            return null;
        }
    }
}