import React, { Component } from "react";
import PlagueCardTraitSlot from "./PlagueCardTraitSlot";

class PlagueCard extends Component{

    render() {

        let locs = [
            {top:"50.0%", left:"1.25%"},
            {top:'50.0%', left:'21.25%'},
            {top:'50.0%', left:'41.25%'},
            {top:'50.0%', left:'61.25%'},
            {top:'1.0%', left:'61.25%'}
        ];

        let cards = [];

        let plagueCard = (loc, index) => {
            return <PlagueCardTraitSlot trait={this.props.state.player.plague.traitSlots[index].card} evolve={this.props.evolve} loc={loc} index={index}/>
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