
/*
Purpose:
This javascript file changes the state's person or message depending on the action type.
Note:
- The constants (action types) can be string or number, doesn't matter.
- The initial state represents the structure of the Store.
It is very important to maintain this structure in the rootReducer function 
because rootReducer always return a "new state" (see pure function definition).
If your view does not update/re-render, it is because you change the local state and
Redux did not know that the state has changed.
- Action ON/OFF changes the state's message.
- Action ADD/MINUS changes the state's person.
Todo:
- A store.js might be useful to allow other Components to access the Store.
- Can there be more than one reducers (required redux.combineReducer function)? When do we need them?
*/
import { createStore } from "redux";

// constants.js
export const ON = 'ACTIVATE';
export const OFF = 'DEACTIVATE';
export const ADD = 2;
export const MINUS = 3;
export const ADD_REMINDER = '+REMINDER';
export const DEL_REMINDER = '-REMINDER';
// shopping cart variables
export const CART_ADD = 'ADD ITEM TO CART';
export const CART_EDIT = 'EDIT ITEM IN CART';
export const CART_DEL = 'DELETE ITEM IN CART';

// store.js
// This is The state structure
export const initialState = {
    message: 'SYSTEM ON',
    person: {
        name: 'ANDY',
        age: 21
    },
    reminderAry: [],
    cartData: { one:1 },
}

// Reducer. input = initialState and action. output = newState created by the action.
export function rootReducer(state = initialState, action) {
    let newState = { ...state }
    //console.log('rootreducer before state:'); console.log(newState);
    switch (action.type) {
        case ON:
            newState.message = 'SYSTEM ON'
            break;
        case OFF:
            newState.message = 'SYSTEM OFF';
            break;
        case ADD:
            newState.person = {
                name: newState.person.name,
                age: newState.person.age + action.value
            }
            break;
        case MINUS:
            newState.person = {
                name: newState.person.name,
                age: newState.person.age - action.value
            }
            break;
        case ADD_REMINDER:
            // keep old ary values, add new value.
            newState.reminderAry = [...state.reminderAry, action.payload];
            break;
        case DEL_REMINDER:
            // use js array filter function
            newState.reminderAry = [...state.reminderAry.filter(
                reminder => reminder !== action.payload
            )];
            break;
        case CART_ADD:
            newState.cartData = { ...state.cartData, ...action.payload };
            // console.log('cart add state');
            // console.log(newState.cartData);
            break;
        case CART_EDIT:
            newState.cartData = { ...state.cartData, ...action.payload };
            break;
        case CART_DEL:
            // loop through a list of carData items, keep non-matched payload 
            newState.cartData = Object.keys(state.cartData).reduce((r, e) => {
                if (!action.payload[e]) r[e] = state.cartData[e];
                return r
            }, {})
            
            // console.log('cart delete state');
            // console.log(newState.cartData);
            break;
        default:
        // do nothing. newState is state
    }
    console.log('rootreducer new state:'); console.log(newState);
    return newState;
}

export const store = createStore(rootReducer);
