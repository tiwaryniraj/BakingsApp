package com.example.niraj.bakingsapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.preference.PreferenceManager;

/**
 * Created by Gilang Kusuma Jati on 8/29/17.
 */

public class PreferenceUtil {
    private static final String RECIPE_ID = "recipe_id";
    private static final String RECIPE_NAME = "recipe_name";
    public static final int NO_ID = -1;
    public static final String NO_NAME = "";

    public static final int getSelectedRecipeId(@NonNull Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(RECIPE_ID, NO_ID);
    }

    public static void setSelectedRecipeId(@NonNull Context context, int recipeId) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(RECIPE_ID, recipeId);
        editor.apply();
    }

    public static final String getSelectedRecipeName(@NonNull Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(RECIPE_NAME, NO_NAME);
    }

    public static void setSelectedRecipeName(@NonNull Context context, String recipeName) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(RECIPE_NAME, recipeName);
        editor.apply();
    }
}
