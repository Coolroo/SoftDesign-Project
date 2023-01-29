import React, { Component } from "react";

class EventCard extends Component{

    render() {
        return(
            <React.Fragment>{
                    <div>
                        <img src={`/events/${this.props.cardName}.png`} className="card" alt="img"/>
                    </div>   
                }
            </React.Fragment>
        )
    }
}

export default EventCard;