import React, { Component } from "react";
import CountryCard from "./CountryCard";

class BoardCountrySlot extends Component{

    state = {
        cardRef: null
    }

    render() {

        console.log(this.props.state.game);
        const className = (this.props.state.game.choppingBlock && this.props.state.game.choppingBlock.includes(this.props.country.countryName) ? "pulseAnim" : "");
        return(
            <React.Fragment>{
                <div className={"boardCountrySlot " + className} style={{...this.props.loc}} ref={this.state.cardRef}>
                    <CountryCard kill={this.props.kill} state={this.props.state} infect={this.props.infect} country={this.props.country}/>
                </div>   
    }       </React.Fragment>
        )
    }
}

export default BoardCountrySlot;