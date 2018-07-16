package com.bielanski.bakingapp.widget;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bielanski.bakingapp.R;
import com.bielanski.bakingapp.data.Recipe;
import com.bielanski.bakingapp.data.Step;
import com.bielanski.bakingapp.data.database.RecipesDatabase;

import java.util.ArrayList;
import java.util.List;

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
        return new RemoteViewsFactory(){
            ArrayList<Step> mSteps;
            @Override
            public void onCreate() {
                // Nothing to do
            }

            @Override
            public void onDataSetChanged() {
                Log.v(TAG, "onDataSetChanged");
                final long identityToken = Binder.clearCallingIdentity();

                List<Recipe> allRecipes = RecipesDatabase.getInstance(RecipeIntentService.this).recipeDao().getAllRecipes();
                Recipe recipe = allRecipes.get(0);
                mSteps = recipe.getSteps();

                for(Step s : mSteps)
                    Log.v(TAG, "Step : " + s.getShortDescription());

                Binder.restoreCallingIdentity(identityToken);

            }

            @Override
            public void onDestroy() {
                Log.v(TAG, "onDestroy");

                mSteps = null;
            }

            @Override
            public int getCount() {
                int count = mSteps == null ? 0 : mSteps.size();
                Log.v(TAG, "getCount " + count);

                return count;
            }

            @Override
            public RemoteViews getViewAt(int position) {
                Log.v(TAG, "getViewAt");

                if(mSteps == null || position >=  mSteps.size())
                    return null;

                RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_step_list_item);
                String shortDescription = mSteps.get(position).getShortDescription();
                Log.v(TAG, "getViewAt " + shortDescription);
                views.setTextViewText(R.id.step, shortDescription);
                Log.v(TAG, "RemoteViews getViewAt position " + position + " step" + shortDescription);

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                Log.v(TAG, "getLoadingView");

                return new RemoteViews(getPackageName(), R.layout.widget_step_list_item);
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

    //private static final String EXTRA_PARAM1 = "com.bielanski.bakingapp.extra.PARAM1";
    //private static final String EXTRA_PARAM2 = "com.bielanski.bakingapp.extra.PARAM2";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
//    public static void startActionFetchRecipes(Context context, String param1, String param2) {
//        Intent intent = new Intent(context, RecipeIntentService.class);
//        intent.setAction(ACTION_FETCH_RECIPES);
//        //intent.putExtra(EXTRA_PARAM1, param1);
//        //intent.putExtra(EXTRA_PARAM2, param2);
//        context.startService(intent);
//    }


//    @Override
//    protected void onHandleIntent(Intent intent) {
//        if (intent != null) {
//            final String action = intent.getAction();
//            if (ACTION_FETCH_RECIPES.equals(action)) {
// //               final String param1 = intent.getStringExtra(EXTRA_PARAM1);
// //               final String param2 = intent.getStringExtra(EXTRA_PARAM2);
//                handleActionFetchRecipes();
//            }
//        }
//    }

//    private void handleActionFetchRecipes() {
//        List<Recipe> allRecipes = RecipesDatabase.getInstance(this).recipeDao().getAllRecipes();
//
//        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
//        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
//        //Now update all widgets
//    }
}
