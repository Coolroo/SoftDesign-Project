import React, { Component } from "react";
import { Droppable } from 'react-drag-and-drop';

class DraftDiscardPile extends Component{
    openDiscardBrowser(){
        console.log("COUNTRY DISCARD PILE OPENED")
        var x = document.getElementById("countryDiscardBrowser");
        if (x.style.display === "none") {
            x.style.display = "block";
        } else {
            x.style.display = "none";
        }
    }
    
    render() {
        let pile = [];
        if(this.props.state.game.countryDiscard.length > 0) {
            pile.push (<img style={{display: "block", "margin-left": "auto", "margin-right": "auto", width: "90%"}} alt="" width='90%' src={"/countries/countrycardback.png"}/>)
        }

        let discardList = [];
        for(let i = 0; i < this.props.state.game.countryDiscard.length; i++) {
            discardList.push (<img alt="" style={{"margin-left" : "1px", "margin-right" : "1px"}} width='20%' src={"/countries/" + this.props.state.game.countryDiscard[i] + ".png"}/>)
        }

        let discard = (data, event) => {
            if (!this.props.state.game.readyToProceed){
                this.props.discard(data.country);
            }
        }    
    
        return(
            <React.Fragment>
                <Droppable types={['country']} onDrop={discard}>
                    <div onClick={this.openDiscardBrowser} style={{top:'6.0%',left:'102.0%'}} className="countryDiscardPile">{pile}</div>
                    <div style={{"display" : "none"}} class="discardBrowser countryDiscardBrowser" id="countryDiscardBrowser">Country Discard<div>{discardList}</div></div>
                </Droppable>
            </React.Fragment>
        )
    }
}

export default DraftDiscardPile;