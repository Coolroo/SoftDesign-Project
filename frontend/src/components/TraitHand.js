import React, { Component } from "react";
import TraitCard from "./TraitCard";
class TraitHand extends Component{
    
    render() {
        var hand = [];
        this.props.hand.forEach(card => {
            hand.push(<TraitCard cardName={card}/>)
        });
        return(
            <React.Fragment>{
                <div>
                    <span style={{display:"flex", justifyContent:"center", alignItems:"center"}}>
                        {hand}
                    </span>
                </div>
            }
            </React.Fragment>
        )
    }
}

export default TraitHand;