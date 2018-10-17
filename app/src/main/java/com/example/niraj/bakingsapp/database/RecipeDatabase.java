package com.example.niraj.bakingsapp.database;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

@Database(version = RecipeDatabase.VERSION,
        packageName = "com.example.niraj.bakingsapp.provider")
public class RecipeDatabase {

    public static final int VERSION = 1;

    @Table(IngredientColumns.class)
    public static final String INGREDIENTS = "ingredients";

    @Table(RecipeColumns.class)
    public static final String RECIPES = "recipes";

    @Table(StepColumns.class)
    public static final String STEPS = "steps";
}