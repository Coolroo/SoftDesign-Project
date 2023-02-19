import React, { Component } from "react";

class EventDiscardPile extends Component{
    render() {
        let pile = [];
        if(this.props.state.game.eventDiscard.length > 0) {
            pile.push (<img alt="" width='10%' src={"/events/eventcardback.png"}/>)
        }
    
        return(
            <React.Fragment>
                <div style={{top:'9.0%',left:'124.0%'}} className="eventDiscardPile">{pile}</div>
            </React.Fragment>
        )
    }
}

export default EventDiscardPile;