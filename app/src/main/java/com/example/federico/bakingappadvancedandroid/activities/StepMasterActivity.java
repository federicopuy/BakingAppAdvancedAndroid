package com.example.federico.bakingappadvancedandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.federico.bakingappadvancedandroid.R;

import com.example.federico.bakingappadvancedandroid.adapters.IngredientsAdapter;
import com.example.federico.bakingappadvancedandroid.adapters.StepsAdapter;
import com.example.federico.bakingappadvancedandroid.model.Recipe;
import com.example.federico.bakingappadvancedandroid.utils.Constants;
import com.google.gson.Gson;

import butterknife.ButterKnife;

/**
 * An activity representing a list of Steps. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class StepMasterActivity extends AppCompatActivity {

    private static final String SCROLL_POSITION = "scrollPosition";
    private Recipe recipe;
    private ScrollView mScrollView;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_master);
        ButterKnife.bind(this);
        mScrollView = findViewById(R.id.scrollViewMaster);

        recipe = getRecipeFromIntent();

        if (recipe == null) {
            Toast.makeText(StepMasterActivity.this, R.string.error_recipe, Toast.LENGTH_LONG).show();
            finish();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(recipe.getName());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (findViewById(R.id.step_detail_container) != null) {
            mTwoPane = true;
        }

        // setup recyclerview showing ingredients
        View recyclerViewIngredients = findViewById(R.id.ingredients_list);
        assert recyclerViewIngredients != null;
        setupRecyclerViewIngredients((RecyclerView) recyclerViewIngredients);

        // setup recyclerview showing steps
        View recyclerViewSteps = findViewById(R.id.step_list);
        assert recyclerViewSteps != null;
        setupRecyclerViewSteps((RecyclerView) recyclerViewSteps);
    }

    private Recipe getRecipeFromIntent() {

        Intent intent = getIntent();
        if (intent.hasExtra(Constants.RECIPE_DETAIL_INTENT)) {
            String recipeJson = intent.getStringExtra(Constants.RECIPE_DETAIL_INTENT);
            Gson recipeGson = new Gson();
            return recipeGson.fromJson(recipeJson, Recipe.class);
        } else {
            return null;
        }
    }

    private void setupRecyclerViewIngredients(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new IngredientsAdapter(recipe.getIngredients()));
    }

    private void setupRecyclerViewSteps(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new StepsAdapter(this, recipe.getSteps(), mTwoPane));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // copied from https://stackoverflow.com/questions/29208086/save-the-position-of-scrollview-when-the-orientation-changes
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray(SCROLL_POSITION,
                new int[]{mScrollView.getScrollX(), mScrollView.getScrollY()});
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final int[] position = savedInstanceState.getIntArray(SCROLL_POSITION);
        if (position != null)
            mScrollView.post(new Runnable() {
                public void run() {
                    mScrollView.scrollTo(position[0], position[1]);
                }
            });
    }
}
