package com.bielanski.bakingapp;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bielanski.bakingapp.data.Recipe;

import java.util.List;


public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {

    private List<Recipe> mRecipes;
    private final OnClickRecipeHandler mClickHandler;
    private String TAG = "RecipesAdapter";

    public interface OnClickRecipeHandler{
        void recipeOnClick(int position);
    }
    public RecipesAdapter(List<Recipe> recipes, OnClickRecipeHandler clickHandler) {
        this.mRecipes = recipes;
        this.mClickHandler = clickHandler;
    }

    public void addRecipeData(List<Recipe> recipes){
        this.mRecipes = recipes;
        notifyDataSetChanged();
    }

    public List<Recipe> getRecipes(){
        return mRecipes;
    }

    @Override
    public RecipesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipesAdapter.ViewHolder holder, int position) {
        holder.name.setText(mRecipes.get(position).getName());
        holder.serving.setText(String.valueOf(mRecipes.get(position).getServings())); }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView name;
        private TextView serving;

        ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            serving = itemView.findViewById(R.id.serving);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.v(TAG, "onClick");
            mClickHandler.recipeOnClick(getAdapterPosition());
        }
    }
}
