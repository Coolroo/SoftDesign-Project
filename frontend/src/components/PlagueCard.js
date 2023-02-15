import React, { Component } from "react";
import PlagueCardTraitSlot from "./PlagueCardTraitSlot";

class PlagueCard extends Component{

    render() {

        let locs = [
            {top:"52.0%", left:"2.6%"},
            {top:'52.0%', left:'22.5%'},
            {top:'52.0%', left:'42.5%'},
            {top:'52.0%', left:'62.5%'},
            {top:'4.0%', left:'62.5%'}
        ];

        let cards = [];

        let plagueCard = (loc, index) => {
            return <PlagueCardTraitSlot evolve={this.props.evolve} loc={loc} index={index}/>
        }

        locs.forEach((loc, index) => {
            cards.push(plagueCard(loc, index));
        })

        
        return(
            <React.Fragment>{
                    <div className="plagueCard">
                        {cards}
                        <img src={`/plaguecards/${this.props.cardName}.png`} alt="img" className="fullSize"/>
                        
                    </div>   
                }
            </React.Fragment>
        )
    }
}

export default PlagueCard;