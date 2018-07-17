package com.bielanski.bakingapp.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.bielanski.bakingapp.R;

public final class PrefUtils {
    public static final int RECIPE_ID_NOT_SET = 0;
    private static final String TAG = "PrefUtils";
    private PrefUtils() {
    }

    public static void addRecipe(Context context, int recipe) {
        Log.d(TAG, "addRecipe " + recipe);
        String key = context.getString(R.string.pref_recipe_key);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, recipe);
        editor.apply();
    }

    public static int getRecipe(Context context) {
        String key = context.getString(R.string.pref_recipe_key);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int recipeId = prefs.getInt(key, RECIPE_ID_NOT_SET);
        Log.d(TAG, "getRecipe " + recipeId);

        return recipeId;
    }
}
