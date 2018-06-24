package com.bielanski.bakingapp.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.bielanski.bakingapp.R;
import com.bielanski.bakingapp.data.Recipe;
import com.bielanski.bakingapp.data.database.RecipeDao;
import com.bielanski.bakingapp.data.database.RecipesDatabase;

import java.util.List;

import static com.bielanski.bakingapp.ui.MainActivity.*;

public class StepsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Recipe>{

    private static final String TAG = "StepsActivity";
    public static final int LOADER_ID = 123;
    private int idExtra;
    private Context mCo = this;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        Intent intent = getIntent();
        if(intent != null){
            idExtra = intent.getIntExtra(RECIPE_ID, 0);
            Log.v(TAG, "id " + idExtra);

            Bundle bundle = new Bundle();
            bundle.putInt(RECIPE_ID, idExtra);
            getSupportLoaderManager().initLoader(LOADER_ID, bundle, this);
        }
    }

    static class StepsAsyncLoader extends AsyncTaskLoader<Recipe> {
        @SuppressLint("StaticFieldLeak")
        static Context mContext;
        static int mRecipeId;

        public StepsAsyncLoader(@NonNull Context context, int recipeId) {
            super(context);
            mContext = context;
            mRecipeId = recipeId;
        }

        @Override
        public Recipe loadInBackground() {
            RecipesDatabase database = RecipesDatabase.getInstance(mContext);
            RecipeDao recipeDao = database.recipeDao();
            List<Recipe> recipes = recipeDao.getRecipesWithId(mRecipeId);
            return recipes.get(0);
        }

        @Override
        protected void onStartLoading() {
            //Think of this as AsyncTask onPreExecute() method,you can start your progress bar,and at the end call forceLoad();
            forceLoad();

        }
    };

    @NonNull
    @Override
    public Loader<Recipe> onCreateLoader(int id, @Nullable Bundle args) {
        return new StepsAsyncLoader (this, args.getInt("RECIPE_ID"));
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Recipe> loader, Recipe data) {
        Log.v(TAG, "onLoadFinished " + data);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Recipe> loader) {

    }
}
