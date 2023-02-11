import React, { Component } from "react";

class Lobby extends Component{
    
    render(){
        const backgrounds = {
                        "RED": "radial-gradient(50% 50% at 50% 50%, rgba(0, 0, 0, 0) 0%, rgba(255, 0, 0, 0.4) 100%)",
                        "BLUE": "radial-gradient(50% 50% at 50% 50%, rgba(0, 0, 0, 0) 0%, rgba(0, 0, 255, 0.36) 100%)",
                        "YELLOW": "radial-gradient(50% 50% at 50% 50%, rgba(0, 0, 0, 0) 0%, rgba(255, 255, 0, 0.36) 100%)",
                        "PURPLE": "radial-gradient(50% 50% at 50% 50%, rgba(0, 0, 0, 0) 0%, rgba(255, 0, 199, 0.4) 100%)"
                        };
        const plagues = this.props.game.plagues;
        const playerIcons = {
            "RED": () => {
                if(plagues.RED != null){
                    return ", url(/dnaTokens/RED" + plagues.RED.diseaseType + ".jpg)";
                }
                return "";
            },
            "BLUE": () => {
                if(plagues.BLUE != null){
                    return ", url(/dnaTokens/BLUE" + plagues.BLUE.diseaseType + ".jpg)";
                }
                return "";
            },
            "YELLOW": () => {
                if(plagues.YELLOW != null){
                    return ", url(/dnaTokens/YELLOW" + plagues.YELLOW.diseaseType + ".jpg)";
                }
                return "";
            },
            "PURPLE": () => {
                if(plagues.PURPLE != null){
                    return ", url(/dnaTokens/PURPLE" + plagues.PURPLE.diseaseType + ".jpg)";
                }
                return "";
            }
        };

        const readyColors = {
            "RED": () => {
                if(plagues.RED != null){
                    return this.props.game.votesToStart["RED"] ? "green" : "red";
                }
                return "red";
            },
            "BLUE": () => {
                if(plagues.BLUE != null){
                    return this.props.game.votesToStart["BLUE"] ? "green" : "red";
                }
                return "red";
            },
            "YELLOW": () => {
                if(plagues.YELLOW != null){
                    return this.props.game.votesToStart["YELLOW"] ? "green" : "red";
                }
                return "red";
            },
            "PURPLE": () => {
                if(plagues.PURPLE != null){
                    return this.props.game.votesToStart["PURPLE"] ? "green" : "red";
                }
                return "red";
            }
        }


        return(<React.Fragment>
            <div className="playerBox" style={{top: "20%", 
                                           left: "15%", 
                                           border: "0.2vw solid " + readyColors["RED"](), 
                                           backgroundImage: backgrounds["RED"] + playerIcons["RED"](),
                                           }}/>
            <div className="playerBox" style={{top: "20%", 
                                           left: "35%", 
                                           border: "0.2vw solid " + readyColors["BLUE"](), 
                                           backgroundImage: backgrounds["BLUE"] + playerIcons["BLUE"](),}}/>
            <div className="playerBox" style={{top: "20%", 
                                           left: "55%", 
                                           border: "0.2vw solid " + readyColors["YELLOW"](), 
                                           backgroundImage: backgrounds["YELLOW"]+ playerIcons["YELLOW"](),
                                           }}/>
            <div className="playerBox" style={{top: "20%", 
                                           left: "75%", 
                                           border: "0.2vw solid " + readyColors["PURPLE"](), 
                                           backgroundImage: backgrounds["PURPLE"] + playerIcons["PURPLE"](),}}/>
            <div className="lobbyIdTag" style={{left: "45%", top: "85%", transform: "transform: translate(-50%, -50%)"}}>Lobby ID</div>
            <div className="lobbyIdBox" style={{left: "45%", top: "90%", transform: "transform: translate(-50%, -50%)"}}><p>{this.props.lobbyId}</p></div>
        </React.Fragment>);
    }
}

export default Lobby;