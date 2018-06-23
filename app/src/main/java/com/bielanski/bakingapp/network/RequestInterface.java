package com.bielanski.bakingapp.network;

import com.bielanski.bakingapp.data.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RequestInterface {
    @GET("android-baking-app-json")
    Call<List<Recipe>> getJSON();
}