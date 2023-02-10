import React, { Component } from "react";

class JoinGamePage extends Component {

    state = {
        lobbyId: "Please enter lobby ID"
    }
    render(){
        var board = "orangebacteria";
        return(
            <React.Fragment>{
                <div className="joinGameLobby" height={window.innerHeight} width={window.innerWidth}>
                    <div className="lobbyButton" id="joinGame" style={{left: "20%", top: "40%"}}><p>Join Game</p></div>
                </div> 
        }
        </React.Fragment>
        )
    }
}

export default JoinGamePage;