package com.example.manufacture.model;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.manufacture.data.ComponentDAO;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ComponentRepository {

    private final ComponentDAO componentDAO;
    private final LiveData<List<Component>> allComponents;

    public static final String SHARED_PREFS = "sharedPrefs";
    private static SharedPreferences sharedPreferences;

    private static boolean ass;

    public ComponentRepository(Application application) {

        ComponentDatabase componentDatabase = ComponentDatabase.getInstance(application);

        componentDAO = componentDatabase.componentDAO();

        allComponents = componentDAO.getAllComponents();

        sharedPreferences = application.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
    }

    public Boolean contains(String componentName) {
        new ContainsComponentAsyncTask(componentDAO).execute(componentName);
        Log.i("TAG", "sadbugs contains: " + ass);
        return ass;
    }

    private static class ContainsComponentAsyncTask extends AsyncTask<String, Boolean, Boolean> {
        private final ComponentDAO componentDAO;

        public ContainsComponentAsyncTask(ComponentDAO componentDAO) {
            this.componentDAO = componentDAO;
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            return componentDAO.contains(strings[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            ass = aBoolean;
        }
    }

    public LiveData<Component> getComponentById(int id) {
        return componentDAO.getComponentById(id);
    }

    public int insert(Component component) {
        new InsertComponentAsyncTask(componentDAO).execute(component);
        return sharedPreferences.getInt("rowID", 1);
    }

    public void update(Component component) {
        new UpdateComponentAsyncTask(componentDAO).execute(component);
    }

    public void delete(Component component) {
        new DeleteComponentAsyncTask(componentDAO).execute(component);
    }

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
            long rowID = componentDAO.insert(components[0]);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("rowID", (int) rowID + 1);
            editor.apply();

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