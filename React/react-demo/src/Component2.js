
import React from "react";

// try to update pick 
// NOTE: this.state (cannot be this.localState), comma in setState
class Component2 extends React.Component {
    constructor(props) {
        super(props);
        this.state = props.cart;
        // can't do: this.setState(props.cart);
        console.log('local state');
        console.log(this.state);
        this.handleClickEdit = this.handleClickEdit.bind(this);
        this.handleClickAdd = this.handleClickAdd.bind(this);
    }
    render() {
        return (
            <div className="Component2">
                <p>game1_side: {this.state.game1_side}</p>
                <p>game1_ou: {this.state.game1_ou}</p>
                <p>game2_side: {this.state.game2_side}</p>
                <button onClick={() => this.handleClickEdit('game1_side','NE -4.5')}>Change</button>
                {console.log('after edit state')}
                {console.log(this.state)}
            </div>
        );
    }
    handleClickEdit(key, value) {        
        // update local state 
        this.setState({
            [key]:value,
        });       
        //console.log(`key=${[key]}, value=${value}, state.game1_side=${this.state.game1_side}`);       
    }
    
    handleClickAdd(key, value) {         
        
        //console.log(`key=${[key]}, value=${value}, state.game1_side=${this.state.game1_side}`);       
    }

}

export default Component2;