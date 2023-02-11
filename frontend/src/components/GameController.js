import React, { Component } from "react";
import GameView from "./GameView";
import JoinGamePage from "./lobby/JoinGamePage";
import Dropdown from 'react-bootstrap/Dropdown';
import { DropdownButton } from "react-bootstrap";
import * as SockJS from 'sockjs-client';
import { Stomp } from "@stomp/stompjs";
import Lobby from "./lobby/Lobby";
import Cookies from 'universal-cookie';

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

const cookies = new Cookies();
class GameController extends Component{

    state = {
        game: {
            board: {
                OCEANIA: [],
                EUROPE: [],
                ASIA: [],
                NORTH_AMERICA: [],
                SOUTH_AMERICA: [],
                AFRICA: []
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
            plague: {
                color: null,
                diseaseType: null,
                dnaPoints: null,
                plagueTokens: null,
                traitSlots: null,
                killedCountries: null
            }
        },
       dropdownText: "Please select color",
       playerId: null,
       socket: null,
       lobbyId: null
        
    };

    componentDidMount(){
        console.log("Loading cookies");
        if(cookies.get("lobbyId") != null){
            console.log("Found lobbyId cookie: " + cookies.get("lobbyId"));
            var playerId = null;
            if(cookies.get("playerId") != null){
                playerId = cookies.get("playerId");
                console.log("Found playerId cookie: " + cookies.get("playerId"));
            }
            this.setState((prevState) => {
                return {
                    ...prevState,
                    lobbyId: cookies.get("lobbyId"),
                    playerId: playerId
                }
            }, () => {
                if(this.state.playerId != null){
                    this.getPlayerInfo();
                }
                this.loadLobby(this.state.lobbyId)
            });
        }
        
    }
    

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

    async updateGameState(data){
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
                    playState: data.playState,
                    readyToProceed: data.readyToProceed,
                    revealedCountries: data.revealedCountries,
                    suddenDeath: data.suddenDeath,
                    traitDiscard: data.traitDiscard,
                    turnOrder: data.turnOrder,
                    votesToStart: data.votesToStart
                }
            };
        });
    }

    async joinGame(color){
        if(this.state.lobbyId == null){
            console.log("No lobby ID");
            return;
        }
        this.patchRequest("/joinGame", this.state.lobbyId, JSON.stringify({plagueColor: color}))
        .then(response => {
            if(response.ok) {
                response.text().then(text => {
                    this.setState((prevState) => {
                        return {...prevState,
                        playerId: text.replace(/['"]+/g, ''),
                }}, () => {
                        cookies.set("playerId", this.state.playerId, { path: '/' });
                        console.log("Player ID = " + text);
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
            console.log("new PATCH request to endpoint" + endpoint + " with body " + JSON.stringify(patchBody));
        return fetch(SERVER_URL + endpoint + "?gameStateId=" + lobbyId, patchBody);
    }

    async loadLobby(id){
        if(this.state.socket != null){
            console.log("Cannot join another lobby if you are already in one");
        }
        fetch(SERVER_URL + "/gameState?gameStateId=" + id)
        .then(resp => {
            if(resp.ok){
                resp.text().then(gameState => {
                    this.setState((prevState) => {
                        return {
                            ...prevState,
                            lobbyId: id
                        }
                    }, () => {
                        cookies.set("lobbyId", id, { path: '/' });
                        console.log(gameState);
                        this.updateGameState(JSON.parse(gameState)).then(() => {
                            console.log("Game loaded: " + JSON.stringify(gameState));
                            this.initWebSocket();
                        });
                        
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
                    this.updateGameState(jsonBody).then(() => {
                        this.getPlayerInfo();
                    })
                    
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
                            const playerJson = JSON.parse(playerInfo);
                            this.setState((prevState) => {
                                return {
                                    ...prevState,
                                    player: {
                                        hand: playerJson.hand,
                                        eventCards: playerJson.eventCards,
                                        plague: playerJson.plague
                                    }
                                }
                            }, () => {
                                console.log("Player loaded: " + playerInfo);
                                this.forceUpdate();
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

    voteToStart(){
        this.patchRequest("/voteToStart", this.state.lobbyId, JSON.stringify({playerId: this.state.playerId}))
        console.log("Voted to start")
    };
    
       
       
    render() {
        const joinGamePage = () => {
            if(this.state.lobbyId == null){
                return <JoinGamePage joinGame={(id) => this.loadLobby(id)} createGame={() => this.createGame()}/>
            }
        }

        const lobbyPage = () => {
            console.log("lobbyId = " + this.state.lobbyId + " playState = " + JSON.stringify(this.state.game.playState));
            if(this.state.lobbyId != null && this.state.game.playState === "INITIALIZATION"){
                console.log("Lobby Page")
                return <Lobby joinGame={(color) => this.joinGame(color)}  state={this.state} voteToStart={() => this.voteToStart()} />
            }
        }
        
        // eslint-disable-next-line
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