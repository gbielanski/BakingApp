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
import com.bielanski.bakingapp.data.PrefUtils;
import com.bielanski.bakingapp.data.Recipe;
import com.bielanski.bakingapp.data.Step;
import com.bielanski.bakingapp.data.database.RecipesDatabase;
import com.bielanski.bakingapp.widget.RecipeWidgetProvider;


import java.util.ArrayList;
import java.util.List;

import static com.bielanski.bakingapp.ui.MainActivity.*;

public class StepsActivity extends AppCompatActivity implements StepsAdapter.OnClickStepHandler, LoaderManager.LoaderCallbacks<List<Recipe>> {

    public static final String STEP_ID = "STEP_ID";
    public static final int STEPS_LOADER_ID = 123;
    private int mRecipeId;
    List<Recipe> mListOfRecipes;
    boolean isTabletLandscape = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        if (findViewById(R.id.video_container) != null)
            isTabletLandscape = true;

        Intent intent = getIntent();
        if (intent != null) {
            mRecipeId = intent.getIntExtra(RECIPE_ID, 0);
            Log.v(TAG, "id " + mRecipeId);
        } else if (savedInstanceState != null) {
            mRecipeId = savedInstanceState.getInt(RECIPE_ID);
        }

        getSupportLoaderManager().initLoader(STEPS_LOADER_ID, null, this);

    }

    public void onClickNextButton(View view) {
        nextRecipe();
        replaceFragment();
        if (isTabletLandscape)
            stepOnClick(0);
    }

    private void nextRecipe() {
        int i = 0;
        for (; i < mListOfRecipes.size(); i++) {
            if (mListOfRecipes.get(i).getId() == mRecipeId)
                break;
        }

        if (i == mListOfRecipes.size() - 1)
            mRecipeId = mListOfRecipes.get(0).getId();
        else
            mRecipeId = mListOfRecipes.get(i + 1).getId();

    }

    private void prevRecipe() {
        int i = 0;
        for (; i < mListOfRecipes.size(); i++) {
            if (mListOfRecipes.get(i).getId() == mRecipeId)
                break;
        }

        if (i == 0)
            mRecipeId = mListOfRecipes.get(mListOfRecipes.size() - 1).getId();
        else
            mRecipeId = mListOfRecipes.get(0).getId();

    }

    public void onClickPrevButton(View view) {
        prevRecipe();
        replaceFragment();
        if (isTabletLandscape)
            stepOnClick(0);
    }

    @Override
    public void stepOnClick(int position) {

        if (!isTabletLandscape) {
            Intent intent = new Intent(this, StepDetailsActivity.class);
            intent.putExtra(RECIPE_ID, mRecipeId);
            intent.putExtra(STEP_ID, position);
            startActivity(intent);
        } else {
            InstructionsFragment instructionsFragment = new InstructionsFragment();
            VideoFragment videoFragment = new VideoFragment();
            videoFragment.setRecipes(mListOfRecipes);
            videoFragment.setRecipeNumber(mRecipeId);
            videoFragment.setStepNumber(position);
            instructionsFragment.setRecipes(mListOfRecipes);
            instructionsFragment.setRecipeNumber(mRecipeId);
            instructionsFragment.setStepNumber(position);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.video_container, videoFragment)
                    .replace(R.id.instructions_container, instructionsFragment)
                    .commit();
        }
    }

    public void onClickWidget(View view) {
        PrefUtils.addRecipe(this, mRecipeId);
        Intent dataUpdatedIntent = new Intent(RecipeWidgetProvider.ACTION_DATA_UPDATED);
        sendBroadcast(dataUpdatedIntent);
    }

    @NonNull
    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, @Nullable Bundle args) {
        RecipeAsyncTaskLoader recipeAsyncTaskLoader = new RecipeAsyncTaskLoader(this);
        RecipesDatabase database = RecipesDatabase.getInstance(getApplicationContext());
        recipeAsyncTaskLoader.setDatabase(database);
        return recipeAsyncTaskLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Recipe>> loader, List<Recipe> listOfRecipes) {

        Log.v(TAG, "onLoadFinished " + listOfRecipes);
        mListOfRecipes = listOfRecipes;
        replaceFragment();
        if (isTabletLandscape)
            stepOnClick(0);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(RECIPE_ID, mRecipeId);
        super.onSaveInstanceState(outState);
    }
}
