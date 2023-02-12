import React, { Component } from "react";
import DraftCountrySlot from "./DraftCountrySlot";

class CountryDraftZone extends Component{

    render() {
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
                        <img src={`/CountryDraftZone.png`} alt="img"/>
                        <div>{revealedCountries}</div>
                    </div>   
                }
            </React.Fragment>
        )
    }
}

export default CountryDraftZone;