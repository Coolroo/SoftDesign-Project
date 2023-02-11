import React, { Component } from "react";

class JoinGamePage extends Component {


    render(){
        
        let joinGame = () => {
            this.props.joinGame(document.getElementById("lobbyInput").value);
        }

        let createGame = () => {
            this.props.createGame();
        }
        return(
            <React.Fragment>{
                <div className="joinGameLobby" height={window.innerHeight} width={window.innerWidth}>
                    <div className="plagueIncTitle" style={{left:'40%', top: '10%'}}>Plague Inc.</div>
                    <div className="plagueIncSubTitle" style={{left:'35%', top: '20%'}}>The Board Game</div>
                    <div className="lobbyButton" id="joinGame" style={{left: "10%", top: "50%"}} onClick={joinGame}><p>Join Game</p></div>
                    <div className="lobbyInput" id="lobbyInput" style={{left: "10%", top: "44%"}}><input  type="text" defaultValue="Room Code" maxLength="4"/></div>
                    <div className="lobbyButton" id="createGame" style={{left: "70%", top: "45%"}} onClick={createGame}><p>Create Game</p></div>
                </div> 
        }
        </React.Fragment>
        )
    }
}

export default JoinGamePage;