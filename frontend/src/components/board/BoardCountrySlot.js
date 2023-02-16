import React, { Component } from "react";
import CountryCard from "./CountryCard";

class BoardCountrySlot extends Component{

    render() {
        return(
            <React.Fragment>{
                <div className="boardCountrySlot" style={{...this.props.loc}}>
                    <CountryCard infect={this.props.infect} country={this.props.country}/>
                </div>   
    }       </React.Fragment>
        )
    }
}

export default BoardCountrySlot;