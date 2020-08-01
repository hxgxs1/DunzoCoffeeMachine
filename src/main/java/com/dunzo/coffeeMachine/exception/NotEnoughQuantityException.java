package com.dunzo.coffeeMachine.exception;

import com.dunzo.coffeeMachine.entities.Ingredient;

public class NotEnoughQuantityException extends Exception {

    public NotEnoughQuantityException(String message) {
        super(message);
    }
}
