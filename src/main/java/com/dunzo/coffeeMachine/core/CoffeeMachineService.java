package com.dunzo.coffeeMachine.core;

import com.dunzo.coffeeMachine.datastore.InMemoryDB;
import com.dunzo.coffeeMachine.entities.Beverage;
import com.dunzo.coffeeMachine.entities.Ingredient;
import com.dunzo.coffeeMachine.entities.IngredientQuantity;
import com.dunzo.coffeeMachine.exception.IngredientNotAvailableException;
import com.dunzo.coffeeMachine.exception.NotEnoughQuantityException;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CoffeeMachineService {

    private InMemoryDB db;
    private int slots;
    private ExecutorService executorService;


    public CoffeeMachineService(){
        db=InMemoryDB.getInstance();
    }

    public CoffeeMachineService(int slots, Map<Ingredient, Integer> totalQuantity, List<Beverage> beverageList){
        this();
        db.createBeverages(beverageList);
        db.createInventory(totalQuantity);
        this.slots=slots;
        this.executorService= Executors.newFixedThreadPool(slots);
    }


    /**
     *  check if all the ingredients are available
     * @param beverage
     * @throws IngredientNotAvailableException
     */
    private void checkIfIngredientsAvailable(Beverage beverage) throws IngredientNotAvailableException {

        Set<Ingredient> ingredientsInMachine=db.getAllIngredients();
        for(Ingredient ingredient: beverage.getIngredients().keySet()) {
            if (!ingredientsInMachine.contains((ingredient))) {
                throwNotAvailableException(beverage, ingredient);
            }
        }
    }

    /**
     * check if all the ingredients are present in sufficient quantities
     * @param beverage
     * @throws NotEnoughQuantityException
     */
    private void checkIfIngredientsSufficient(Beverage beverage) throws NotEnoughQuantityException{

        for(Map.Entry<Ingredient, Integer> i: beverage.getIngredients().entrySet()){
            IngredientQuantity inMachineQuantity=db.getQuantity(i.getKey());
            if(!inMachineQuantity.checkIfEnoughQuantity(i.getValue()).isPresent()){
                throwInsufficientException(beverage, i.getKey());
            }
        }
    }

    /**
     *  make the beverage & update inventory
     * @param beverage
     * @throws NotEnoughQuantityException
     */
    private void makeBeverage(Beverage beverage) throws NotEnoughQuantityException {

        for(Map.Entry<Ingredient, Integer> i: beverage.getIngredients().entrySet()){
            IngredientQuantity inMachineQuantity=db.getQuantity(i.getKey());
            if(!inMachineQuantity.consumeQuantity(i.getValue()).isPresent()){
                throwInsufficientException(beverage, i.getKey());
            }
        }
    }

    /**
     *
     * @param beverageName
     * @return
     * @throws IngredientNotAvailableException
     * @throws NotEnoughQuantityException
     */
    public Future<Beverage> getBeverage(String beverageName){
        Beverage beverage = db.getBeverage(beverageName);


        Callable<Beverage> makeBeverageTask = () -> {
            checkIfIngredientsAvailable(beverage);  // check if all the ingredients are available
            checkIfIngredientsSufficient(beverage); // check if all the ingredients are present in sufficient quantities
            makeBeverage(beverage); // make the beverage & update inventory
            System.out.println(beverageName + " is prepared");
            return beverage;
        };

       Future<Beverage> beverageFut= executorService.submit(makeBeverageTask);
        return beverageFut;
    }

    private void throwNotAvailableException(Beverage beverage, Ingredient ingredient) throws IngredientNotAvailableException {
        System.out.println(beverage.getName() + " cannot be prepared because " + ingredient.getName() + " is not available");
        throw new IngredientNotAvailableException(beverage.getName() + " cannot be prepared because " + ingredient.getName() + " is not available");
    }

    private void throwInsufficientException(Beverage beverage, Ingredient ingredient) throws NotEnoughQuantityException{
        System.out.println(beverage.getName() + " cannot be prepared because " + ingredient.getName() + " is not sufficient");
        throw new NotEnoughQuantityException(beverage.getName() + " cannot be prepared because " + ingredient.getName() + " is not sufficient");
    }

    public void cleanUp(){
        executorService.shutdown();
    }

}
