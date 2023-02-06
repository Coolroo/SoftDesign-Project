import React, { Component } from "react";
import BoardCountrySlot from "./BoardCountrySlot";

class PlagueCard extends Component{

    render() {
        return(
            <React.Fragment>{
                    <div className="countryDraftZone">
                        <img src={`/CountryDraftZone.png`} alt="img"/>
                        <div style={{top:'2.4%', left:'29.4%'}} className="draftZoneCountrySlot"><BoardCountrySlot/></div>
                        <div style={{top:'2.8%', left:'51.3%'}} className="draftZoneCountrySlot"><BoardCountrySlot/></div>
                        <div style={{top:'3.2%', left:'74.7%'}} className="draftZoneCountrySlot"><BoardCountrySlot/></div>
                    </div>   
                }
            </React.Fragment>
        )
    }
}

export default PlagueCard;