import React, { Component } from "react";

class TraitCard extends Component{

    render() {
        return(
            <React.Fragment>{
                    <div className="traitCard">
                        <img src={`/traitcards/${this.props.cardName}.png`}  alt="img"/>
                    </div>   
                }
            </React.Fragment>
        )
    }
}

export default TraitCard;