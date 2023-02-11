import React, { Component } from "react";
import PlayerBox from "./PlayerBox";

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
        
        const plagues = this.state.state.game.plagues;

        const getPlayerIcon = (color) => {
            if(plagues[color] != null){
                return ", url(/dnaTokens/" + color + plagues[color].diseaseType + ".jpg)";
            }
            return "";
        }

        const getReadyColor = (color) => {
            if(plagues[color] != null){
                return this.state.state.game.votesToStart[color] ? "green" : "red";
            }
            return "red";
        }



        return(<React.Fragment>
            {/*Player Boxes*/}
            <PlayerBox joinGame={this.props.joinGame} voteToStart={this.props.voteToStart} state={this.state.state} color="RED" readyColor={getReadyColor("RED")} pos={{top: "20%", left: "15%"}} playerIcon={getPlayerIcon("RED")}/>
            <PlayerBox joinGame={this.props.joinGame} voteToStart={this.props.voteToStart} state={this.state.state} color="BLUE" readyColor={getReadyColor("BLUE")} pos={{top: "20%", left: "35%"}} playerIcon={getPlayerIcon("BLUE")}/>
            <PlayerBox joinGame={this.props.joinGame} voteToStart={this.props.voteToStart} state={this.state.state} color="YELLOW" readyColor={getReadyColor("YELLOW")} pos={{top: "20%", left: "55%"}} playerIcon={getPlayerIcon("YELLOW")}/>
            <PlayerBox joinGame={this.props.joinGame} voteToStart={this.props.voteToStart} state={this.state.state} color="PURPLE" readyColor={getReadyColor("PURPLE")} pos={{top: "20%", left: "75%"}} playerIcon={getPlayerIcon("PURPLE")}/>
            {/*Lobby ID*/}
            <div className="lobbyIdTag" style={{left: "45%", top: "85%", transform: "transform: translate(-50%, -50%)"}}>Lobby ID</div>
            <div className="lobbyIdBox" style={{left: "45%", top: "90%", transform: "transform: translate(-50%, -50%)"}}><p>{this.state.state.lobbyId}</p></div>
        </React.Fragment>);
    }
}

export default Lobby;