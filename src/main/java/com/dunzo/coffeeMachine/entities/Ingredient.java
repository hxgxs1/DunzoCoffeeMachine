package com.dunzo.coffeeMachine.entities;


public class Ingredient {

    String name;

    public Ingredient(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Ingredient)) return false;
        Ingredient that = (Ingredient) o;
        return this.name.equals(that.name);
    }
}
