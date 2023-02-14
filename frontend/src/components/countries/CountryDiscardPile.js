import React, { Component } from "react";
import { Droppable } from 'react-drag-and-drop';

class DraftDiscardPile extends Component{
    render() {
        let discard = (data, event) => {
            console.log("discard")
            this.props.discard(data.country);
        }    
    
        return(
            <Droppable types={['country']} onDrop={discard}>
                <div style={{top:'9.0%',left:'102.0%'}} className="countryDiscardPile"></div>
            </Droppable>
        )
    }
}

export default DraftDiscardPile;