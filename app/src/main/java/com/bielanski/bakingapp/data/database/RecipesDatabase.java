package com.bielanski.bakingapp.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

import com.bielanski.bakingapp.data.Recipe;

@Database(entities = {Recipe.class}, version = 1, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class RecipesDatabase extends RoomDatabase {
    private static final String LOG_TAG = RecipesDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static RecipesDatabase sInstance;
    private static final String DATABASE_NAME = "recipes";

    public static RecipesDatabase getInstance(Context context) {
        Log.d(LOG_TAG, "Getting the database");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        RecipesDatabase.class, RecipesDatabase.DATABASE_NAME).build();
                Log.d(LOG_TAG, "Made new database");
            }
        }
        return sInstance;
    }

    public abstract RecipeDao recipeDao();
}
