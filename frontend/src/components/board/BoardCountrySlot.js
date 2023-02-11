import React, { Component } from "react";
import CountryCard from "../countries/CountryCard";

class BoardCountrySlot extends Component{

    render() {
        return(
            <React.Fragment>{
                <div>
                    <CountryCard country={this.props.country}/>
                </div>   
    }       </React.Fragment>
        )
    }
}

export default BoardCountrySlot;