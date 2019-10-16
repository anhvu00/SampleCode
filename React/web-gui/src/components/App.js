import React, { Component } from 'react';
import '../css/App.css';

import Spread from './Spread';
import Pick from './Pick';
import ListAppointments from './ListAppointments';

class App extends Component {
  constructor() {
    super();
    this.state = {
      myAppointments: [],
      mySpreads: [],
      myPicks: [],
      lastIndex: 0
    };
  }

  componentDidMount() {
    fetch('./data.json')
      .then(response => response.json())
      .then(result => {
        const apts = result.map(item => {
          return item;
        });
        this.setState({
          myAppointments: apts
        });
      });
    fetch('./data-spread.json')
      .then(response => response.json())
      .then(result => {
        const tempAry = result.map(item => {
          item.gameId = this.state.lastIndex;
          // increment artificial index
          this.setState({ lastIndex: this.state.lastIndex + 1 })
          return item;
        });
        this.setState({
          mySpreads: tempAry
        });
      });
    // picks
    fetch('./data-winloss.json')
      .then(response => response.json())
      .then(result => {
        const tempAry = result.map(item => {
          return item;
        });
        this.setState({
          myPicks: tempAry
        });
      });
  }

  render() {
    return (
      <main className="page bg-white" id="petratings">
        <div className="container">
          <div className="row">
            <div className="col-md-12 bg-white">
              <div className="container">
                <Spread spreads={this.state.mySpreads} />
                <Pick
                  spreads={this.state.mySpreads}
                  picks={this.state.myPicks} />
                <ListAppointments appointments={this.state.myAppointments} />
              </div>
            </div>
          </div>
        </div>
      </main>
    );
  }
}

export default App;
