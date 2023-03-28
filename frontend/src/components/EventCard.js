import React, { Component } from "react";
import { Draggable } from 'react-drag-and-drop';

class EventCard extends Component{

    render() {
        return(
            <React.Fragment>{
                <Draggable className="eventCard" type={'trait'} data={this.props.index}>
                    <div>
                        <img src={`/events/${this.props.cardName}.png`} className="fullSize curvedBorder" style={{width:"100%","--borderRadius": "10px"}} alt="img"/>
                    </div>
                </Draggable>
                }
            </React.Fragment>
        )
    }
}

export default EventCard;