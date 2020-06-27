import React from 'react';
import './App.css';
import Component1 from './Component1.js'
import Component2 from './Component2';

/**
 * Main app has the State and parent event handlers.
 * It sends to child component a part of the State and an event handler to update that part.
 * The reason is that React dataflow/control is one-way. Child cannot change Parent's state.
 * Other ways to 'callback' from child/leaf component includes Container, Context and Flux (not shown here).
 * In any case, it'll hard to maintain the State in large application.
 * React-redux can help.
 */
class App extends React.Component {

  // mock input
  pickList = ['game2_ou', 'game3_side'];

  // define the app state
  constructor(props) {
    super(props);
    this.state =
      {
        person: {
          name: 'ANDY',
          age: 21
        },
        message: 'TEST MESSAGE',
        cart: {
          game1_side: 'LAR -2.0',
          game1_ou: 'Over 46 LAR@SD',
          game2_side: 'WAS +3.0',
        }
      };
    this.appSetPerson = this.appSetPerson.bind(this);

    // add some more state values
    // this.pickList.map(
    //   (key) => this.setState({
    //     cart: {
    //       [key]: 'null',
    //     },
    //   }));

  }

  componentDidMount() {
    let newState = {...this.state};
    newState.cart = {...this.state.cart, game5_side: 'abc'};
    console.log('new state');
    console.log(newState);
    this.setState({
      newState,
    });
    console.log('after mounted state');
    console.log(this.state);
  }

  // parent's event handler to pass down to child for setState.
  appSetPerson(newPerson) {
    this.setState({ person: newPerson })
  }

  // note: the passed in "person" is equivalent to "props" in the component.
  render() {
    return (
      <div className="App">
        <h1>React Demo</h1>
        <Component1
          person={this.state.person}
          eventHandler={this.appSetPerson}
        />
        <Component2
          cart={this.state.cart} />
      </div>
    );
  }
}
export default App;
