import React, { Component } from "react";

class DraftCountryCard extends Component{
    render() {
        return(
            <React.Fragment>{
                <div>
                    <img src={`/countries/${this.props.cardName}.png`} className="card"alt="img"/>
                </div>
            }       
            </React.Fragment>
        )


    }
}

export default DraftCountryCard;