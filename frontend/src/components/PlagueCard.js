import React, { Component } from "react";
import PlagueCardTraitSlot from "./PlagueCardTraitSlot";

class PlagueCard extends Component{

    render() {
        return(
            <React.Fragment>{
                    <div>
                        <img src={`/plaguecards/${this.props.cardName}.png`} alt="img"/>
                        <div style={{top:'52.0%', left:'2.6%'}} className="plagueCardTraitSlot"><PlagueCardTraitSlot/></div>
                        <div style={{top:'52.0%', left:'22.5%'}} className="plagueCardTraitSlot"><PlagueCardTraitSlot/></div>
                        <div style={{top:'52.0%', left:'42.5%'}} className="plagueCardTraitSlot"><PlagueCardTraitSlot/></div>
                        <div style={{top:'52.0%', left:'62.5%'}} className="plagueCardTraitSlot"><PlagueCardTraitSlot/></div>
                        <div style={{top:'4.0%', left:'62.5%'}} className="plagueCardTraitSlot"><PlagueCardTraitSlot/></div>
                    </div>   
                }
            </React.Fragment>
        )
    }
}

export default PlagueCard;