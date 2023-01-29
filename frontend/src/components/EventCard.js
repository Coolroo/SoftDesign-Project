import React, { Component } from "react";

class EventCard extends Component{

    render() {
        return(
            <React.Fragment>{
                    <div className="card">
                        <img src={`/events/${this.props.cardName}.png`}  alt="img" height="200" width="144"/>
                    </div>   
                }
            </React.Fragment>
        )
    }
}

export default EventCard;