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

import java.util.List;

public class UpdatingIngredientsListService extends RemoteViewsService {

    // consulted https://www.sitepoint.com/killer-way-to-show-a-list-of-items-in-android-collection-widget/

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsWidgetViewsFactory(this.getApplicationContext(), intent);
    }

    class IngredientsWidgetViewsFactory implements RemoteViewsFactory {

        final Context mContext;
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

            assert IngredientsWidgetProvider.recipe!=null;
            try {
               selectedRecipe = IngredientsWidgetProvider.recipe;
               ingredientList = selectedRecipe.getIngredients();
           }catch (Exception e){
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

            if (selectedRecipe != null) {
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

    }
}
