package com.bielanski.bakingapp.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.bielanski.bakingapp.data.Recipe;
import java.util.List;

@Dao
public interface  RecipeDao {
    @Query("SELECT * FROM recipes")
    List<Recipe> getAllRecipes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(Recipe... recipe);

    @Query("DELETE FROM recipes")
    void deleteAll();

    @Query("SELECT COUNT(*) FROM recipes")
    int countRecipes();

    @Query("SELECT * FROM recipes WHERE id == :id")
    List<Recipe> getRecipesWithId(int id);
}
