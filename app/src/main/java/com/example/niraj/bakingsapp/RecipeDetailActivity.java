package com.example.niraj.bakingsapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.niraj.bakingsapp.model.Recipe;
import com.example.niraj.bakingsapp.ui.RecipeDetailFragment;
import com.example.niraj.bakingsapp.ui.RecipeStepFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.example.niraj.bakingsapp.RecipeActivity.EXTRA_RECIPE;

public class RecipeDetailActivity extends AppCompatActivity
        implements RecipeDetailFragment.StepClickListener,
        RecipeStepFragment.StepActionListener {

    public static final String EXTRA_LIST_INDEX = "extra_list_index";
    public static final String EXTRA_STEP_LIST = "extra_step_list";
    public static final String EXTRA_RECIPE_NAME = "extra_recipe_name";

    @Nullable
    @BindView(R.id.layout_root)
    LinearLayout layoutRoot;

    @BindView(R.id.recipe_detail_content_view)
    FrameLayout container;

    private boolean isTwoPane;
    private Recipe recipe;
    private int selectedStepIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        ButterKnife.bind(this);
        Timber.plant(new Timber.DebugTree());

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(EXTRA_RECIPE)) {
                recipe = intent.getParcelableExtra(EXTRA_RECIPE);
                selectedStepIndex = savedInstanceState != null && savedInstanceState.containsKey(EXTRA_LIST_INDEX) ?
                        savedInstanceState.getInt(EXTRA_LIST_INDEX) : 0;
            }
        } else if (savedInstanceState != null) {
            recipe = savedInstanceState.getParcelable(EXTRA_RECIPE);
            selectedStepIndex = savedInstanceState.getInt(EXTRA_LIST_INDEX);
        }

        if (recipe != null) {
            if (savedInstanceState == null) {
                replaceRecipeDetailFragment(recipe);
            }

            if (findViewById(R.id.recipe_step_content_view) != null) {
                isTwoPane = true;
                if (savedInstanceState == null) {
                    replaceRecipeStepFragment(selectedStepIndex);
                }
            } else {
                isTwoPane = false;
            }

            setTitle(recipe.getName());
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_LIST_INDEX, selectedStepIndex);
        outState.putParcelable(EXTRA_RECIPE, recipe);
    }

    @Override
    public void onStepClicked(int position) {
        selectedStepIndex = position;
        if (isTwoPane) {
            replaceRecipeStepFragment(position);
        } else {
            Intent intent = new Intent(this, RecipeStepActivity.class);
            intent.putExtra(EXTRA_LIST_INDEX, position);
            intent.putParcelableArrayListExtra(EXTRA_STEP_LIST, new ArrayList<Parcelable>(recipe.getSteps()));
            intent.putExtra(EXTRA_RECIPE_NAME, recipe.getName());
            startActivity(intent);
        }
    }

    @Override
    public void onNext() {
        if (selectedStepIndex < recipe.getSteps().size() - 1) {
            selectedStepIndex++;
            replaceRecipeStepFragment(selectedStepIndex);
        }
    }

    @Override
    public void onPrev() {
        if (selectedStepIndex > 0) {
            selectedStepIndex--;
            replaceRecipeStepFragment(selectedStepIndex);
        }
    }

    private void replaceRecipeDetailFragment(Recipe recipe) {
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        recipeDetailFragment.setRecipe(recipe);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.recipe_detail_content_view, recipeDetailFragment)
                .commit();
    }

    private void replaceRecipeStepFragment(int position) {
        RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
        recipeStepFragment.setListIndex(position);
        recipeStepFragment.setStep(recipe.getSteps().get(position));
        recipeStepFragment.isPrevEnabled(position > 0);
        recipeStepFragment.isNextEnabled(position < recipe.getSteps().size() - 1);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().
                replace(R.id.recipe_step_content_view, recipeStepFragment)
                .commit();
    }
}
