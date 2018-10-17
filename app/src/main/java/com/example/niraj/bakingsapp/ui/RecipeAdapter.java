package com.example.niraj.bakingsapp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.niraj.bakingsapp.R;
import com.example.niraj.bakingsapp.model.Recipe;
import com.example.niraj.bakingsapp.util.ImageUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private final RecipeListItemClickListener recipeListItemClickListener;
    private List<Recipe> recipes;

    public RecipeAdapter(RecipeListItemClickListener recipeListItemClickListener) {
        this.recipeListItemClickListener = recipeListItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View rootView = layoutInflater.inflate(R.layout.recipe_list_item, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.bind(recipe);
    }

    @Override
    public int getItemCount() {
        return recipes == null ? 0 : recipes.size();
    }

    public void swapData(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    public List<Recipe> getData() {
        return recipes;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.imageview_receipt)
        AppCompatImageView image;

        @BindView(R.id.textview_recipe_name)
        TextView recipeName;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        private void bind(@NonNull Recipe recipe) {
            if (!TextUtils.isEmpty(recipe.getImage())) {
                Picasso.with(recipeName.getContext()).load(recipe.getImage()).into(image);
            }
            else {
                Picasso.with(recipeName.getContext()).load(ImageUtil.getImageResId(recipe.getName())).into(image);
            }
            recipeName.setText(recipe.getName());
        }

        @Override
        public void onClick(View v) {
            recipeListItemClickListener.onRecipeItemClick(recipes.get(getAdapterPosition()));
        }
    }

    public interface RecipeListItemClickListener {
        void onRecipeItemClick(Recipe recipe);
    }
}
