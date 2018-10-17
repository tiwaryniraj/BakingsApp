package com.example.niraj.bakingsapp.database;

import android.support.annotation.NonNull;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;


public interface StepColumns {

    @DataType(DataType.Type.INTEGER)
    @PrimaryKey
    @AutoIncrement
    String ID = "_id";

    @DataType(DataType.Type.INTEGER)
    String STEP_ID = "stepId";

    @DataType(DataType.Type.TEXT)
    String SHORT_DESCRIPTION = "shortDescription";

    @DataType(DataType.Type.TEXT)
    String DESCRIPTION = "description";

    @DataType(DataType.Type.TEXT)
    String VIDEO_URL = "videoURL";

    @DataType(DataType.Type.TEXT)
    String THUMBNAIL_URL = "thumbnailURL";

    @DataType(DataType.Type.INTEGER)
    @NonNull
    String ORDER = "stepOrder";

    @DataType(DataType.Type.INTEGER)
    @References(table = RecipeDatabase.RECIPES, column = RecipeColumns.ID)
    @NonNull
    String RECIPE_ID = "recipeId";
}
