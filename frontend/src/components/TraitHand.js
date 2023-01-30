import React, { Component } from "react";
import TraitCard from "./TraitCard";

const hand = [];
for(let i = 0; i < 5; i++){
    hand.push(<span className="cardInHand"><TraitCard cardName={'coma'}/></span>)
} 

class TraitHand extends Component{
    
    render() {
        return(
            <React.Fragment>{
                <div>
                    {hand}
                </div>
            }
            </React.Fragment>
        )
    }
}

export default TraitHand;