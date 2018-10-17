package com.example.niraj.bakingsapp.database;

import android.support.annotation.NonNull;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;


public interface IngredientColumns {
    @DataType(DataType.Type.INTEGER)
    @PrimaryKey
    @AutoIncrement
    String ID = "_id";

    @DataType(DataType.Type.REAL)
    String QUANTITY = "quantity";

    @DataType(DataType.Type.TEXT)
    String MEASURE = "measure";

    @DataType(DataType.Type.TEXT)
    String NAME = "name";

    @DataType(DataType.Type.INTEGER)
    @References(table = RecipeDatabase.RECIPES, column = RecipeColumns.ID)
    @NonNull
    String RECIPE_ID = "recipeId";
}
