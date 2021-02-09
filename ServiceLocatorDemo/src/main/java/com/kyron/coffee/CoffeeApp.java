package com.kyron.coffee;

/**
 * 
 * - This class prepareCoffee/brew a single type of coffee regardless of the CoffeeMachine
 * - It uses a ServiceLocator to get a CoffeeMachine
 * - Strange. It doesn't use the CoffeeSelection passed in as a parameter.
 *
 */
public class CoffeeApp {
    public Coffee prepareCoffee(CoffeeSelection selection)
            throws CoffeeException {
            CoffeeMachine coffeeMachine = CoffeeServiceLocator.getInstance().coffeeMachine();
            Coffee coffee = coffeeMachine.brewFilterCoffee();
            System.out.println("Coffee is ready!");
            return coffee;
        }
}
