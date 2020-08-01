package com.dunzo.coffeeMachine.api;

import com.dunzo.coffeeMachine.datastore.InMemoryDB;
import com.dunzo.coffeeMachine.entities.Ingredient;

public class IngredientApi {

    private InMemoryDB db;

    public IngredientApi(){
        db=InMemoryDB.getInstance();
    }

    public void refillIngredient(Ingredient ingredient, Integer quantity){
        db.refillIngredient(ingredient, quantity);
    }

}
