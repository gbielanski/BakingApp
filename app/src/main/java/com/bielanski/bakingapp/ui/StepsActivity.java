package com.bielanski.bakingapp.ui;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;

import com.bielanski.bakingapp.R;
import com.bielanski.bakingapp.StepsAdapter;
import com.bielanski.bakingapp.data.Recipe;
import com.bielanski.bakingapp.data.database.RecipeDao;
import com.bielanski.bakingapp.data.database.RecipesDatabase;


import java.util.List;

import static com.bielanski.bakingapp.ui.MainActivity.*;

public class StepsActivity extends AppCompatActivity implements  StepsAdapter.OnClickStepHandler, LoaderManager.LoaderCallbacks<List<Recipe>>{

    public static final String STEP_ID = "STEP_ID";
    public static final int STEPS_LOADER_ID = 123;
    private int mRecipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        Intent intent = getIntent();
        if(intent != null){
            mRecipeId = intent.getIntExtra(RECIPE_ID, 0);
            Log.v(TAG, "id " + mRecipeId);

            getSupportLoaderManager().initLoader(STEPS_LOADER_ID, null, this);
        }
    }

    @Override
    public void stepOnClick(int position) {
        Intent intent = new Intent(this, StepDetailsActivity.class);
        intent.putExtra(RECIPE_ID, mRecipeId);
        intent.putExtra(STEP_ID, position);
        startActivity(intent);
    }

    @NonNull
    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, @Nullable Bundle args) {
        //return new StepsAsyncLoader (this, args.getInt("RECIPE_ID"));
        return new AsyncTaskLoader<List<Recipe>>(this) {
            @Override
            public List<Recipe> loadInBackground() {
                RecipesDatabase database = RecipesDatabase.getInstance(StepsActivity.this);
                RecipeDao recipeDao = database.recipeDao();
                List<Recipe> recipes = recipeDao.getAllRecipes();
                return recipes;
            }

            @Override
            protected void onStartLoading() {
                //Think of this as AsyncTask onPreExecute() method,you can start your progress bar,and at the end call forceLoad();
                forceLoad();
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Recipe>> loader, List<Recipe> listOfRecipes) {

        Log.v(TAG, "onLoadFinished " + listOfRecipes);
        StepsFragment stepsFragment = new StepsFragment();
        stepsFragment.setRecipes(listOfRecipes);
        stepsFragment.setRecipeNumber(mRecipeId);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.steps_container, stepsFragment)
                .commit();
    }


    @Override
    public void onLoaderReset(@NonNull Loader<List<Recipe>> loader) {

    }
}
