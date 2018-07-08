package com.bielanski.bakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.bielanski.bakingapp.data.Recipe;
import com.bielanski.bakingapp.data.database.RecipesDatabase;

import java.util.List;

import static com.bielanski.bakingapp.ui.StepsActivity.STEPS_LOADER_ID;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class RecipeIntentService extends IntentService {


    public static final String ACTION_FETCH_RECIPES = "com.bielanski.bakingapp.action.FETCH_RECIPES";

    //private static final String EXTRA_PARAM1 = "com.bielanski.bakingapp.extra.PARAM1";
    //private static final String EXTRA_PARAM2 = "com.bielanski.bakingapp.extra.PARAM2";

    public RecipeIntentService() {
        super("RecipeIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionFetchRecipes(Context context, String param1, String param2) {
        Intent intent = new Intent(context, RecipeIntentService.class);
        intent.setAction(ACTION_FETCH_RECIPES);
        //intent.putExtra(EXTRA_PARAM1, param1);
        //intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FETCH_RECIPES.equals(action)) {
 //               final String param1 = intent.getStringExtra(EXTRA_PARAM1);
 //               final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFetchRecipes();
            }
        }
    }

    private void handleActionFetchRecipes() {
        List<Recipe> allRecipes = RecipesDatabase.getInstance(this).recipeDao().getAllRecipes();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
        //Now update all widgets
        RecipeWidgetProvider.updateRecipeWidgets(this, appWidgetManager,appWidgetIds, allRecipes);
    }
}
