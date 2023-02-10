import React, { Component } from "react";
import GameView from "./GameView";
import Dropdown from 'react-bootstrap/Dropdown';
import { DropdownButton } from "react-bootstrap";
import * as SockJS from 'sockjs-client';
import { Stomp } from "@stomp/stompjs";

const postRequestOptions = {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
}; 

const patchRequestOptions = {
    method: 'PATCH',
    headers: { 'Content-Type': 'application/json' }
};

const SOCKET_URL = 'http://localhost:8080/plague-socket';

const SERVER_URL = "http://localhost:8080";

class GameController extends Component{

    state = {
        game: {
            board: {
            AFRICA: [],
            NORTH_AMERICA: [],
            SOUTH_AMERICA: [],
            OCEANIA: [],
            ASIA: [],
            EUROPE: []
            }
        },
        player: {
            hand: [],
            eventCards: [],
            plague: null
        },
       dropdownText: "Please select color",
       playerId: null,
       socket: null,
       lobbyId: null
        
    };

    

    async createGame(){
        this.gameID = await fetch(SERVER_URL + `/createGame`, postRequestOptions)
            .then(function(response) {
                return response.text();
            }).then(function(data) {
                console.log(data); // this will be a string
            });
    }

    async getState(id){
        await fetch(SERVER_URL + '/gameState?gameStateId=' + id)
            .then(function(response) {
                return response.json();
            }).then(data => {
                console.log(data);
                this.updateGameState(data);
            })
    }

    updateGameState(data){
        this.setState((prevState) => {
            return {
                ...prevState,
                game: {
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
                }
            };
        },
        () => {});
    }

    async joinGame(id){
        this.patchRequest("/joinGame", id, JSON.stringify({plagueColor: this.state.dropdownText}))
        .then(response => {
            if(response.ok) {
                response.text().then(text => {
                    this.setState((prevState) => {
                        return {...prevState,
                        playerId: text.replace(/['"]+/g, ''),
                        lobbyId: id
                }}, () => {
                        console.log("Player ID = " + text);
                        console.log("State = " + JSON.stringify(this.state));
                        this.initWebSocket();
                        this.getPlayerInfo();
                });        
                });
                }
        });
        
        
    }

    async patchRequest(endpoint, lobbyId, body){
        var patchBody = {
            ...patchRequestOptions,
            body
            };
            console.log(JSON.stringify(patchBody));
        return fetch(SERVER_URL + endpoint + "?gameStateId=" + lobbyId, patchBody);
    }

    initWebSocket(){
        var socket = new SockJS(SOCKET_URL);
        var stompClient = Stomp.over(socket);
        stompClient.connect({}, frame => {
            stompClient.subscribe("/games/gameState/"+this.state.lobbyId, (body) => {
                console.log("Websocket updated state: " + body.body);
                const jsonBody = JSON.parse(body.body);
                this.setState((prevState) => {
                    return {...prevState,
                    game: jsonBody
                    }
                });
                this.getPlayerInfo();
            });
        })
    }

    async getPlayerInfo(){
        fetch(SERVER_URL + "/getPlayerInfo?gameStateId=" + this.state.lobbyId + "&playerId=" + this.state.playerId)
                .then(resp => {
                    if(resp.ok){
                        resp.text().then(playerInfo => {
                            this.setState((prevState) => {
                                return {
                                    ...prevState,
                                    player: JSON.parse(playerInfo)
                                }
                            }, () => {
                                console.log("Player loaded: " + playerInfo);
                            });
                            
                        });
                    }
                });
    }

    changeColor(color){
        this.setState((prevState) => { 
            return {...prevState,
            dropdownText: color};
        });
    }
       
    render() {
        return(
            <React.Fragment>{
                <div>
                    <button onClick={()=>this.createGame()}>Create Game</button>

                    <form><input type="text" id="joinID" name="joinID"/></form>
                    <button onClick={()=>this.getState(document.getElementById('joinID').value)}>Get State</button>
                    <DropdownButton id="dropdown-item-button" title={this.state.dropdownText} className="format">
                        <Dropdown.Item as="button"><div onClick={(e) => this.changeColor(e.target.textContent)}>RED</div></Dropdown.Item>
                        <Dropdown.Item as="button"><div onClick={(e) => this.changeColor(e.target.textContent)}>ORANGE</div></Dropdown.Item>
                        <Dropdown.Item as="button"><div onClick={(e) => this.changeColor(e.target.textContent)}>YELLOW</div></Dropdown.Item>
                        <Dropdown.Item as="button"><div onClick={(e) => this.changeColor(e.target.textContent)}>BLUE</div></Dropdown.Item>
                        <Dropdown.Item as="button"><div onClick={(e) => this.changeColor(e.target.textContent)}>PURPLE</div></Dropdown.Item>
                    </DropdownButton>
                    <button onClick={()=>this.joinGame(document.getElementById('joinID').value).title}>Join Game</button>
                
                    <GameView state={this.state.game} player={this.state.player}/>
                </div>   
            }       
            </React.Fragment>
        )
    }
}

export default GameController;