import React, { Component } from "react";
import TraitCard from "./TraitCard";
class TraitHand extends Component{
    
    render() {
        var hand = [];
        this.props.hand.forEach(card => {
            hand.push(<span className="cardInHand"><TraitCard cardName={card}/></span>)
        });
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