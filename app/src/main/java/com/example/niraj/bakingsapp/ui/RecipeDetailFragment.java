package com.example.niraj.bakingsapp.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.niraj.bakingsapp.R;
import com.example.niraj.bakingsapp.model.Ingredient;
import com.example.niraj.bakingsapp.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailFragment extends Fragment
        implements IngredientAdapter.IngredientListItemClickListener,
        StepAdapter.StepListItemClickListener {

    private static final String RECIPE = "recipe";

    @BindView(R.id.recyclerview_ingredient)
    RecyclerView ingredientRecyclerView;

    @BindView(R.id.recyclerview_step)
    RecyclerView stepRecyclerView;

    private IngredientAdapter ingredientAdapter;
    private StepAdapter stepAdapter;

    private Unbinder unbinder;

    private Recipe recipe;

    private StepClickListener stepClickListener;

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            stepClickListener = (StepClickListener) context;
        }
        catch (ClassCastException cce) {
            throw new ClassCastException(context.toString() + " should implements interface StepClickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        unbinder = ButterKnife.bind(this, view);

        Context context = getActivity();

        ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        ingredientRecyclerView.setHasFixedSize(true);
        ingredientRecyclerView.setNestedScrollingEnabled(false);
        ingredientAdapter = new IngredientAdapter(this);
        ingredientRecyclerView.setAdapter(ingredientAdapter);

        stepRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        stepRecyclerView.setHasFixedSize(true);
        stepRecyclerView.setNestedScrollingEnabled(false);
        stepAdapter = new StepAdapter(this);
        stepRecyclerView.setAdapter(stepAdapter);

        if (savedInstanceState != null) {
            recipe = savedInstanceState.getParcelable(RECIPE);
        }

        if (recipe != null) {
            ingredientAdapter.setData(recipe.getIngredients());
            stepAdapter.setData(recipe.getSteps());
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECIPE, recipe);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
    }

    @Override
    public void onIngredientItemClick(Ingredient ingredient) {

    }

    @Override
    public void onStepItemClick(int position) {
        stepClickListener.onStepClicked(position);
    }

    public void setRecipe(@NonNull Recipe recipe) {
        this.recipe = recipe;
    }

    public interface StepClickListener {
        void onStepClicked(int position);
    }
}
