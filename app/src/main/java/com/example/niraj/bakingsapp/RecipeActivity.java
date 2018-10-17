package com.example.niraj.bakingsapp;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.example.niraj.bakingsapp.api.RecipeAPIClient;
import com.example.niraj.bakingsapp.model.Recipe;
import com.example.niraj.bakingsapp.service.RecipeWidgetService;
import com.example.niraj.bakingsapp.ui.RecipeAdapter;
import com.example.niraj.bakingsapp.util.DBUtil;
import com.example.niraj.bakingsapp.util.PreferenceUtil;
import com.example.niraj.bakingsapp.util.SimpleIdlingResource;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

public class RecipeActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Pair<List<Recipe>, Exception>>,
        RecipeAdapter.RecipeListItemClickListener {

    private static final int ONLINE_RECIPE_LOADER_ID = 999;
    private static final String EXTRA_RECIPE_LIST = "extra_recipes_list";
    public static final String EXTRA_RECIPE = "extra_recipe";

    private static final int PHONE_POTRAIT_COLUMN = 1;
    private static final int PHONE_LANDSCAPE_COLUMN = 2;
    private static final int TABLET_POTRAIT_COLUMN = 2;
    private static final int TABLET_LANDSCAPE_COLUMN = 3;

    @BindView(R.id.layout_root)
    FrameLayout layoutRoot;

    @BindView(R.id.recyclerview_recipe)
    RecyclerView recyclerView;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    private RecipeAdapter recipeAdapter;

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        Timber.plant(new Timber.DebugTree());

        Context context = this;
        int numColumn = getColumn();
        GridLayoutManager layoutManager = new GridLayoutManager(context, numColumn);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        recipeAdapter = new RecipeAdapter(this);
        recyclerView.setAdapter(recipeAdapter);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(EXTRA_RECIPE_LIST)) {
                List<Recipe> recipes = savedInstanceState.getParcelableArrayList(EXTRA_RECIPE_LIST);
                recipeAdapter.swapData(recipes);
            }
            hideProgressBar();
        } else {
            getSupportLoaderManager().initLoader(ONLINE_RECIPE_LOADER_ID, null, this);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (recipeAdapter != null) {
            List<Recipe> recipes = recipeAdapter.getData();
            if (recipes != null && !recipes.isEmpty()) {
                outState.putParcelableArrayList(EXTRA_RECIPE_LIST, new ArrayList<>(recipes));
            }
        }
    }

    @Override
    public Loader<Pair<List<Recipe>, Exception>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Pair<List<Recipe>, Exception>>(this) {

            @Override
            protected void onStartLoading() {
                showProgressBar();
                forceLoad();
            }

            @Override
            public Pair<List<Recipe>, Exception> loadInBackground() {
                Call<List<Recipe>> call = RecipeAPIClient.getInstance().getRecipes();
                try {
                    Response<List<Recipe>> response = call.execute();
                    if (response != null && response.isSuccessful()) {
                        List<Recipe> recipes = response.body();
                        return new Pair<>(recipes, null);
                    }
                } catch (IOException ex) {
                    Timber.e(ex);
                    return new Pair<List<Recipe>, Exception>(null, ex);
                }

                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Pair<List<Recipe>, Exception>> loader, Pair<List<Recipe>, Exception> data) {
        hideProgressBar();
        if (data != null) {
            if (data.second != null) {
                showMessage(getErrorMessage(data.second));
            } else {
                final List<Recipe> recipes = data.first;
                recipeAdapter.swapData(recipes);
                showMessage(getString(R.string.data_loaded, recipes.size()));

                // insert recipe with its ingredients and steps to DB
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        ContentResolver contentResolver = getContentResolver();
                        DBUtil.deleteAllRecipes(contentResolver);
                        DBUtil.deleteAllIngredients(contentResolver);
                        DBUtil.deleteAllSteps(contentResolver);
                        DBUtil.insertRecipes(contentResolver, recipes);
                        for (Recipe recipe : recipes) {
                            DBUtil.insertIngredients(contentResolver, recipe.getIngredients(), recipe.getRecipeId());
                            DBUtil.insertSteps(contentResolver, recipe.getSteps(), recipe.getRecipeId());
                        }
                        RecipeWidgetService.startActionUpdateWidgets(RecipeActivity.this);
                        return null;
                    }
                }.execute();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Pair<List<Recipe>, Exception>> loader) {

    }

    @Override
    public void onRecipeItemClick(Recipe recipe) {
        PreferenceUtil.setSelectedRecipeId(this, recipe.getRecipeId());
        PreferenceUtil.setSelectedRecipeName(this, recipe.getName());
        RecipeWidgetService.startActionUpdateWidgets(this);

        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(EXTRA_RECIPE, recipe);
        startActivity(intent);
    }

    private String getErrorMessage(Exception ex) {
        if (ex instanceof UnknownHostException) {
            return getString(R.string.unknown_host_exception);
        } else {
            return ex.getMessage();
        }
    }

    private void showMessage(@NonNull String message) {
        Snackbar snackbar = Snackbar
                .make(layoutRoot, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    private int getColumn() {
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (isTablet) {
            return isLandscape ? TABLET_LANDSCAPE_COLUMN : TABLET_POTRAIT_COLUMN;
        } else {
            return isLandscape ? PHONE_LANDSCAPE_COLUMN : PHONE_POTRAIT_COLUMN;
        }
    }
}
