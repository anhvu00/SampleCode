
import React from "react";
import { connect } from "react-redux";

import { OFF, ON, ADD, MINUS } from './reducer';

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
      </div>
    );
  }
}

// map properties/attributes
const mapStateToProps = state => {
  return {
    person: state.person,
    message: state.message,
  };
};

// map action/event handlers. Note the mispell in mapDispaCHToProps().
const mapDispachToProps = dispatch => {
  return {
    offHandler: () => dispatch({ type: OFF, message: 'OFF' }),
    onHandler: () => dispatch({ type: ON, message: 'ON' }),
    onAgeUp: () => dispatch({ type: ADD, value: 1 }),
    onAgeDown: () => dispatch({ type: MINUS, value: 1 })
  };
};

// bind Redux to React
export default connect(
  mapStateToProps,
  mapDispachToProps
)(App);