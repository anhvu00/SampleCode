package com.kyron.coffee;

/**
 * 
 * 
 *
 */
public class CoffeeServiceLocator {
    private static CoffeeServiceLocator locator;

    private CoffeeMachine coffeeMachine;

    private CoffeeServiceLocator() {
        // configure and instantiate a CoffeeMachine
        Map<CoffeeSelection, CoffeeBean> beans = new HashMap<CoffeeSelection, CoffeeBean>();
        beans.put(CoffeeSelection.ESPRESSO, new CoffeeBean(
            "My favorite espresso bean", 1000));
        beans.put(CoffeeSelection.FILTER_COFFEE, new CoffeeBean(
            "My favorite filter coffee bean", 1000));
        coffeeMachine = new PremiumCoffeeMachine(beans);
    }

    public static CoffeeServiceLocator getInstance() {
        if (locator == null) {
            locator = new CoffeeServiceLocator();
        }
        return locator;
    }

    public CoffeeMachine coffeeMachine() {
        return coffeeMachine;
    }
}
