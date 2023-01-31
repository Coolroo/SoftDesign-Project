import React, { Component } from "react";

class TraitCard extends Component{

    render() {
        return(
            <React.Fragment>{
                    <div>
                        <img src={`/traitcards/${this.props.cardName}.png`} className="card" alt="img"/>
                    </div>   
                }
            </React.Fragment>
        )
    }
}

export default TraitCard;