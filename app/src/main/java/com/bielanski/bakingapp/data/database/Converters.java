package com.bielanski.bakingapp.data.database;

import android.arch.persistence.room.TypeConverter;

import com.bielanski.bakingapp.data.Ingredients;
import com.bielanski.bakingapp.data.Step;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class Converters {
    @TypeConverter
    public static ArrayList<Ingredients> toIngredientsList(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Ingredients>>() {}.getType();
        ArrayList<Ingredients> ingredients = gson.fromJson(json, type);
        return ingredients;
    }

    @TypeConverter
    public static String fromIngredientsList(ArrayList<Ingredients> list) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Ingredients>>() {}.getType();
        String json = gson.toJson(list, type);
        return json;
    }

    @TypeConverter
    public static ArrayList<Step> toStepList(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Step>>() {}.getType();
        ArrayList<Step> steps = gson.fromJson(json, type);
        return steps;
    }

    @TypeConverter
    public static String fromStepList(ArrayList<Step> list) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Step>>() {}.getType();
        String json = gson.toJson(list, type);
        return json;
    }
}
