package com.dunzo.coffeeMachine;

import com.dunzo.coffeeMachine.core.CoffeeMachineService;
import com.dunzo.coffeeMachine.entities.Beverage;
import com.dunzo.coffeeMachine.entities.Ingredient;
import com.dunzo.coffeeMachine.exception.IngredientNotAvailableException;
import com.dunzo.coffeeMachine.exception.NotEnoughQuantityException;
import jdk.nashorn.internal.runtime.ECMAException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.Assert.*;


public class MachineTest {

    private final ByteArrayOutputStream output	= new ByteArrayOutputStream();
    private CoffeeMachineService service;

    @Before
    public void init(){
        System.setOut(new PrintStream(output));
        Ingredient hotWater = new Ingredient("hot_water");
        Ingredient hotMilk = new Ingredient("hot_milk");
        Ingredient gingerSyrup = new Ingredient("ginger_syrup");
        Ingredient sugarSyrup = new Ingredient("sugar_syrup");
        Ingredient teaLeavesSyrup = new Ingredient("tea_leaves_syrup");
        Ingredient greenMixture = new Ingredient("green_mixture");

        List<Beverage> beverages=new ArrayList<>();

        //Initial load into the Machine.
        Map<Ingredient, Integer> totalQuantity=new HashMap<>();
        totalQuantity.put(hotWater, 500);
        totalQuantity.put(hotMilk, 500);
        totalQuantity.put(gingerSyrup, 100);
        totalQuantity.put(sugarSyrup, 100);
        totalQuantity.put(teaLeavesSyrup, 100);


        // create hot_tea
        Map<Ingredient, Integer> hotTeaIngredients=new HashMap<>();
        hotTeaIngredients.put(hotWater, 200);
        hotTeaIngredients.put(hotMilk, 100);
        hotTeaIngredients.put(gingerSyrup, 10);
        hotTeaIngredients.put(sugarSyrup, 10);
        hotTeaIngredients.put(teaLeavesSyrup, 30);
        Beverage hotTea=new Beverage("hot_tea", hotTeaIngredients);
        beverages.add(hotTea);
        // create hot_coffee
        Map<Ingredient, Integer> hotCoffeeIngredients=new HashMap<>();
        hotCoffeeIngredients.put(hotWater, 100);
        hotCoffeeIngredients.put(hotMilk, 400);
        hotCoffeeIngredients.put(gingerSyrup, 30);
        hotCoffeeIngredients.put(sugarSyrup, 50);
        hotCoffeeIngredients.put(teaLeavesSyrup, 30);
        Beverage hotCoffee=new Beverage("hot_coffee", hotCoffeeIngredients);
        beverages.add(hotCoffee);

        // create black_tea
        Map<Ingredient, Integer> blackTeaIngredients=new HashMap<>();
        blackTeaIngredients.put(hotWater, 300);
        blackTeaIngredients.put(gingerSyrup, 30);
        blackTeaIngredients.put(sugarSyrup, 50);
        blackTeaIngredients.put(teaLeavesSyrup, 30);
        Beverage blackTea=new Beverage("black_tea", blackTeaIngredients);
        beverages.add(blackTea);

        // create green_tea
        Map<Ingredient, Integer> greenTeaIngredients=new HashMap<>();
        greenTeaIngredients.put(hotWater, 100);
        greenTeaIngredients.put(gingerSyrup, 30);
        greenTeaIngredients.put(sugarSyrup, 50);
        greenTeaIngredients.put(greenMixture, 30);
        Beverage greenTea=new Beverage("green_tea", greenTeaIngredients);
        beverages.add(greenTea);

        this.service=new CoffeeMachineService(3, totalQuantity, beverages);

    }

    @After
    public void cleanUp()
    {
        System.setOut(null);
        service.cleanUp();
    }

    @Test
    public void test() throws NotEnoughQuantityException, IngredientNotAvailableException, ExecutionException, InterruptedException {
        Throwable caught = null;
        Future<Beverage> tea=service.getBeverage("hot_tea");
        assertEquals("hot_tea", tea.get().getName());

        Future<Beverage> coffee=service.getBeverage("hot_coffee");
        assertEquals("hot_coffee", coffee.get().getName());
        try {
            Future<Beverage> greenTea = service.getBeverage("green_tea");
            assertEquals("green_tea", greenTea.get().getName());
        }catch(Exception e){
            caught=e;
        }
        assertNotNull(caught);
        assertEquals(caught.getMessage(), "com.dunzo.coffeeMachine.exception.IngredientNotAvailableException: green_tea cannot be prepared because green_mixture is not available");


        try {
            Future<Beverage> blackTea = service.getBeverage("black_tea");
            assertEquals("black_tea", blackTea.get().getName());
        }catch(Exception e){
            caught=e;
        }
        assertNotNull(caught);
        assertEquals(caught.getMessage(), "com.dunzo.coffeeMachine.exception.NotEnoughQuantityException: black_tea cannot be prepared because sugar_syrup is not sufficient");
    }


    @Test
    public void test2() throws NotEnoughQuantityException, IngredientNotAvailableException, ExecutionException, InterruptedException {
        Throwable caught = null;
        Future<Beverage> tea=service.getBeverage("hot_tea");
        assertEquals("hot_tea", tea.get().getName());

        Future<Beverage> blackTea=service.getBeverage("black_tea");
        assertEquals("black_tea", blackTea.get().getName());
        try {
            Future<Beverage> greenTea = service.getBeverage("green_tea");
            assertEquals("green_tea", greenTea.get().getName());
        }catch(Exception e){
            caught=e;
        }
        assertNotNull(caught);
        assertEquals(caught.getMessage(), "com.dunzo.coffeeMachine.exception.IngredientNotAvailableException: green_tea cannot be prepared because green_mixture is not available");

        try {
            Future<Beverage> hotCoffee = service.getBeverage("hot_coffee");
            assertEquals("hot_coffee", hotCoffee.get().getName());
        }catch(Exception e){
            caught=e;
        }
        assertNotNull(caught);
        assertEquals(caught.getMessage(), "com.dunzo.coffeeMachine.exception.NotEnoughQuantityException: hot_coffee cannot be prepared because hot_water is not sufficient");
    }

    @Test
    public void test3() throws NotEnoughQuantityException, IngredientNotAvailableException, ExecutionException, InterruptedException {
        Throwable caught = null;
        Future<Beverage> hotCoffee = service.getBeverage("hot_coffee");
        assertEquals("hot_coffee", hotCoffee.get().getName());

        Future<Beverage> blackTea=service.getBeverage("black_tea");
        assertEquals("black_tea", blackTea.get().getName());
        try {
            Future<Beverage> greenTea = service.getBeverage("green_tea");
            assertEquals("green_tea", greenTea.get().getName());
        }catch(Exception e){
            caught=e;
        }
        assertNotNull(caught);
        assertEquals(caught.getMessage(), "com.dunzo.coffeeMachine.exception.IngredientNotAvailableException: green_tea cannot be prepared because green_mixture is not available");

        try {
            Future<Beverage> tea=service.getBeverage("hot_tea");
            assertEquals("hot_tea", tea.get().getName());
        }catch(Exception e){
            caught=e;
        }
        assertNotNull(caught);
        assertEquals(caught.getMessage(), "com.dunzo.coffeeMachine.exception.NotEnoughQuantityException: hot_tea cannot be prepared because hot_water is not sufficient");
    }

}