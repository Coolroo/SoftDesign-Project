import React, { Component } from "react";
import { Draggable } from 'react-drag-and-drop';

class TraitCard extends Component{

    render() {
        return(
            <React.Fragment>{
                    <Draggable className="traitCard" type={'trait'} data={{index: this.props.index}}>
                        <div>
                            <img src={`/traitcards/${this.props.cardName}.png`} className="fullSize" alt="img"/>
                        </div>
                    </Draggable>
                }
            </React.Fragment>
        )
    }
}

export default TraitCard;