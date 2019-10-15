import React, { Component } from 'react';
import { FaTimes } from 'react-icons/fa';
import Moment from 'react-moment';

class Spread extends Component {
    /*
    render() {
        const listItems = this.props.spreads.map(item => (
            <div>
                <div>{item.gameDateTime}</div>
                <div>{item.visitor}</div>
                <div>{item.home}</div>
            </div>
        ));
        return <div>{listItems}</div>;
    } */
    render() {
        return (
            <div className="weekly-spread">
                <h2>Weekly Spread Table</h2>
                <table class="table table-dark table-hover">
                    <thead>
                        <tr>
                            <th>Time</th>
                            <th>Matchup</th>
                            <th>Line</th>
                            <th>O/U</th>
                        </tr>
                    </thead>
                    <tbody>
                        {this.props.spreads.map(item => (
                        <tr>
                            <td> {item.gameDateTime} </td>
                            <td>
                                <p>{item.visitor}</p>
                                <p>{item.home}</p>
                            </td>
                            <td>
                                <p>{item.visitorLine}</p>
                                <span contenteditable="true" >{item.homeLine}</span>
                            </td>
                            <td>
                                <p>---</p>
                                <span contenteditable="true" >{item.OULine}</span>
                            </td>
                        </tr>
                        ))}
                    </tbody>
                </table>
                <div class="form-group">
                    <button type="submit" class="btn btn-primary">Save</button>
                </div>
            </div >
        );
    } // render
}

export default Spread;