package com.example.niraj.bakingsapp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.niraj.bakingsapp.R;
import com.example.niraj.bakingsapp.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {

    private final StepListItemClickListener stepListItemClickListener;
    private List<Step> steps;

    public StepAdapter(StepListItemClickListener stepListItemClickListener) {
        this.stepListItemClickListener = stepListItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View rootView = layoutInflater.inflate(R.layout.step_list_item, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Step step = steps.get(position);
        holder.bind(step);
    }

    @Override
    public int getItemCount() {
        return steps == null ? 0 : steps.size();
    }

    public void setData(List<Step> steps) {
        this.steps = steps;
        notifyDataSetChanged();
    }

    public List<Step> getData() {
        return steps;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.textview_step_count)
        AppCompatTextView stepCount;

        @BindView(R.id.textview_short_description)
        AppCompatTextView shortDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(@NonNull Step step) {
            stepCount.setText(stepCount.getContext().getString(R.string.step_count, getAdapterPosition()));
            shortDescription.setText(step.getShortDescription());
        }

        @Override
        public void onClick(View v) {
            stepListItemClickListener.onStepItemClick(getAdapterPosition());
        }
    }

    public interface StepListItemClickListener {
        void onStepItemClick(int position);
    }

}
