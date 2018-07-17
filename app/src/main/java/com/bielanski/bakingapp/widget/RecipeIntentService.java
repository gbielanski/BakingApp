package com.bielanski.bakingapp.widget;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bielanski.bakingapp.R;
import com.bielanski.bakingapp.data.Ingredients;
import com.bielanski.bakingapp.data.PrefUtils;
import com.bielanski.bakingapp.data.Recipe;
import com.bielanski.bakingapp.data.database.RecipesDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.bielanski.bakingapp.data.PrefUtils.RECIPE_ID_NOT_SET;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class RecipeIntentService extends RemoteViewsService {
    final static String TAG = "RecipeIntentService";


    public static final String ACTION_FETCH_RECIPES = "com.bielanski.bakingapp.action.FETCH_RECIPES";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            ArrayList<Ingredients> mIngredients = null;

            @Override
            public void onCreate() {
                // Nothing to do
            }

            @Override
            public void onDataSetChanged() {
                Log.v(TAG, "onDataSetChanged");
                final long identityToken = Binder.clearCallingIdentity();

                List<Recipe> allRecipes = RecipesDatabase.getInstance(RecipeIntentService.this).recipeDao().getAllRecipes();
                final int recipeId = PrefUtils.getRecipe(getApplicationContext());
                if(recipeId  != RECIPE_ID_NOT_SET) {
                    Recipe recipe = allRecipes.get(recipeId-1);
                    mIngredients = recipe.getIngredients();
                }

                Binder.restoreCallingIdentity(identityToken);

            }

            @Override
            public void onDestroy() {
                Log.v(TAG, "onDestroy");

                mIngredients = null;
            }

            @Override
            public int getCount() {
                int count = mIngredients == null ? 0 : mIngredients.size();
                Log.v(TAG, "getCount " + count);

                return count;
            }

            @Override
            public RemoteViews getViewAt(int position) {
                Log.v(TAG, "getViewAt");

                if (mIngredients == null || position >= mIngredients.size())
                    return null;

                RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_ingredients_list_item);
                String shortDescription = mIngredients.get(position).getIngredient() + " "
                        + mIngredients.get(position).getQuantity() + " " + mIngredients.get(position).getMeasure();
                Log.v(TAG, "getViewAt " + shortDescription);
                views.setTextViewText(R.id.ingredient, shortDescription);
                Log.v(TAG, "RemoteViews getViewAt position " + position + " " + shortDescription);

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                Log.v(TAG, "getLoadingView");

                return new RemoteViews(getPackageName(), R.layout.widget_ingredients_list_item);
            }

            @Override
            public int getViewTypeCount() {
                Log.v(TAG, "getViewTypeCount");

                return 1;
            }

            @Override
            public long getItemId(int position) {
                Log.v(TAG, "getItemId position " + position);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                Log.v(TAG, "hasStableIds");

                return true;
            }
        };
    }
}
