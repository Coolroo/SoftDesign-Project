import React, { Component } from "react";
import PlayerBox from "./PlayerBox";

var w = window.innerWidth;
var h = window.innerHeight
class Lobby extends Component{
    state = {
        state: this.props.state
    }

    static getDerivedStateFromProps(props, state){
        if(props.state !== state.state){
            return {state: props.state};
        }
        return null;
    }

    render(){
        let exitLobby = () => {
            console.log("EXIT CLICKED");
            this.props.exitGame();
        }


        return(<React.Fragment>{
            <div height={h} width={w} style={{marginLeft: "auto", marginRight: "auto"}}>
            {/*Player Boxes*/}
                <div style={{display: "flex", justifyContent: "center", marginTop:"17vh"}}>
                    <PlayerBox changeType={this.props.changeType} joinGame={this.props.joinGame} voteToStart={this.props.voteToStart} state={this.state.state} color="RED" />
                    <PlayerBox changeType={this.props.changeType} joinGame={this.props.joinGame} voteToStart={this.props.voteToStart} state={this.state.state} color="BLUE" />
                    <PlayerBox changeType={this.props.changeType} joinGame={this.props.joinGame} voteToStart={this.props.voteToStart} state={this.state.state} color="YELLOW" />
                    <PlayerBox changeType={this.props.changeType} joinGame={this.props.joinGame} voteToStart={this.props.voteToStart} state={this.state.state} color="PURPLE" />
                </div>
                {/*Lobby ID*/}
                <div style={{verticalAlign: "middle", marginLeft:"45%", marginRight:"45%", position:"absolute", bottom:"0"}}>
                    <div className="lobbyIdTag">Lobby ID</div>
                    <div style={{"margin-bottom":"100%"}} className="lobbyIdBox" >{this.state.state.lobbyId}</div>
                </div>
                <div style ={{verticalAlign: "left", marginLeft:"0%", position:"absolute", top:"85%", left: "44.4%"}}>
                    <div className="joinGameButton" onClick={exitLobby}>EXIT LOBBY</div>
                </div>
            </div>
            }</React.Fragment>);
    }
}

export default Lobby;