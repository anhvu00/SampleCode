import React, { Component } from 'react';
import Moment from 'react-moment';

class Pick extends Component {

    render() {
        return (
            <div>
                <div className="container">
                    <h2>User Pick Table</h2>
                    <table className="table table-dark table-hover">
                        <thead>
                            <tr>
                                <th>Time</th>
                                <th>Matchup</th>
                                <th>Side</th>
                                <th>O/U</th>
                            </tr>
                        </thead>
                        <tbody>
                            {this.props.spreads.map(item => (
                                <tr >
                                    <td> {item.gameId} <Moment date={item.gameDateTime}
                                        parse="YYYY-MM-dd hh:mm"
                                        format="dddd, YYYY/MM/DD h:mma"></Moment>
                                    </td>
                                    <td>
                                        <p>{ item.visitor }</p>
                                        <p>{ item.home }</p>

                                    </td>
                                    <td>
                                        <button type="submit" className="btn btn-primary d-block">{ item.visitorLine }</button>
                                        <button type="submit" className="btn btn-primary d-block">{ item.homeLine }</button>
                                    </td>
                                    <td>
                                        <button type="submit" class="btn btn-primary d-block">O { item.OULine }</button>
                                        <button type="submit" class="btn btn-primary d-block">U { item.OULine }</button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>

                    <h2>Bet Card Table</h2>
                    <table className="table table-dark table-hover; let id=index">
                        <thead>
                            <tr>
                                <th>Selection</th>
                                <th>Line</th>
                                <th>Amount</th>
                                <th>(cancel)</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>SEA</td>
                                <td>-2.0</td>
                                <td>
                                    <input type="text" name="betAmt" value="100" />
                                </td>
                                <td>
                                    <button type="submit" className="btn btn-primary d-block">Delete</button>
                                </td>
                            </tr>
                            <tr>
                                <td>Over TB/NO</td>
                                <td>47.5</td>
                                <td>
                                    <input type="text" name="betAmt" value="200" />
                                </td>
                                <td>
                                    <button type="submit" className="btn btn-primary d-block">Delete</button>
                                </td>
                            </tr>

                        </tbody>
                    </table>
                    <div>
                        <button type="submit" className="btn btn-primary d-block">Send</button>
                        <button type="submit" className="btn btn-primary d-block">Clear</button>
                    </div>
                </div>
            </div>
        )
    }
}
export default Pick;