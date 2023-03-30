import React, { Component } from "react";

class EventDiscardPile extends Component{
    openDiscardBrowser(){
        console.log("EVENT DISCARD PILE OPENED")
        var x = document.getElementById("eventDiscardBrowser");
        if (x.style.display === "none") {
            x.style.display = "block";
        } else {
            x.style.display = "none";
        }
    }

    render() {
        let pile = [];
        if(this.props.state.game.eventDiscard.length > 0) {
            pile.push (<img alt="" width='10%' src={"/events/eventcardback.png"}/>)
        }

        let discardList = [];
        for(let i = 0; i < this.props.state.game.eventDiscard.length; i++) {
            discardList.push (<img alt="" style={{"margin-left" : "1px", "margin-right" : "1px"}} width='20%' src={"/events/" + this.props.state.game.eventDiscard[i] + ".png"}/>)
        }
    
        return(
            <React.Fragment>
                <div onClick={this.openDiscardBrowser} style={{top:'6.0%',left:'130.0%'}} className="eventDiscardPile">{pile}</div>
                <div style={{"display" : "none"}} class="discardBrowser eventDiscardBrowser" id="eventDiscardBrowser">Event Discard<div>{discardList}</div></div>
            </React.Fragment>
        )
    }
}

export default EventDiscardPile;