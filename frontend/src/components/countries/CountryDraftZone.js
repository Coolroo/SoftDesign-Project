import React, { Component } from "react";
import DraftCountrySlot from "./DraftCountrySlot";
import CountryDiscardPile from "./CountryDiscardPile";

class CountryDraftZone extends Component {

    render(){
        console.log("rendering");
        const DRAFT_SLOTS = [
            {top:'9.0%', left:'29.4%'},
            {top:'9.4%', left:'51.3%'},
            {top:'9.8%', left:'74.7%'}
        ]

        let revealedCountries = [];
        for(let i = 0; i < this.props.state.game.revealedCountries.length; i++) {
            revealedCountries.push (<div style={DRAFT_SLOTS[i]} className="draftZoneCountrySlot"><DraftCountrySlot name={this.props.state.game.revealedCountries[i]}/></div>)
        }


        return(
            <React.Fragment>{
                    <div className="countryDraftZone">
                        <img src={`/CountryDraftZone.png`} className="curvedBorder" alt="img"/>
                        <div style={{top:'9.0%',left:'6.7%'}} className="draftZoneCountrySlot"><DraftCountrySlot name={"countrycardback"}/></div>
                        <div>{revealedCountries}</div>
                        <div style={{top:'9.0%',left:'102.0%'}} className="countryDiscardPile"><CountryDiscardPile/></div>
                    </div>   
                }
            </React.Fragment>
        )
            }
}

export default CountryDraftZone;