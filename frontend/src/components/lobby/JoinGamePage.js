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
                <div className="joinGameLobby" height={window.innerHeight} width={window.innerWidth} style={{verticalAlign:"middle"}}>
                    <div style={{marginLeft: "auto", marginRight: "auto"}}>
                        <div style={{"margin-top":"6%"}} className="plagueIncTitle"><u>Pla</u>g<u>ue Inc.</u></div>
                        <div className="plagueIncSubTitle">The Board Game</div>
                    </div>
                    <div style={{display:"flex", justifyContent:"center", marginRight:"auto", marginLeft:"auto", marginTop:"10%"}}>
                        <div style={{ marginRight:"auto", marginLeft:"auto", width:"20%"}}>
                            <div className="lobbyInput"><input  type="text" id="lobbyInput" defaultValue="Room Code" maxLength="4"/></div>
                            <div className="lobbyButton" id="joinGame" onClick={joinGame}>Join Game</div>
                        </div>
                        <div className="lobbyButton" id="createGame" style={{marginRight: "auto", marginLeft:"auto", width:"20%"}} onClick={createGame}>Create Game</div>
                    </div>
                </div> 
        }
        </React.Fragment>
        )
    }
}

export default JoinGamePage;