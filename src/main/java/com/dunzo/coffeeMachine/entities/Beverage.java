package com.dunzo.coffeeMachine.entities;

import java.util.Map;

public class Beverage {

    private String name;
    private Map<Ingredient, Integer> ingredients;


    public Beverage(String name, Map<Ingredient, Integer> ingredients) {
        this.name = name;
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Ingredient, Integer> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Map<Ingredient, Integer> ingredients) {
        this.ingredients = ingredients;
    }
}
