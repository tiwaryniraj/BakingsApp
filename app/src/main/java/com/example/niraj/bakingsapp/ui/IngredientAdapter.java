package com.example.niraj.bakingsapp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.niraj.bakingsapp.R;
import com.example.niraj.bakingsapp.model.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private final IngredientListItemClickListener ingredientListItemClickListener;
    private List<Ingredient> ingredients;

    public IngredientAdapter(IngredientListItemClickListener ingredientListItemClickListener) {
        this.ingredientListItemClickListener = ingredientListItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View rootView = layoutInflater.inflate(R.layout.ingredient_list_item, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.bind(ingredient);
    }

    @Override
    public int getItemCount() {
        return ingredients == null ? 0 : ingredients.size();
    }

    public void setData(@NonNull List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.layout_root)
        LinearLayout layoutRoot;

        @BindView(R.id.textview_ingredient_summary)
        TextView ingredientSummary;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        private void bind(@NonNull Ingredient ingredient) {
            int backgroundColor = getAdapterPosition() % 2 == 0 ? R.color.background1 : R.color.background2;
            layoutRoot.setBackgroundColor(ContextCompat.getColor(layoutRoot.getContext(), backgroundColor));
            ingredientSummary.setText(ingredient.toString());
        }

        @Override
        public void onClick(View v) {
            ingredientListItemClickListener.onIngredientItemClick(ingredients.get(getAdapterPosition()));
        }
    }

    public interface IngredientListItemClickListener {
        void onIngredientItemClick(Ingredient ingredient);
    }
}
