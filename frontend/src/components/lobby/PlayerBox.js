import React, { Component } from "react";

class PlayerBox extends Component{

    state = {
        buttonText: this.props.state.playerId == null ? "Join Game" : "Vote to Start",
        state: this.props.state
    }


    static getDerivedStateFromProps(props, state){
        if(props.state !== state.state){
            return {...state,
                state: props.state};
        }
        return null;
    }
    render(){
        const backgrounds = {
            "RED": "radial-gradient(50% 50% at 50% 50%, rgba(0, 0, 0, 0) 0%, rgba(255, 0, 0, 0.4) 100%)",
            "BLUE": "radial-gradient(50% 50% at 50% 50%, rgba(0, 0, 0, 0) 0%, rgba(0, 0, 255, 0.36) 100%)",
            "YELLOW": "radial-gradient(50% 50% at 50% 50%, rgba(0, 0, 0, 0) 0%, rgba(255, 255, 0, 0.36) 100%)",
            "PURPLE": "radial-gradient(50% 50% at 50% 50%, rgba(0, 0, 0, 0) 0%, rgba(255, 0, 199, 0.4) 100%)"
            };
        let buttonClick = () => {
            if(this.state.state.playerId == null){
                this.props.joinGame(this.props.color);
                this.setState({buttonText: "Vote to Start"});
            }else if(this.props.color === this.state.state.player.plague.color){
                this.props.voteToStart();
            }
        }
        const lobbyButton = () => {
            if((this.state.state.playerId == null && this.state.state.game.plagues[this.props.color] == null) || (this.state.state.player.plague != null && this.props.color === this.state.state.player.plague.color)){
                return <div className="joinGameButton" id={this.props.color} style={{left: this.props.pos.left, top: "40%"}} onClick={buttonClick}>{this.state.buttonText}</div>
            }
        }

        let whenClicked = () => {
            //TODO: add check for player color, then call change color
            console.log("clicked");
            this.props.changeType();
        }
        return(
            <React.Fragment>
                <div className="playerBox" style={{...this.props.pos, 
                                           border: "0.2vw solid " + this.props.readyColor, 
                                           backgroundImage: backgrounds[this.props.color] + this.props.playerIcon,
                                           }} onClick={whenClicked}/>

                {lobbyButton()}
            </React.Fragment>
        )
    }
}
export default PlayerBox;