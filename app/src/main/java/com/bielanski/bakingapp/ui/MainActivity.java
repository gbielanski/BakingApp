package com.bielanski.bakingapp.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.bielanski.bakingapp.R;
import com.bielanski.bakingapp.RecipesAdapter;
import com.bielanski.bakingapp.SimpleIdlingResource;
import com.bielanski.bakingapp.data.Recipe;
import com.bielanski.bakingapp.data.database.RecipeDao;
import com.bielanski.bakingapp.data.database.RecipesDatabase;
import com.bielanski.bakingapp.network.RequestInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements RecipesAdapter.OnClickRecipeHandler {
    public static final String TAG = "MainActivity";

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    public static final int TABLET_SMALLEST_WIDGHT = 600;
    private ArrayList<Recipe> data;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.progress_bar)ProgressBar progressBar;
    private RecipesAdapter adapter;
    public static final String RECIPE_ID = "RECIPE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        recyclerView.setHasFixedSize(true);
        Log.d(TAG, "Lifecycle onCreate ");
        Log.d(TAG, "MainActivity screenWidthDp " + getResources().getConfiguration().smallestScreenWidthDp);

        if (getResources().getConfiguration().smallestScreenWidthDp >= TABLET_SMALLEST_WIDGHT &&
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
            recyclerView.setLayoutManager(layoutManager);
        }else{
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);

        }
        adapter = new RecipesAdapter(new ArrayList<Recipe>(), this);
        recyclerView.setAdapter(adapter);
        loadJSON();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "Lifecycle onResume ");
        super.onResume();
    }

    private void loadJSON() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://go.udacity.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<List<Recipe>> call = request.getJSON();

        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                List<Recipe> jsonResponse = response.body();
                if (jsonResponse != null) {

                    data = new ArrayList<>(jsonResponse);
                    Thread t = new Thread() {
                        public void run() {
                            mIdlingResource.setIdleState(true);
                            RecipesDatabase database = RecipesDatabase.getInstance(getApplicationContext());
                            RecipeDao recipeDao = database.recipeDao();
                            Recipe[] recipes = data.toArray(new Recipe[data.size()]);
                            recipeDao.bulkInsert(recipes);
                        }
                    };

                    t.start();
                    adapter.addRecipeData(data);
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    mIdlingResource.setIdleState(false);
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

    @Override
    public void recipeOnClick(int position) {
        Log.v(TAG, "recipeOnClick");
        Recipe recipe = adapter.getRecipes().get(position);
        int id = recipe.getId();
        Intent intent = new Intent(this, StepsActivity.class);
        intent.putExtra(RECIPE_ID, id);
        startActivity(intent);
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}
