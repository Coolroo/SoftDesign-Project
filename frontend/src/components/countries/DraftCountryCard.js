import React, { Component } from "react";
import { Draggable } from 'react-drag-and-drop';

class DraftCountryCard extends Component{
  render(){
    let cardName = this.props.cardName;

        return(
              <Draggable type='country' data={this.props.cardName}>
                    <img src={`/countries/${cardName}.png`} className="fullSize curvedBorder" style={{"--borderRadius": "10px"}}/>
              </Draggable>
        )
  }
}

export default DraftCountryCard;