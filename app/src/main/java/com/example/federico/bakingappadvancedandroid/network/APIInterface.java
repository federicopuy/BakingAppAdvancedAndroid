package com.example.federico.bakingappadvancedandroid.network;

import com.example.federico.bakingappadvancedandroid.model.Recipe;
import com.example.federico.bakingappadvancedandroid.utils.NetworkUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIInterface {

    @GET(NetworkUtils.RECIPES_URL)
    Call<List<Recipe>> getRecipes();

}
