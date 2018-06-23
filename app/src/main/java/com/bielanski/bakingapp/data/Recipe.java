package com.bielanski.bakingapp.data;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

public class Recipe {
    @PrimaryKey
    private int id;
    private String name;
    @Embedded
    private List <Ingredients> ingredients;
    @Embedded
    private List <Step> steps;
    private int servings;
    private String image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredients> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredients> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        String desc = "Recipe " +
                "id " + id +
                " name " + name;

        return desc;
    }
}
