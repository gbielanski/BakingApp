package com.bielanski.bakingapp.ui;

import android.content.Context;
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
import com.bielanski.bakingapp.RecipeAsyncTaskLoader;
import com.bielanski.bakingapp.data.Recipe;
import com.bielanski.bakingapp.data.database.RecipeDao;
import com.bielanski.bakingapp.data.database.RecipesDatabase;

import java.util.List;

import static com.bielanski.bakingapp.ui.MainActivity.RECIPE_ID;
import static com.bielanski.bakingapp.ui.MainActivity.TAG;
import static com.bielanski.bakingapp.ui.StepsActivity.STEP_ID;

public class StepDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Recipe>> {
    private int mRecipeId;
    private int mStepId;
    public static final int STEP_DETAILS_LOADER_ID = 234;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_details);
        Intent intent = getIntent();
        if (intent != null) {
            mRecipeId = intent.getIntExtra(RECIPE_ID, 0);
            mStepId = intent.getIntExtra(STEP_ID, 0);
            Log.v(TAG, "id " + mRecipeId);

            getSupportLoaderManager().initLoader(STEP_DETAILS_LOADER_ID, null, this);
        }

    }

    @NonNull
    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, @Nullable Bundle args) {
        RecipeAsyncTaskLoader recipeAsyncTaskLoader = new RecipeAsyncTaskLoader(this);
        RecipesDatabase database = RecipesDatabase.getInstance(StepDetailsActivity.this);
        recipeAsyncTaskLoader.setDatabase(database);
        return recipeAsyncTaskLoader;

    }


    @Override
    public void onLoadFinished(@NonNull Loader<List<Recipe>> loader, List<Recipe> listOfRecipes) {
        InstructionsFragment instructionsFragment = new InstructionsFragment();
        VideoFragment videoFragment = new VideoFragment();
        instructionsFragment.setRecipes(listOfRecipes);
        instructionsFragment.setRecipeNumber(mRecipeId);
        instructionsFragment.setStepNumber(mStepId);
        videoFragment.setRecipes(listOfRecipes);
        videoFragment.setRecipeNumber(mRecipeId);
        videoFragment.setStepNumber(mStepId);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.video_container, videoFragment)
                .replace(R.id.instructions_container, instructionsFragment)
                .commit();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Recipe>> loader) {

    }
}
