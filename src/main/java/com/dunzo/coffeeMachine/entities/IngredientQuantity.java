package com.dunzo.coffeeMachine.entities;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;



public class IngredientQuantity {

    private AtomicInteger quantity; // provides wrapper class for int that can be used to achieve this atomic operation without usage of Synchronization.

    public IngredientQuantity(AtomicInteger quantity) {
        this.quantity = quantity;
    }

    public Optional<String>  checkIfEnoughQuantity(int needed) {
        if (this.quantity.get() < needed)
            return Optional.empty();
        return Optional.of("Enough ingredient for this beverage");
    }

    public void updateQuantity(int delta){
        this.quantity.addAndGet(delta);
    }

    public Optional<String> consumeQuantity(int needed){
        int oldValue=quantity.get();
        if(oldValue<needed)
            return Optional.empty();
        quantity.getAndAdd(-needed);
        return Optional.of("Inventory has been updated");
    }
}
