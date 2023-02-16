import React, { Component } from "react";
import CountryCard from "./CountryCard";

class BoardCountrySlot extends Component{

    state = {
        cardRef: null
    }

    render() {
        return(
            <React.Fragment>{
                <div className="boardCountrySlot" style={{...this.props.loc}} ref={this.state.cardRef}>
                    <CountryCard kill={this.props.kill} state={this.props.state} infect={this.props.infect} country={this.props.country}/>
                </div>   
    }       </React.Fragment>
        )
    }
}

export default BoardCountrySlot;