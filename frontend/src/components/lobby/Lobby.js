import React, { Component } from "react";
import PlayerBox from "./PlayerBox";

class Lobby extends Component{
    
    render(){
        
        const plagues = this.props.state.game.plagues;

        const getPlayerIcon = (color) => {
            if(plagues[color] != null){
                return ", url(/dnaTokens/" + color + plagues[color].diseaseType + ".jpg)";
            }
            return "";
        }

        const getReadyColor = (color) => {
            if(plagues[color] != null){
                return this.props.state.game.votesToStart[color] ? "green" : "red";
            }
            return "red";
        }



        return(<React.Fragment>
            {/*Player Boxes*/}
            <PlayerBox player={this.props.state.player} joinGame={this.props.joinGame} voteToStart={this.props.voteToStart} state={this.props.state} color="RED" readyColor={getReadyColor("RED")} pos={{top: "20%", left: "15%"}} playerIcon={getPlayerIcon("RED")}/>
            <PlayerBox player={this.props.state.player} joinGame={this.props.joinGame} voteToStart={this.props.voteToStart} state={this.props.state} color="BLUE" readyColor={getReadyColor("BLUE")} pos={{top: "20%", left: "35%"}} playerIcon={getPlayerIcon("BLUE")}/>
            <PlayerBox player={this.props.state.player} joinGame={this.props.joinGame} voteToStart={this.props.voteToStart} state={this.props.state} color="YELLOW" readyColor={getReadyColor("YELLOW")} pos={{top: "20%", left: "55%"}} playerIcon={getPlayerIcon("YELLOW")}/>
            <PlayerBox player={this.props.state.player} joinGame={this.props.joinGame} voteToStart={this.props.voteToStart} state={this.props.state} color="PURPLE" readyColor={getReadyColor("PURPLE")} pos={{top: "20%", left: "75%"}} playerIcon={getPlayerIcon("PURPLE")}/>
            {/*Lobby ID*/}
            <div className="lobbyIdTag" style={{left: "45%", top: "85%", transform: "transform: translate(-50%, -50%)"}}>Lobby ID</div>
            <div className="lobbyIdBox" style={{left: "45%", top: "90%", transform: "transform: translate(-50%, -50%)"}}><p>{this.props.state.lobbyId}</p></div>
        </React.Fragment>);
    }
}

export default Lobby;