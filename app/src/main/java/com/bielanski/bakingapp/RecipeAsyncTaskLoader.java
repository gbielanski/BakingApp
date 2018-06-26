package com.bielanski.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

import com.bielanski.bakingapp.data.Recipe;
import com.bielanski.bakingapp.data.database.RecipeDao;
import com.bielanski.bakingapp.data.database.RecipesDatabase;

import java.util.List;

public class RecipeAsyncTaskLoader extends AsyncTaskLoader<List<Recipe>> {
    private RecipesDatabase database;

    public RecipeAsyncTaskLoader(@NonNull Context context) {
        super(context);
    }

    @Override
    public List<Recipe> loadInBackground() {
        RecipeDao recipeDao = database.recipeDao();
        return recipeDao.getAllRecipes();
    }

    @Override
    protected void onStartLoading() {
        //Think of this as AsyncTask onPreExecute() method,you can start your progress bar,and at the end call forceLoad();
        forceLoad();
    }

    public void setDatabase(RecipesDatabase database) {
        this.database = database;
    }
}
