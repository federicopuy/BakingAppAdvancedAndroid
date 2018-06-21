package com.example.federico.bakingappadvancedandroid.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.federico.bakingappadvancedandroid.R;
import com.example.federico.bakingappadvancedandroid.model.Ingredient;
import com.example.federico.bakingappadvancedandroid.model.Recipe;
import com.example.federico.bakingappadvancedandroid.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UpdatingIngredientsListService extends RemoteViewsService {

    // consulted https://www.sitepoint.com/killer-way-to-show-a-list-of-items-in-android-collection-widget/

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsWidgetViewsFactory(this.getApplicationContext(), intent);
    }

    class IngredientsWidgetViewsFactory implements RemoteViewsFactory {

        final Context mContext;
        Integer recipeId;
        List<Ingredient> ingredientList;
        Recipe selectedRecipe;

        IngredientsWidgetViewsFactory(Context context, Intent intent) {
            mContext = context;
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {

            recipeId = IngredientsWidgetProvider.recipeId;
            try {
                String jsonArray = loadJSONFromAsset();
                Type listType = new TypeToken<ArrayList<Recipe>>() {
                }.getType();
                List<Recipe> recipesList = new Gson().fromJson(jsonArray, listType);
                for (Recipe recipe : recipesList) {
                    if (recipe.getId() == recipeId) {
                        selectedRecipe = recipe;
                        ingredientList = recipe.getIngredients();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDestroy() {
        }

        @Override
        public int getCount() {
            return ingredientList == null ? 0 : ingredientList.size();
        }

        @Override
        public RemoteViews getViewAt(int i) {

            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.each_list_item);

            if (recipeId != null) {
                Ingredient ingredient = ingredientList.get(i);
                String textToDisplay = ingredient.getQuantity() + " " +
                        ingredient.getMeasure() + " " +
                        ingredient.getIngredient();
                rv.setTextViewText(R.id.widgetItemIngredientNameLabel, textToDisplay);
                Intent fillInIntent = new Intent();
                Gson recipeGson = new Gson();
                fillInIntent.putExtra(Constants.RECIPE_DETAIL_INTENT, recipeGson.toJson(selectedRecipe));
                rv.setOnClickFillInIntent(R.id.widgetItemContainer, fillInIntent);
            }
            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        String loadJSONFromAsset() {
            String json;
            try {
                InputStream is = mContext.getAssets().open("recipes");
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
    }
}
