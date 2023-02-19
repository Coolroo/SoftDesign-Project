import React, { Component } from "react";
import EventCard from "./EventCard";
class EventHand extends Component{
    
    render() {
        var hand = [];
        this.props.eventCards.forEach((card, index) => {
            hand.push(<EventCard cardName={card} index={index}/>)
        });
        return(
            <React.Fragment>{
                <div style={{display:"flex", justifyContent:"center", alignItems:"center"}}> 
                    {hand}
                </div>
            }
            </React.Fragment>
        )
    }
}

export default EventHand;