package com.example.niraj.bakingsapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.niraj.bakingsapp.model.WidgetType;
import com.example.niraj.bakingsapp.service.IngredientListWidgetService;
import com.example.niraj.bakingsapp.service.RecipeListWidgetService;
import com.example.niraj.bakingsapp.service.RecipeWidgetService;
import com.example.niraj.bakingsapp.util.PreferenceUtil;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, WidgetType widgetType) {

        RemoteViews views;

        if (widgetType == WidgetType.INGREDIENTS) {
            views = getIngredientListRemoteView(context);
        } else if (widgetType == WidgetType.RECIPES) {
            views = getRecipeListRemoteView(context);
        } else {
            views = getEmptyRemoteView(context);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, WidgetType widgetType) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, widgetType);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RecipeWidgetService.startActionUpdateWidgets(context);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private static RemoteViews getIngredientListRemoteView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_view);
        String recipeName = PreferenceUtil.getSelectedRecipeName(context);
        views.setTextViewText(R.id.title_text_view, context.getString(R.string.ingredient_of, recipeName));

        Intent intent = new Intent(context, IngredientListWidgetService.class);
        views.setRemoteAdapter(R.id.widget_list_view, intent);

        Intent appIntent = new Intent(context, RecipeDetailActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_list_view, appPendingIntent);

        views.setEmptyView(R.id.widget_list_view, R.id.empty_view);
        return views;
    }

    private static RemoteViews getRecipeListRemoteView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_view);
        String title = context.getString(R.string.recipes);
        views.setTextViewText(R.id.title_text_view, title);

        Intent intent = new Intent(context, RecipeListWidgetService.class);
        views.setRemoteAdapter(R.id.widget_list_view, intent);

        Intent appIntent = new Intent(context, RecipeDetailActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_list_view, appPendingIntent);

        views.setEmptyView(R.id.widget_list_view, R.id.empty_view);
        return views;
    }

    private static RemoteViews getEmptyRemoteView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.empty_widget_view);

        Intent appIntent = new Intent(context, RecipeActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.empty_widget_image_view, appPendingIntent);
        views.setOnClickPendingIntent(R.id.empty_widget_image_view, appPendingIntent);

        return views;
    }
}

