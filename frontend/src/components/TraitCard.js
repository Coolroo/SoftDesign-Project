import React, { Component } from "react";

class TraitCard extends Component{

    render() {
        return(
            <React.Fragment>
                {
                    <div className="traitCard">
                        <span className="traitCardCost">COST</span>
                        <span className="traitCardName">NAME</span>
                    </div>
                }
            </React.Fragment>
        );
    }
}

export default TraitCard;