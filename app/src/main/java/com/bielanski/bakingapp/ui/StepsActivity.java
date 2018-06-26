package com.bielanski.bakingapp.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bielanski.bakingapp.R;
import com.bielanski.bakingapp.RecipeAsyncTaskLoader;
import com.bielanski.bakingapp.StepsAdapter;
import com.bielanski.bakingapp.data.Recipe;
import com.bielanski.bakingapp.data.database.RecipesDatabase;


import java.util.List;

import static com.bielanski.bakingapp.ui.MainActivity.*;

public class StepsActivity extends AppCompatActivity implements  StepsAdapter.OnClickStepHandler, LoaderManager.LoaderCallbacks<List<Recipe>>{

    public static final String STEP_ID = "STEP_ID";
    public static final int STEPS_LOADER_ID = 123;
    private int mRecipeId;
    List<Recipe> mListOfRecipes;

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

    public void onClickNextButton(View view){
        //TODO Calculate this correctly
        mRecipeId++;
        replaceFragment();
    }
    public void onClickPrevButton(View view){
        //TODO Calculate this correctly
        mRecipeId--;
        replaceFragment();
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
        RecipeAsyncTaskLoader recipeAsyncTaskLoader = new RecipeAsyncTaskLoader(this);
        RecipesDatabase database = RecipesDatabase.getInstance(StepsActivity.this);
        recipeAsyncTaskLoader.setDatabase(database);
        return recipeAsyncTaskLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Recipe>> loader, List<Recipe> listOfRecipes) {

        Log.v(TAG, "onLoadFinished " + listOfRecipes);
        mListOfRecipes = listOfRecipes;
        replaceFragment();
    }

    private void replaceFragment() {
        StepsFragment stepsFragment = new StepsFragment();
        stepsFragment.setRecipes(mListOfRecipes);
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
