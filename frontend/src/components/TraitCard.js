import React, { Component } from "react";

class TraitCard extends Component{

    render() {
        return(
            <React.Fragment>{
                    <div className="traitCard">
                        <img src={`/images/${this.props.cardName}.png`}  alt="img" height="100" width="100"/>
                    </div>   
                }
            </React.Fragment>
        )
    }
}

export default TraitCard;