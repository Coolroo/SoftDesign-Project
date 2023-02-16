import React, { Component } from "react";
import { Draggable } from 'react-drag-and-drop';

class DraftCountryCard extends Component{
  render(){
    let cardName = this.props.cardName;
    console.log(cardName);

        return(
              <Draggable type='country' data={this.props.cardName}>
                <div>
                    <img src={`/countries/${cardName}.png`} className="card curvedBorder" style={{"--borderRadius": "10px"}} alt="img"/>
                </div>
              </Draggable>
        )
  }
}

export default DraftCountryCard;