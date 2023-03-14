import React, { Component } from "react";

class EventCard extends Component{

    render() {
        return(
            <React.Fragment>{
                    <div>
                        <img src={`/events/${this.props.cardName}.png`} className="fullSize curvedBorder" style={{width:"100%","--borderRadius": "10px"}} alt="img"/>
                    </div>   
                }
            </React.Fragment>
        )
    }
}

export default EventCard;