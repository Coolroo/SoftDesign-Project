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



        return(<React.Fragment>{
            <div height={h} width={w} style={{marginLeft: "auto", marginRight: "auto"}}>
            {/*Player Boxes*/}
                <div style={{display: "flex", justifyContent: "center", marginTop:"15vh"}}>
                    <PlayerBox changeType={this.props.changeType} joinGame={this.props.joinGame} voteToStart={this.props.voteToStart} state={this.state.state} color="RED" readyColor={getReadyColor("RED")} pos={{top: "20%", left: "15%"}} playerIcon={getPlayerIcon("RED")}/>
                    <PlayerBox changeType={this.props.changeType} joinGame={this.props.joinGame} voteToStart={this.props.voteToStart} state={this.state.state} color="BLUE" readyColor={getReadyColor("BLUE")} pos={{top: "20%", left: "35%"}} playerIcon={getPlayerIcon("BLUE")}/>
                    <PlayerBox changeType={this.props.changeType} joinGame={this.props.joinGame} voteToStart={this.props.voteToStart} state={this.state.state} color="YELLOW" readyColor={getReadyColor("YELLOW")} pos={{top: "20%", left: "55%"}} playerIcon={getPlayerIcon("YELLOW")}/>
                    <PlayerBox changeType={this.props.changeType} joinGame={this.props.joinGame} voteToStart={this.props.voteToStart} state={this.state.state} color="PURPLE" readyColor={getReadyColor("PURPLE")} pos={{top: "20%", left: "75%"}} playerIcon={getPlayerIcon("PURPLE")}/>
                </div>
                {/*Lobby ID*/}
                <div style={{verticalAlign: "middle", marginLeft:"45%", marginRight:"45%", position:"absolute", bottom:"0"}}>
                    <div className="lobbyIdTag">Lobby ID</div>
                    <div className="lobbyIdBox" >{this.state.state.lobbyId}</div>
                </div>
            </div>
            }</React.Fragment>);
    }
}

export default Lobby;