
import React from "react";

class Component1 extends React.Component {
    constructor(props) {
        super(props);
        console.log('prop:'); console.log(props);
        this.localState = props.person;
        console.log('local state:'); console.log(this.localState);
        this.handleClickAdd = this.handleClickAdd.bind(this);
    }
    render() {
        return (
            <div className="Component1">
                <p>Name: {this.props.person.name}</p>
                <p>Age: <span>{this.props.person.age}</span> </p>
                <button onClick={this.handleClickAdd}>Age Up</button>
            </div>
        );
    }
    handleClickAdd() {
        const newAge = this.props.person.age + 1;
        // update local state and call parent's event handler
        this.localState.age = newAge;
        this.props.eventHandler(this.localState);
    }
    // shouldComponentUpdate(newProps) {
    //     const isNewName = (this.props.person.name !== newProps.person.name);
    //     const isNewAge = (this.props.person.age !== newProps.person.age);
    //     return (isNewName || isNewAge);
    // }
}

export default Component1;