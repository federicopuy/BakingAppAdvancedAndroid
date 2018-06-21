package com.example.federico.bakingappadvancedandroid.activities;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.federico.bakingappadvancedandroid.R;
import com.example.federico.bakingappadvancedandroid.adapters.RecipesAdapter;
import com.example.federico.bakingappadvancedandroid.model.Recipe;
import com.example.federico.bakingappadvancedandroid.utils.Constants;
import com.example.federico.bakingappadvancedandroid.widget.IngredientsWidgetProvider;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class RecipesActivity extends AppCompatActivity implements RecipesAdapter.ListItemClickListener {

    private final String KEY_RECYCLER_STATE = "recycler_state";
    private RecyclerView mRecyclerView;
    private boolean mTwoPane = false;
    private RecyclerView.LayoutManager mLayoutManager;
    private Parcelable mListState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        ButterKnife.bind(this);

        getSupportActionBar();

        mTwoPane = getResources().getBoolean(R.bool.isTablet);
        mRecyclerView = findViewById(R.id.recipes_recycler_view);

        if (mTwoPane) {
            mLayoutManager = new GridLayoutManager(this, 3);
        } else {
            mLayoutManager = new LinearLayoutManager(this);
        }
        mRecyclerView.setLayoutManager(mLayoutManager);
        populateRecyclerView();
    }

    private void populateRecyclerView() {

        try {
            String jsonArray = loadJSONFromAsset();
            Type listType = new TypeToken<ArrayList<Recipe>>() {
            }.getType();
            List<Recipe> recipesList = new Gson().fromJson(jsonArray, listType);
            RecipesAdapter mAdapter = new RecipesAdapter(recipesList, RecipesActivity.this, RecipesActivity.this);
            mRecyclerView.setAdapter(mAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = RecipesActivity.this.getAssets().open("recipes");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public void onListItemClick(int clickedItemIndex, final Recipe clickedRecipe) {

        RecipesActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // this will send the broadcast to update the appwidget
                IngredientsWidgetProvider.sendRefreshBroadcast(RecipesActivity.this, clickedRecipe.getId());
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
