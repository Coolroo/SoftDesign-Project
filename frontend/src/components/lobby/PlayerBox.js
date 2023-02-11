import React, { Component } from "react";

class PlayerBox extends Component{

    state = {
        buttonText: this.props.state.playerId == null ? "Join Game" : "Vote to Start"
    }
    render(){
        let buttonClick = () => {
            if(this.props.state.playerId == null){
                this.props.joinGame(this.props.color);
                this.setState({buttonText: "Vote to Start"});
            }else if(this.props.color === this.props.player.plague.color){
                this.props.voteToStart();
            }
        }
        const lobbyButton = () => {
            if((this.props.state.playerId == null && this.props.state.game.plagues[this.props.color] == null) || (this.props.player.plague != null && this.props.color === this.props.player.plague.color)){
                return <div className="joinGameButton" id={this.props.color} style={{left: this.props.pos.left, top: "40%"}} onClick={buttonClick}>{this.state.buttonText}</div>
            }
    }
        return(
            <React.Fragment>
                <div className="playerBox" style={{...this.props.pos, 
                                           border: "0.2vw solid " + this.props.readyColor, 
                                           backgroundImage: this.props.background + this.props.playerIcon,
                                           }}/>

                {lobbyButton()}
            </React.Fragment>
        )
    }
}
export default PlayerBox;