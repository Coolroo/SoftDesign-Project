import React, { Component } from "react";

class PlagueCard extends Component{

    render() {
        return(
            <React.Fragment>{
                    <div>
                        <img src={`/plaguecards/${this.props.cardName}.png`} alt="img"/>
                    </div>   
                }
            </React.Fragment>
        )
    }
}

export default PlagueCard;