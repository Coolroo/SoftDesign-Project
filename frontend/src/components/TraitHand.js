import React, { Component } from "react";
import TraitCard from "./TraitCard";
class TraitHand extends Component{
    
    render() {
        var hand = [];
        this.props.hand.forEach((card, index) => {
            hand.push(<TraitCard cardName={card} index={index}/>)
        });
        return(
            <React.Fragment>{
                <div style={{display:"flex", justifyContent:"center", alignItems:"center"}}> 
                    {hand}
                </div>
            }
            </React.Fragment>
        )
    }
}

export default TraitHand;