package com.bielanski.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class InstructionsFragment extends Fragment {
    private final int INGREDIENCE = 0;
    private List<Recipe> recipes;
    private int recipeNumber;
    private int stepNumber;

    public InstructionsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_instructions, container, false);
        TextView instructionsTextView = rootView.findViewById(R.id.instructions_text_view);
        String stepDescription = null;
        for (Recipe r : recipes) {
            if (r.getId() == recipeNumber) {
                if (stepNumber == INGREDIENCE)
                    stepDescription = getDescriptionFromIngredience(recipes);
                else
                    stepDescription = r.getSteps().get(stepNumber - 1).getDescription();
            }
        }

        instructionsTextView.setText(stepDescription == null ? "no description" : stepDescription);
        return rootView;
    }

    private String getDescriptionFromIngredience(List<Recipe> recipes) {
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<Ingredients> ingredients = recipes.get(recipeNumber).getIngredients();
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
}
