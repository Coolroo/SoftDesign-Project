import React, { Component } from "react";
import GameView from "./GameView";

const postRequestOptions = {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
}; 

class GameController extends Component{

    state = {
        board: {
            AFRICA: [],
            NORTH_AMERICA: [],
            SOUTH_AMERICA: [],
            OCEANIA: [],
            ASIA: [],
            EUROPE: []
        }
        
        
    };

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
            .then(function(response) {
                return response.json();
            }).then(data => {
                console.log(data);
                this.setState((prevState) => {
                    return {
                        ...prevState,
                    board: data.board,
                    countryDiscard: data.countryDiscard,
                    currTurn: data.currTurn,
                    eventDiscard:data.eventDiscard,
                    eventPlayer:data.eventPlayer,
                    plagues: data.plagues,
                    playState: data.plagues,
                    readyToProceed: data.readyToProceed,
                    revealedCountries: data.revealedCountries,
                    suddenDeath: data.suddenDeath,
                    traitDiscard: data.traitDiscard,
                    turnOrder: data.turnOrder,
                    votesToStart: data.votesToStart
                    };
                },
                () => {});
            })
    }
       
    render() {
        console.log(this.state.board.AFRICA)
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