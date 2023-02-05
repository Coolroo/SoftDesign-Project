import React, { Component } from "react";
import GameView from "./GameView";

const postRequestOptions = {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
}; 

class GameController extends Component{
    gameID
    gameState;
    UUID;

    async createGame(){
        this.gameID = await fetch(`http://localhost:8080/createGame`, postRequestOptions)
            .then(function(response) {
                return response.text();
            }).then(function(data) {
                console.log(data); // this will be a string
            });
    }

    async getState(id){
        await fetch('http://localhost:8080/gameState?gameStateId=' + id)
            .then((response) => response.json())
            .then((data) => this.gameState = data);
        this.forceUpdate();
    }
       
    render() {
        console.log(this.gameState)
        return(
            <React.Fragment>{
                <div>
                    <button onClick={()=>this.createGame()}>Create Game</button>

                    <form><input type="text" id="joinID" name="joinID"/></form>
                    <button onClick={()=>this.getState(document.getElementById('joinID').value)}>Get State</button>

                    <GameView state={this.gameState}/>
                </div>   
            }       
            </React.Fragment>
        )
    }
}

export default GameController;