package com.kyron.coffee;

/**
 * 
 * - CoffeeApp can use different CoffeeMachine
 * - CoffeeApp calls prepareCoffee()/brew() to make coffee (specific to the given CoffeeMachine)
 * - CoffeeBean = context/param/resource = a DTO holding configuration for the desired coffee cup
 * This bean must be passed to the appropriated CoffeeMachine which knows how to use the ingredient/bean.
 * @author anh
 *
 */
public class MainApp {
	
    public static void main(String[] args) {
        // create a Map of available coffee beans
        Map<CoffeeSelection, CoffeeBean> beans = new HashMap<CoffeeSelection, CoffeeBean>();
        beans.put(CoffeeSelection.ESPRESSO, new CoffeeBean(
            "My favorite espresso bean", 1000));
        beans.put(CoffeeSelection.FILTER_COFFEE, new CoffeeBean(
            "My favorite filter coffee bean", 1000));

        // get a new CoffeeMachine object
        PremiumCoffeeMachine machine = new PremiumCoffeeMachine(beans);

        // Instantiate CoffeeApp
        CoffeeApp app = new CoffeeApp(machine);

        // brew a fresh coffee
        try {
            app.prepareCoffee(CoffeeSelection.ESPRESSO);
        } catch (CoffeeException e) {
            e.printStackTrace();
        }
    }
}
