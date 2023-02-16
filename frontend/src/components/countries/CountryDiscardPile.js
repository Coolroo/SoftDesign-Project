import React, { Component } from "react";
import { Droppable } from 'react-drag-and-drop';

class DraftDiscardPile extends Component{
    render() {
        let pile = [];
        if(this.props.state.game.countryDiscard.length > 0) {
            pile.push (<img width='100%' src={"/countries/countrycardback.png"}/>)
        }

        let discard = (data, event) => {
            if (!this.props.state.game.readyToProceed){
                this.props.discard(data.country);
            }
        }    
    
        return(
            <React.Fragment>
                <Droppable types={['country']} onDrop={discard}>
                    <div style={{top:'9.0%',left:'102.0%'}} className="countryDiscardPile">{pile}</div>
                </Droppable>
            </React.Fragment>
        )
    }
}

export default DraftDiscardPile;