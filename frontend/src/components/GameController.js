import React, { Component } from "react";
import GameView from "./GameView";
import JoinGamePage from "./lobby/JoinGamePage";
import Dropdown from 'react-bootstrap/Dropdown';
import { DropdownButton } from "react-bootstrap";
import * as SockJS from 'sockjs-client';
import { Stomp } from "@stomp/stompjs";
import Lobby from "./lobby/Lobby";

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
                NORTH_AMERICA: [],
                EUROPE: [],
                ASIA: [],
                SOUTH_AMERICA: [],
                AFRICA: [],
                EUROPE: []
            },
            plagues: {
                RED: null,
                BLUE: null,
                YELLOW: null,
                PURPLE: null
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
            }).then((data) => {
                console.log(data); // this will be a string
                this.loadLobby(data);
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

    async joinGame(){
        if(this.state.lobbyId == null){
            console.log("No lobby ID");
            return;
        }
        this.patchRequest("/joinGame", this.state.lobbyId, JSON.stringify({plagueColor: this.state.dropdownText}))
        .then(response => {
            if(response.ok) {
                response.text().then(text => {
                    this.setState((prevState) => {
                        return {...prevState,
                        playerId: text.replace(/['"]+/g, ''),
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

    async loadLobby(id){
        if(this.state.socket != null){
            console.log("Cannot join another lobby if you are already in one");
        }
        fetch(SERVER_URL + "/gameState?gameStateId=" + id)
        .then(resp => {
            if(resp.ok){
                resp.json().then(gameState => {
                    this.setState((prevState) => {
                        return {
                            ...prevState,
                            game: gameState,
                            lobbyId: id
                        }
                    }, () => {
                        console.log("Game loaded: " + JSON.stringify(gameState));
                        this.initWebSocket();
                    });
                });
            }
        });
    }

    initWebSocket(){
        var socket = new SockJS(SOCKET_URL);
        var stompClient = Stomp.over(socket);
        stompClient.connect({}, frame => {
                this.setState((prevState) => {
                    return {
                        ...prevState,
                        socket: stompClient
                    }
                }, () => {
                    stompClient.subscribe("/games/gameState/"+this.state.lobbyId, (body) => {
                    console.log("Websocket updated state: " + body.body);
                    const jsonBody = JSON.parse(body.body);
                    this.setState((prevState) => {
                        return {...prevState,
                        game: jsonBody,
                        socket: stompClient,
                        }
                    });
                    this.getPlayerInfo();
            })})})
    }

    async getPlayerInfo(){
        if(this.state.playerId == null){
            console.log("No player ID");
            return;
        }
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

    voteToStart(id){
        this.patchRequest("/voteToStart", id, JSON.stringify({playerId: this.state.playerId}))
        console.log("Voted to start")
    };
    
       
       
    render() {
        const joinGamePage = () => {
            if(this.state.lobbyId == null){
                return <JoinGamePage joinGame={(id) => this.loadLobby(id)} createGame={() => this.createGame()}/>
            }
        }

        const lobbyPage = () => {
            if(this.state.lobbyId != null && this.state.game.playState === "INITIALIZATION"){
                return <Lobby game={this.state.game} voteToStart={(id) => this.voteToStart(id)}/>
            }
        }
        
        
        const gamePage = () => {
            return [<button onClick={()=>this.createGame()}>Create Game</button>,
                    <form><input type="text" id="joinID" name="joinID"/></form>,
                    <button onClick={()=>this.getState(document.getElementById('joinID').value)}>Get State</button>,
                    <DropdownButton id="dropdown-item-button" title={this.state.dropdownText} className="format">
                        <Dropdown.Item as="button"><div onClick={(e) => this.changeColor(e.target.textContent)}>RED</div></Dropdown.Item>
                        <Dropdown.Item as="button"><div onClick={(e) => this.changeColor(e.target.textContent)}>ORANGE</div></Dropdown.Item>
                        <Dropdown.Item as="button"><div onClick={(e) => this.changeColor(e.target.textContent)}>YELLOW</div></Dropdown.Item>
                        <Dropdown.Item as="button"><div onClick={(e) => this.changeColor(e.target.textContent)}>BLUE</div></Dropdown.Item>
                        <Dropdown.Item as="button"><div onClick={(e) => this.changeColor(e.target.textContent)}>PURPLE</div></Dropdown.Item>
                    </DropdownButton>,
                    <button onClick={()=>this.joinGame(document.getElementById('joinID').value).title}>Join Game</button>,
                    <button onClick={()=>this.voteToStart(document.getElementById('joinID').value)}>Vote Start</button>,
                    <GameView state={this.state.game} player={this.state.player}/>]
        }
        
        return(
            <React.Fragment>{
                <div>
                    {/*gamePage()*/}
                    {joinGamePage()}
                    {lobbyPage()}
                </div>   
            }       
            </React.Fragment>
        )
    }
}

export default GameController;