import React, { Component } from "react";

class CountryCard extends Component{

    render() {
        return(
            <React.Fragment>{
                    <div className="card">
                        <img src={`/countries/${this.props.cardName}.png`}  alt="img" height="200" width="144"/>
                    </div>   
                }
            </React.Fragment>
        )
    }
}

export default CountryCard;