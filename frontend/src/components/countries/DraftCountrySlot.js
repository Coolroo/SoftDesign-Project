import React, { Component } from "react";
import CountryCard from "./CountryCard";

class DraftCountrySlot extends Component{

    render() {
        return(
            <React.Fragment>{
                <div>
                    <CountryCard cardName={this.props.name}/>
                </div>   
    }       </React.Fragment>
        )
    }
}

export default DraftCountrySlot;