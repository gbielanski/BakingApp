package com.bielanski.bakingapp.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.bielanski.bakingapp.R;
import com.bielanski.bakingapp.RecipesAdapter;
import com.bielanski.bakingapp.data.Recipe;
import com.bielanski.bakingapp.data.database.RecipeDao;
import com.bielanski.bakingapp.data.database.RecipesDatabase;
import com.bielanski.bakingapp.network.RequestInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements RecipesAdapter.OnClickRecipeHandler {
    public static final String TAG = "MainActivity";
    private ArrayList<Recipe> data;
    private RecyclerView recyclerView;
    private RecipesAdapter adapter;
    public static final String RECIPE_ID = "RECIPE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecipesAdapter(new ArrayList<Recipe>(), this);
        recyclerView.setAdapter(adapter);
        loadJSON();
    }

    private void loadJSON() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://go.udacity.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<List<Recipe>> call = request.getJSON();

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                List<Recipe> jsonResponse = response.body();
                if (jsonResponse != null) {

                    data = new ArrayList<>(jsonResponse);
                    Thread t = new Thread() {
                        public void run() {
                                RecipesDatabase database = RecipesDatabase.getInstance(MainActivity.this);
                                RecipeDao recipeDao = database.recipeDao();
                                recipeDao.bulkInsert((Recipe[]) data.toArray(new Recipe[data.size()]));
                        }
                    };

                    t.start();
                    adapter.addRecipeData(data);
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
}
