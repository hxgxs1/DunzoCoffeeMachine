package com.dunzo.coffeeMachine.datastore;

import com.dunzo.coffeeMachine.entities.Beverage;
import com.dunzo.coffeeMachine.entities.Ingredient;
import com.dunzo.coffeeMachine.entities.IngredientQuantity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryDB {

    private static InMemoryDB instance;

    private ConcurrentHashMap<Ingredient, IngredientQuantity> inventory;
    private Map<String, Beverage> beverages;

    private InMemoryDB(){
        inventory=new ConcurrentHashMap<Ingredient, IngredientQuantity>();
        beverages=new HashMap<String, Beverage>();
    }



    synchronized public static InMemoryDB getInstance(){
        if(instance==null){
            instance=new InMemoryDB();
        }
        return instance;
    }

    public void createInventory(Map<Ingredient, Integer> ingredients){
        for(Map.Entry<Ingredient, Integer> e : ingredients.entrySet()){
            inventory.put(e.getKey(), new IngredientQuantity(new AtomicInteger(e.getValue())));
        }
    }

    public void createBeverages(List<Beverage> beveragesList){
        for(Beverage b: beveragesList){
            beverages.put(b.getName(), b);
        }
    }

    public Beverage getBeverage(String name){
        return beverages.get(name);
    }

    public Set<Ingredient> getAllIngredients(){
        return inventory.keySet();
    }

    public IngredientQuantity getQuantity(Ingredient ingredient){
        //todo: override equals in Ingredient
        return inventory.get(ingredient);
    }

    public void refillIngredient(Ingredient ingredient, Integer quantity){

        IngredientQuantity ingredientQuantity=inventory.get(ingredient);
        ingredientQuantity.updateQuantity(quantity);
        inventory.put(ingredient, ingredientQuantity);
    }

}
