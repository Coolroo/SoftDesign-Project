import React, { Component } from "react";
import DraftCountrySlot from "./DraftCountrySlot";
import CountryDiscardPile from "./CountryDiscardPile";

class CountryDraftZone extends Component {

    render(){
        console.log("rendering");
        const DRAFT_SLOTS = [
            {top:'4.0%', left:'29.4%'},
            {top:'4%', left:'51.3%'},
            {top:'4%', left:'74.7%'}
        ]

        let revealedCountries = [];
        for(let i = 0; i < this.props.state.game.revealedCountries.length; i++) {
            revealedCountries.push (<div style={DRAFT_SLOTS[i]} className="draftZoneCountrySlot"><DraftCountrySlot name={this.props.state.game.revealedCountries[i]}/></div>)
        }


        return(
            <React.Fragment>{
                    <div className="countryDraftZone">
                        <img src={`/CountryDraftZone.png`} className="curvedBorder fullSize" alt="img"/>
                        <div style={{top:'4.0%',left:'6.7%'}} className="draftZoneCountrySlot"><DraftCountrySlot name={"countrycardback"}/></div>
                        <div>{revealedCountries}</div>
                        <CountryDiscardPile state={this.props.state} discard={(countryName) => this.props.discard(countryName)}/>
                    </div>   
                }
            </React.Fragment>
        )
            }
}

export default CountryDraftZone;