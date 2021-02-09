12/31/20 - Collection of Design Patterns
Could be a parent maven project
Design patterns:
- Factory
Behavior patterns:

A. State:
Goal is to avoid if-else/switch for different action per state
Implementation is
1. Interface IState defines a common method doAction() 
2. Concrete implementation class TVStartState, TVStopState turns on/off (i.e. implement doAction())
3. The TVContext also implements IState so that it could keep the current state and launch action for that state.
Note: Context does not add any intelligence/logic to go from one state to another. It's the job for Main()
Usage
1. Create concrete implementation classes (TVStartState, TVStopState, TVContext)
2. Add a specific state object (i.e. TVStartState or TVStopState) to the context. This is where your logic goes.
3. Call context.doAction()
4. Repeat for all states based on your logic flow.
Note: Redux Store contains state elements (not actions) for the app. When you bind actions to the Store, you have a Context.
Redux executes one action at a time to maintain the integrity of the application state.

B. Strategy:
Goal is to allow the caller (i.e. shopping cart) to process a different algorithm (i.e. pay by credit or paypal).
In another word, it avoids if-else/switch to select an algorithm = similar to State Strategy but without Context.
Implementation is
1. Interface IPaymentStrategy defines a common method pay(amount)
2. Concrete implementation class CreditCardPayment, PaypalPayment pays the amount (i.e. implement pay(amount))
3. The ShoppingCart keeps items, calculates total, and process payment using the common IStrategy.pay(amount)
Usage
1. Create concrete implementation classes (CreditCardPayment, PaypalPayment)
2. Create the ShoppingCart, add items, create the payment object, call cart.process(payment object).
Note: You can use only 1 type of payment per shopping cart. Step 2 is your logic.
You keep items in cart which is an intermediate step (encapsulation). Cart knows the total charge and sends it to the proper payment object.
There is no Context for payment, just an action pay(amount). This is the different from State Strategy.
