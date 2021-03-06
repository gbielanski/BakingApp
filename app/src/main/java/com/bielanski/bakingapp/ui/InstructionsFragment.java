package com.bielanski.bakingapp.ui;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bielanski.bakingapp.R;
import com.bielanski.bakingapp.data.Ingredients;
import com.bielanski.bakingapp.data.Recipe;
import com.bielanski.bakingapp.data.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class InstructionsFragment extends Fragment {
    private static final String TAG = "InstructionsFragment";
    public static final String RECIPE_LIST = "RECIPE_LIST";
    public static final String STEP_NUMBER = "STEP_NUMBER";
    public static final String RECIPE_NUMBER = "RECIPE_NUMBER";
    private final int INGREDIENCE = 0;
    private List<Recipe> recipes;
    private int recipeNumber;
    private int stepNumber;
    private Unbinder unbinder;
    @BindView(R.id.instructions_text_view) TextView instructionsTextView;

    public InstructionsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_instructions, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        if(savedInstanceState != null){
            recipes = savedInstanceState.getParcelableArrayList(RECIPE_LIST);
            recipeNumber = savedInstanceState.getInt(RECIPE_NUMBER);
            stepNumber = savedInstanceState.getInt(STEP_NUMBER);
        }

        String stepDescription = null;
        if(recipes != null) {
            for (Recipe r : recipes) {
                if (r.getId() == recipeNumber) {
                    if (stepNumber == INGREDIENCE)
                        stepDescription = getDescriptionFromIngredience(recipes);
                    else
                        stepDescription = r.getSteps().get(stepNumber - 1).getDescription();
                }
            }
        }

        instructionsTextView.setText(stepDescription == null ? "no description" : stepDescription);
        return rootView;
    }

    private int getRecipeIdx(int recipeNumber){
        int idx = 0;
        for(;idx<recipes.size();idx++){
            if(recipes.get(idx).getId() == recipeNumber)
                break;
        }
        return idx;
    }

    private String getDescriptionFromIngredience(List<Recipe> recipes) {
        StringBuilder stringBuilder = new StringBuilder();
        Log.v(TAG, "recipeNumber " + recipeNumber + " from " + recipes.size());
        ArrayList<Ingredients> ingredients = recipes.get(getRecipeIdx(recipeNumber)).getIngredients();
        for (Ingredients in : ingredients) {
            stringBuilder.append(in.getIngredient()).append(" ")
                    .append(in.getQuantity()).append(" ")
                    .append(in.getMeasure()).append("\n");
        }
        return stringBuilder.toString();
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public void setRecipeNumber(int recipeNumber) {
        this.recipeNumber = recipeNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(RECIPE_LIST, new ArrayList<>(recipes));
        outState.putInt(STEP_NUMBER, stepNumber);
        outState.putInt(RECIPE_NUMBER, recipeNumber);
        super.onSaveInstanceState(outState);
    }
}
