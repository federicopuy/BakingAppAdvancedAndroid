package com.example.federico.bakingappadvancedandroid.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.example.federico.bakingappadvancedandroid.R;
import com.example.federico.bakingappadvancedandroid.activities.StepMasterActivity;
import com.example.federico.bakingappadvancedandroid.model.Recipe;
import com.example.federico.bakingappadvancedandroid.utils.Constants;
import com.google.gson.Gson;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidgetProvider extends AppWidgetProvider {

    static Recipe recipe;

    public static void sendRefreshBroadcast(Context context, Recipe recipe) {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        Gson recipeGson = new Gson();
        intent.putExtra(Constants.RECIPE_ID, recipeGson.toJson(recipe));
        intent.setComponent(new ComponentName(context, IngredientsWidgetProvider.class));
        context.sendBroadcast(intent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.hasExtra(Constants.RECIPE_ID)){
            String recipeJson = intent.getStringExtra(Constants.RECIPE_ID);
            Gson recipeGson = new Gson();
            recipe = recipeGson.fromJson(recipeJson, Recipe.class);
        }

        final String action = intent.getAction();
        assert action != null;
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, IngredientsWidgetProvider.class);
            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.widgetListView);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, UpdatingIngredientsListService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            Gson recipeGson = new Gson();
            intent.putExtra(Constants.RECIPE_ID, recipeGson.toJson(recipe));
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget_provider);
            views.setRemoteAdapter(R.id.widgetListView, intent);

            // template to handle the click listener for each item
            Intent clickIntentTemplate = new Intent(context, StepMasterActivity.class);
            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(clickIntentTemplate)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widgetListView, clickPendingIntentTemplate);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}

