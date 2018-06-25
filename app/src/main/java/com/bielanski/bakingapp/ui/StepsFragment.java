package com.bielanski.bakingapp.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bielanski.bakingapp.R;
import com.bielanski.bakingapp.StepsAdapter;
import com.bielanski.bakingapp.data.Recipe;
import com.bielanski.bakingapp.data.Step;
import com.bielanski.bakingapp.data.database.RecipeDao;
import com.bielanski.bakingapp.data.database.RecipesDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.bielanski.bakingapp.ui.MainActivity.RECIPE_ID;

public class StepsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Recipe>, StepsAdapter.OnClickStepHandler{
    private static final String TAG = "StepsFragment";
    public static final int LOADER_ID = 123;
    private int mIdExtra;
    private StepsAdapter mStepsAdapter;
    private RecyclerView recyclerView;

    public StepsFragment() {
    }

    public void setIdExtra(int idExtra){
        mIdExtra = idExtra;
        Bundle bundle = new Bundle();
        bundle.putInt(RECIPE_ID, idExtra);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, bundle, this);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_steps, container, false);

        recyclerView = rootView.findViewById(R.id.steps_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        mStepsAdapter = new StepsAdapter( new ArrayList<Step>(), this);
        recyclerView.setAdapter(mStepsAdapter);

        return rootView;
    }

    @Override
    public void stepOnClick(int position) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v(TAG, "OnCreate " + mIdExtra);
        super.onCreate(savedInstanceState);
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
        return new StepsAsyncLoader (getActivity(), args.getInt("RECIPE_ID"));
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Recipe> loader, Recipe data) {

        Log.v(TAG, "onLoadFinished " + data);
        mStepsAdapter.addStepsData(data.getSteps());


    }

    @Override
    public void onLoaderReset(@NonNull Loader<Recipe> loader) {

    }
}
