import React from 'react';
import './App.css';
import Component1 from './Component1.js'

/**
 * Main app has the State and parent event handlers.
 * It sends to child component a part of the State and an event handler to update that part.
 * The reason is that React dataflow/control is one-way. Child cannot change Parent's state.
 * Other ways to 'callback' from child/leaf component includes Container, Context and Flux (not shown here).
 * In any case, it'll hard to maintain the State in large application.
 * React-redux can help.
 */
class App extends React.Component {
  // define the app state
  constructor(props) {
    super(props);
    this.state = 
      {
        person: {
          name: 'ANDY',
          age: 21
        },
        message: 'TEST MESSAGE'
      };
    this.appSetPerson = this.appSetPerson.bind(this);
    console.log(this.state);
  }

  // parent's event handler to pass down to child for setState.
  appSetPerson(newPerson) {
    this.setState({person: newPerson})
  }

  // note: the passed in "person" is equivalent to "props" in the component.
  render() {
    return (
      <div className="App">
        <header className="App-header">
          <h1>React Demo</h1>
          <Component1 
            person={this.state.person}
            eventHandler={this.appSetPerson}
            />
        </header>
      </div>
    );
  }
}
export default App;
