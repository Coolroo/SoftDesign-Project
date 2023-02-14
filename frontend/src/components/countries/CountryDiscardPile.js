import React, { Component } from "react";
import { Droppable } from 'react-drag-and-drop';

class DraftDiscardPile extends Component{
    handleDrop(data, event) {
        console.log("DISCARDED " + data); // 'bar'
    }

    render() {
        let discard = () => {
            this.props.discard();
        }    

        return(
            <React.Fragment>{
                <Droppable types={['countryCard']} onDrop={this.handleDrop}>
                </Droppable>
            }       
            </React.Fragment>
        )
    }
}

export default DraftDiscardPile;