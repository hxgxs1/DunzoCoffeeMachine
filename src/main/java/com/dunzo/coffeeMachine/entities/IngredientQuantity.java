package com.dunzo.coffeeMachine.entities;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;



public class IngredientQuantity {

    private AtomicInteger quantity; // provides wrapper class for int that can be used to achieve this atomic operation without usage of Synchronization.

    public IngredientQuantity(AtomicInteger quantity) {
        this.quantity = quantity;
    }

    public Boolean  checkIfEnoughQuantity(int needed) {
        if (this.quantity.get() < needed)
            return false;

        return true;
    }

    public void updateQuantity(int delta){
        this.quantity.addAndGet(delta);
    }

    public Boolean consumeQuantity(int needed){ //Todo: check if Optional<String> makes sense
        int oldValue=quantity.getAndUpdate(quant-> quant>=needed? quant-needed : quant);
        return oldValue >= needed;
    }
}
