import React, { Component } from "react";
import { Draggable } from 'react-drag-and-drop';

class DraftCountryCard extends Component{
  render(){
    let cardName = this.props.cardName;

        return(
          <div style={{width: "100%", height:"100%", position:"absolute"}}>
              <Draggable type='country' data={this.props.cardName} className="fullSize curvedBorder" style={{"--borderRadius": "10px"}}>
                    <img src={`/countries/${cardName}.png`} className="fullSize" alt="img"/>
              </Draggable>
          </div>
        )
  }
}

export default DraftCountryCard;