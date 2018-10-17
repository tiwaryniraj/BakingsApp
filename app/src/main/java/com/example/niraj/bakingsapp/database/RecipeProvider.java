package com.example.niraj.bakingsapp.database;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;


@ContentProvider(
        authority = RecipeProvider.AUTHORITY,
        database = RecipeDatabase.class,
        packageName = "com.example.niraj.bakingsapp.provider"
)
public class RecipeProvider {

    public static final String AUTHORITY = "com.example.niraj.bakingsapp.database.provider";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String INGREDIENTS = "ingredients";
        String RECIPES = "recipes";
        String STEPS = "steps";
    }

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = RecipeDatabase.INGREDIENTS)
    public static class Ingredients {

        @ContentUri(
                path = Path.INGREDIENTS,
                type = "vnd.android.cursor.dir/ingredients",
                defaultSort = IngredientColumns.ID + " ASC")
        public static final Uri INGREDIENTS = buildUri(Path.INGREDIENTS);

        @InexactContentUri(
                path = Path.INGREDIENTS + "/#",
                name = "INGREDIENT_ID",
                type = "vnd.android.cursor.item/ingredients",
                whereColumn = IngredientColumns.ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.INGREDIENTS, String.valueOf(id));
        }
    }

    @TableEndpoint(table = RecipeDatabase.RECIPES)
    public static class Recipes {
        @ContentUri(
                path = Path.RECIPES,
                type = "vnd.android.cursor.dir/recipes",
                defaultSort = RecipeColumns.NAME + " ASC")
        public static final Uri RECIPES = buildUri(Path.RECIPES);

        @InexactContentUri(
                path = Path.RECIPES + "/#",
                name = "RECIPE_ID",
                type = "vnd.android.cursor.item/recipes",
                whereColumn = RecipeColumns.ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.RECIPES, String.valueOf(id));
        }
    }

    @TableEndpoint(table = RecipeDatabase.STEPS)
    public static class Steps {
        @ContentUri(
                path = Path.STEPS,
                type = "vnd.android.cursor.dir/steps",
                defaultSort = StepColumns.ORDER + " ASC")
        public static final Uri STEPS = buildUri(Path.STEPS);

        @InexactContentUri(
                path = Path.STEPS + "/#",
                name = "STEP_ID",
                type = "vnd.android.cursor.item/steps",
                whereColumn = StepColumns.ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.STEPS, String.valueOf(id));
        }
    }

}
