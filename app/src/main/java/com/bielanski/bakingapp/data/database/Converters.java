package com.bielanski.bakingapp.data.database;

import android.arch.persistence.room.TypeConverter;

import com.bielanski.bakingapp.data.Ingredients;
import com.bielanski.bakingapp.data.Step;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Converters {
    @TypeConverter
    public static ArrayList<Ingredients> stringToIngredients(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Ingredients>>() {}.getType();
        ArrayList<Ingredients> ingredients = gson.fromJson(json, type);
        return ingredients;
    }

    @TypeConverter
    public static String ingredientsToString(ArrayList<Ingredients> list) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Ingredients>>() {}.getType();
        String json = gson.toJson(list, type);
        return json;
    }

    @TypeConverter
    public static List<Step> stringToStep(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Step>>() {}.getType();
        List<Step> steps = gson.fromJson(json, type);
        return steps;
    }

    @TypeConverter
    public static String stepToString(ArrayList<Step> list) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Step>>() {}.getType();
        String json = gson.toJson(list, type);
        return json;
    }
}
