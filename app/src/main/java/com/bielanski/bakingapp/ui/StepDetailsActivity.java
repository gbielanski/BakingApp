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
import android.view.View;
import android.widget.Button;

import com.bielanski.bakingapp.R;
import com.bielanski.bakingapp.RecipeAsyncTaskLoader;
import com.bielanski.bakingapp.data.Recipe;
import com.bielanski.bakingapp.data.database.RecipeDao;
import com.bielanski.bakingapp.data.database.RecipesDatabase;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bielanski.bakingapp.ui.MainActivity.RECIPE_ID;
import static com.bielanski.bakingapp.ui.MainActivity.TAG;
import static com.bielanski.bakingapp.ui.StepsActivity.STEP_ID;

public class StepDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Recipe>> {
    public static final String RECIPE_ID_KEY = "RECIPE_ID_KEY";
    public static final String STEP_ID_KEY = "STEP_ID_KEY";
    private int mRecipeId;
    private int mStepId;
    public static final int STEP_DETAILS_LOADER_ID = 234;
    private List<Recipe> mListOfRecipes;
    boolean mIsPhoneLandscapeMode = false;
    @BindView(R.id.detail_next) Button buttonNext;
    @BindView(R.id.detail_previous) Button buttonPrevious;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_details);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent != null) {
            mRecipeId = intent.getIntExtra(RECIPE_ID, 0);
            mStepId = intent.getIntExtra(STEP_ID, 0);
            Log.v(TAG, "id " + mRecipeId);
        }

        if(savedInstanceState != null){
            mRecipeId = savedInstanceState.getInt(RECIPE_ID_KEY);
            mStepId = savedInstanceState.getInt(STEP_ID_KEY);

        }

        getSupportLoaderManager().initLoader(STEP_DETAILS_LOADER_ID, null, this);

        if (findViewById(R.id.instructions_container) == null)
            mIsPhoneLandscapeMode = true;


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
        mListOfRecipes = listOfRecipes;
        replaceFragments();
        setUpButtons();

    }

    private void setUpButtons() {

        if(mIsPhoneLandscapeMode)
            return;

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Manage this

                mStepId++;
                replaceFragments();

            }
        });

        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Manage this
                mStepId--;
                replaceFragments();
            }
        });
    }

    public void replaceFragments() {
        InstructionsFragment instructionsFragment = new InstructionsFragment();
        VideoFragment videoFragment = new VideoFragment();
        videoFragment.setRecipes(mListOfRecipes);
        videoFragment.setRecipeNumber(mRecipeId);
        videoFragment.setStepNumber(mStepId);

        if (mIsPhoneLandscapeMode) {
            instructionsFragment.setRecipes(mListOfRecipes);
            instructionsFragment.setRecipeNumber(mRecipeId);
            instructionsFragment.setStepNumber(mStepId);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.video_container, videoFragment)
                    .commit();
        }else{
            instructionsFragment.setRecipes(mListOfRecipes);
            instructionsFragment.setRecipeNumber(mRecipeId);
            instructionsFragment.setStepNumber(mStepId);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.video_container, videoFragment)
                    .replace(R.id.instructions_container, instructionsFragment)
                    .commit();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Recipe>> loader) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(RECIPE_ID_KEY, mRecipeId);
        outState.putInt(STEP_ID_KEY, mStepId);
        super.onSaveInstanceState(outState);
    }
}
