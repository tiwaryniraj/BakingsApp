package com.example.niraj.bakingsapp.service;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.niraj.bakingsapp.R;
import com.example.niraj.bakingsapp.RecipeActivity;
import com.example.niraj.bakingsapp.model.Recipe;
import com.example.niraj.bakingsapp.util.DBUtil;
import com.example.niraj.bakingsapp.util.PreferenceUtil;

import java.util.List;


public class RecipeListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeListRemoteViewsFactory(getApplicationContext());
    }
}

class RecipeListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    List<Recipe> recipes;

    public RecipeListRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        int selectedRecipeId = PreferenceUtil.getSelectedRecipeId(mContext);
        recipes = DBUtil.getRecipes(mContext.getContentResolver());
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return recipes != null ? recipes.size() : 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Recipe recipe = recipes.get(position);

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_list_item);
        views.setTextViewText(R.id.textview_ingredient_summary, recipe.getName());

        // Fill in the onClick PendingIntent Template using the specific plant Id for each item individually
        //Bundle extras = new Bundle();
        //extras.putParcelable(RecipeActivity.EXTRA_RECIPE, recipe);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(RecipeActivity.EXTRA_RECIPE, recipe);
        views.setOnClickFillInIntent(R.id.textview_ingredient_summary, fillInIntent);

        return views;
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
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
