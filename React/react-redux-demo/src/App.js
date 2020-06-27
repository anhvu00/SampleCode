
import React from "react";
import { connect } from "react-redux";

import { OFF, ON, ADD, MINUS, ADD_REMINDER, DEL_REMINDER, CART_ADD, CART_EDIT, CART_DEL } from './reducer';

/*
Purpose:
Demo minimal features of react-redux: a Store with 2 sections, map the state to Redux, 
map updating state methods, register/bind/connect react with redux.
It shows how we modify different parts of the state (message and person).
Note:
- The Store is created in reducer.js and used in index.js
- Creating the Store needs a reducer which needs a default state representing the 
JSON structure of the store. This reducer can be a redux.combinedReducer() containing 
multiple reducers (not in this demo).
- The Provider in index.js passes the store to App.js as props
- We define the event handlers (onHandler, offHandler, onAgeUp, onAgeDown) in mapDispatchToProps()
thus, such reference as "this.props.onHandler" is valid though the handler is not from original "props".
To do:
- There are other ways to define the event handlers.
- When to use Thunk?
*/

export class App extends React.Component {
  // part 1 = Button ON/OFF
  // part 2 = Button ADD/MINUS. Test update different parts of the State.
  // part 3 = Button ADD/DEL_REMINDER. Add and delete a reminder to an array.
  render() {
    console.log('App:'); console.log(this.props);
    return (
      <div>
        <div>
          <h2>{this.props.message}</h2>
          {(this.props.message === 'SYSTEM ON') ? (
            <button onClick={this.props.offHandler}>TURN OFF</button>
          ) : (
              <button onClick={this.props.onHandler}>TURN ON</button>
            )}
        </div>
        <div>
          <p>Name: {this.props.person.name}</p>
          <p>Age: {this.props.person.age}</p>
          <button onClick={this.props.onAgeUp}>Age Up</button>
          <button onClick={this.props.onAgeDown}>Age Down</button>
        </div>
        <div>
          <p>Reminder: {this.props.reminderAry}</p>
          <button onClick={this.props.onAddReminder}>Add Reminder</button>
          <button onClick={this.props.onDelReminder}>Delete Reminder</button>
        </div>
        <div>
          <p>Cart: {this.props.cartData.one}</p>
          <button onClick={this.props.onCartAdd}>Add item to cart</button>
          <button onClick={this.props.onCartEdit}>Edit item in cart</button>
          <button onClick={this.props.onCartDel}>Delete item in cart</button>
        </div>
      </div>
    );
  }
}

// map properties/attributes
const mapStateToProps = state => {
  return {
    person: state.person,
    message: state.message,
    reminderAry: state.reminderAry,
    cartData: state.cartData,
  };
};

// map action/event handlers. Note the mispell in mapDispaCHToProps().
const mapDispachToProps = dispatch => {
  return {
    offHandler: () => dispatch({ type: OFF, message: 'OFF' }),
    onHandler: () => dispatch({ type: ON, message: 'ON' }),
    onAgeUp: () => dispatch({ type: ADD, value: 1 }),
    onAgeDown: () => dispatch({ type: MINUS, value: 1 }),
    onAddReminder: () => dispatch({ type: ADD_REMINDER, payload: 'BUY STAMPS'}),
    onDelReminder: () => dispatch({ type: DEL_REMINDER, payload: 'BUY STAMPS'}),
    // note: click n times, only add once because same name "Pick"
    onCartAdd: () => dispatch({ type: CART_ADD, payload: {
      Pick: {
        name: 'LAR -3.0', 
        amount: 1}
      } }),
    // note: replace the only pick above with this pick
    onCartEdit: () => dispatch({ type: CART_EDIT, payload:  {
      Pick_2: {
        name: 'Over 46 LAR@BAL', 
        amount: 200}
      } }),
    // note: delete the pick with same name ("Pick" or "Pick_2")
    // onCartDel: () => dispatch({ type: CART_DEL, payload:  {
    //   Pick: {
    //     name: 'LAR -3.0', 
    //     amount: 200}
    //   } }),
    onCartDel: () => dispatch( createPickContent ),  
  }; 
};

// is it possible to create a helper function for dispatch()?
let createPickContent = 
   { type: CART_DEL, payload:  {
    Pick_2: {
      name: 'LAR -3.0', 
      amount: 200}
    } };


// bind Redux to React
export default connect(
  mapStateToProps,
  mapDispachToProps
)(App);