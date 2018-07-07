package com.bielanski.bakingapp;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bielanski.bakingapp.data.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {
    private List<Step> mSteps;
    private final StepsAdapter.OnClickStepHandler mClickHandler;
    private String TAG = "StepsAdapter";

    public interface OnClickStepHandler {
        void stepOnClick(int position);
    }

    public StepsAdapter(List<Step> steps, StepsAdapter.OnClickStepHandler clickHandler) {
        this.mSteps = steps;
        this.mClickHandler = clickHandler;
    }

    public void addStepsData(List<Step> steps) {
        this.mSteps = steps;
        notifyDataSetChanged();
    }

    public List<Step> getSteps() {
        return mSteps;
    }

    @Override
    public StepsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.v(TAG, "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_row, parent, false);
        return new StepsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepsAdapter.ViewHolder holder, int position) {
        Log.v(TAG, "onBindViewHolder");
        if (position == 0)
            holder.description.setText(R.string.step_ingredients);
        else {
            holder.description.setText(mSteps.get(position - 1).getShortDescription());
            Log.d(TAG, "Steps onBindViewHolder " + mSteps.get(position - 1).getShortDescription());
        }
    }

    @Override
    public int getItemCount() {
        return mSteps.size() == 0 ? 0 : mSteps.size() + 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.description) TextView description;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Log.v(TAG, "onClick");
            mClickHandler.stepOnClick(getAdapterPosition());
        }
    }
}
