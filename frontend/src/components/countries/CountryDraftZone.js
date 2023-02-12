import React, { Component } from "react";
import DraftCountrySlot from "./DraftCountrySlot";

class CountryDraftZone extends Component{

    render() {
        if(this.props.state.revealedCountries != null){
        return(
            <React.Fragment>{
                    <div className="countryDraftZone">
                        <img src={`/CountryDraftZone.png`} alt="img"/>
                        <div style={{top:'9.0%', left:'29.4%'}} className="draftZoneCountrySlot"><DraftCountrySlot name={this.props.state.revealedCountries[0]}/></div>
                        <div style={{top:'9.4%', left:'51.3%'}} className="draftZoneCountrySlot"><DraftCountrySlot name={this.props.state.revealedCountries[1]}/></div>
                        <div style={{top:'9.8%', left:'74.7%'}} className="draftZoneCountrySlot"><DraftCountrySlot name={this.props.state.revealedCountries[2]}/></div>
                    </div>   
                }
            </React.Fragment>
        )
    }}
}

export default CountryDraftZone;