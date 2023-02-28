import React, { Component } from "react";
import EventCard from "./EventCard";
import { Draggable } from 'react-drag-and-drop';

class EventHand extends Component{
    
    render() {
        var hand = [];
        this.props.eventCards.forEach((card, index) => {
            hand.push(<Draggable type='event' data={card} className="fullSize curvedBorder" style={{"--borderRadius": "10px"}}>
                        <EventCard cardName={card} index={index}/>
                     </Draggable>)
        });
        return(
            <React.Fragment>{
                
                    <div className="traitCard" style={{display:"flex", justifyContent:"center", alignItems:"center"}}> 
                        {hand}
                    </div>
                
            }
            </React.Fragment>
        )
    }
}

export default EventHand;