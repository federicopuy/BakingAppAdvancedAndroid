package com.example.federico.bakingappadvancedandroid.activities;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.federico.bakingappadvancedandroid.R;
import com.example.federico.bakingappadvancedandroid.adapters.RecipesAdapter;
import com.example.federico.bakingappadvancedandroid.model.Recipe;
import com.example.federico.bakingappadvancedandroid.network.APIInterface;
import com.example.federico.bakingappadvancedandroid.network.RetrofitClient;
import com.example.federico.bakingappadvancedandroid.utils.Constants;
import com.example.federico.bakingappadvancedandroid.utils.NetworkUtils;
import com.example.federico.bakingappadvancedandroid.widget.IngredientsWidgetProvider;
import com.google.gson.Gson;

import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesActivity extends AppCompatActivity implements RecipesAdapter.ListItemClickListener {

    private final String TAG = "Recipes Activity";
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private Parcelable mListState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        ButterKnife.bind(this);

        getSupportActionBar();

        boolean mTwoPane = getResources().getBoolean(R.bool.isTablet);
        mRecyclerView = findViewById(R.id.recipes_recycler_view);

        if (mTwoPane) {
            mLayoutManager = new GridLayoutManager(this, 3);
        } else {
            mLayoutManager = new LinearLayoutManager(this);
        }
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (NetworkUtils.isNetworkAvailable(RecipesActivity.this)) {
            getRecipes();
        } else {
            Toast.makeText(RecipesActivity.this, getString(R.string.no_internet_error), Toast.LENGTH_LONG).show();
        }
    }

    private void getRecipes() {

        APIInterface mService = RetrofitClient.getClient(getApplicationContext()).create(APIInterface.class);
        retrofit2.Call<List<Recipe>> callGetRecipes = mService.getRecipes();
        callGetRecipes.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()){
                    Log.d(TAG,"Response Successful");
                    List<Recipe> recipesList = response.body();
                    try{
                        RecipesAdapter mAdapter = new RecipesAdapter(recipesList, RecipesActivity.this, RecipesActivity.this);
                        mRecyclerView.setAdapter(mAdapter);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                } else {
                    Log.d(TAG,"Response NOT Successful");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Recipe>> call, Throwable t) {
                Log.d(TAG,"Response onFailure");
            }
        });
    }

    @Override
    public void onListItemClick(int clickedItemIndex, final Recipe clickedRecipe) {

        RecipesActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // this will send the broadcast to update the appwidget
                IngredientsWidgetProvider.sendRefreshBroadcast(RecipesActivity.this, clickedRecipe);
            }
        });

        Intent recipeDetailIntent = new Intent(RecipesActivity.this, StepMasterActivity.class);
        Gson recipeGson = new Gson();
        recipeDetailIntent.putExtra(Constants.RECIPE_DETAIL_INTENT, recipeGson.toJson(clickedRecipe));
        startActivity(recipeDetailIntent);
    }

    //copied from https://stackoverflow.com/questions/28236390/recyclerview-store-restore-state-between-activities
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        mListState = mLayoutManager.onSaveInstanceState();
        state.putParcelable(KEY_RECYCLER_STATE, mListState);
    }

    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        if (state != null)
            mListState = state.getParcelable(KEY_RECYCLER_STATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mListState != null) {
            mLayoutManager.onRestoreInstanceState(mListState);
        }
    }
}
