import React, { Component } from "react";

class BoardDNASlot extends Component{

   
    render() {
        console.log(this.props.state.game.plagues.length)
        let tokens = [];
    
        if (this.props.state.game.plagues.RED != null){
            if(this.props.state.game.plagues.RED.dnaPoints == this.props.score){
                tokens.push (<img src={`/dnaTokens/RED${this.props.state.game.plagues.RED.diseaseType}.jpg`} className="card"alt="img"/>)
            }
        }

        if (this.props.state.game.plagues.BLUE != null){
            if(this.props.state.game.plagues.BLUE.dnaPoints == this.props.score){
                tokens.push (<img src={`/dnaTokens/BLUE${this.props.state.game.plagues.BLUE.diseaseType}.jpg`} className="card"alt="img"/>)
            }
        }

        if (this.props.state.game.plagues.YELLOW != null){
            if(this.props.state.game.plagues.YELLOW.dnaPoints == this.props.score){
                tokens.push (<img src={`/dnaTokens/YELLOW${this.props.state.game.plagues.YELLOW.diseaseType}.jpg`} className="card"alt="img"/>)
            }
        }

        if (this.props.state.game.plagues.PURPLE != null){
            if(this.props.state.game.plagues.PURPLE.dnaPoints == this.props.score){
                tokens.push (<img src={`/dnaTokens/PURPLE${this.props.state.game.plagues.PURPLE.diseaseType}.jpg`} className="card"alt="img"/>)
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