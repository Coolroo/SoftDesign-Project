import React, { Component } from "react";

class BoardDNASlot extends Component{

   
    render() {
        let tokens = [];
        for(let i = 0; i < this.props.state.game.plagues.length; i++) {
            if(this.props.state.plagues[i].dnaPoints == this.props.score){
                tokens.push (<img src={`/dnaTokens/${this.props.state.plagues[i].color}${this.props.state.plagues[i].diseaseType}.jpg`} className="card"alt="img"/>)
            }
        }

    
        return(
            <React.Fragment>{
                <div>{tokens}</div>
            }       
            </React.Fragment>
        )

        
    }
}

export default BoardDNASlot;