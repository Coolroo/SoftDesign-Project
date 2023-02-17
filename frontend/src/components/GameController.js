import React, { Component } from "react";
import GameView from "./GameView";
import JoinGamePage from "./lobby/JoinGamePage";
import * as SockJS from 'sockjs-client';
import { Stomp } from "@stomp/stompjs";
import Lobby from "./lobby/Lobby";
import Cookies from 'universal-cookie';

const postRequestOptions = {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' }
}; 

const patchRequestOptions = {
    method: 'PATCH',
    headers: { 'Content-Type': 'application/json' }
};

const SOCKET_URL = SERVER_URL + '/plague-socket';

const SERVER_URL = "http://plagueinc.coolroo.ca/backend";

const cookies = new Cookies();

class GameController extends Component{
    socket = null;
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
       playerId: null,
       lobbyId: null
        
    };
    /**
     * This function is called when the component is first mounted.
     * It checks if the user has a cookie for a lobbyId and playerId.
     * If they do, it will load the lobby and player info.
     * If they don't, it will do nothing.
     */
    componentDidMount(){
        if(cookies.get("preserveState")  === "false"){
            console.log("Cookies disabled. Not loading cookies");
            return;
        }
        console.log("Loading cookies");
        if(cookies.get("lobbyId") !== null){
            console.log("Found lobbyId cookie: " + cookies.get("lobbyId"));
            var playerId = null;
            if(cookies.get("playerId") !== null){
                playerId = cookies.get("playerId");
                console.log("Found playerId cookie: " + cookies.get("playerId"));
            }
            var lobbyId = cookies.get("lobbyId");
            this.getGameState(lobbyId).then(resp => {
                return resp.json();
            }).then(resp => {
                if(resp !== null){
                    this.setState((prevState) => {
                        return {
                            ...prevState,
                            lobbyId: lobbyId,
                            playerId: playerId
                        }
                    }, () => {
                        this.loadLobby(cookies.get("lobbyId")).then(() => {
                            if(this.state.playerId != null){
                                this.getPlayerInfo()
                            }
                        })
                    })
                    
                }
                else{
                    cookies.remove("lobbyId");
                    cookies.remove("playerId");
                }
            })

        }
        
    }
    
    async createGame(){
        this.gameID = await fetch(SERVER_URL + `/createGame`, postRequestOptions)
            .then(function(response) {
                return response.text();
            }).then((data) => {
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

    async getGameState(id){
        return fetch(SERVER_URL + '/gameState?gameStateId=' + id);
    }

    async loadLobby(id){
        if(this.socket != null){
            console.log("Cannot join another lobby if you are already in one");
        }
        return this.getGameState(id)
        .then(resp => {
            if(resp.ok){
                resp.text().then(gameState => {
                    this.setState((prevState) => {
                        return {
                            ...prevState,
                            lobbyId: id
                        }
                    }, 
                    () => {
                        cookies.set("lobbyId", id, { path: '/' });
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
        var sock = new SockJS(SOCKET_URL);
        var stompClient = Stomp.over(sock);
        this.socket = stompClient;
        stompClient.connect({}, frame => {
                
                    stompClient.subscribe("/games/gameState/"+this.state.lobbyId, (body) => {
                    console.log("Websocket updated state: " + body.body);
                    const jsonBody = JSON.parse(body.body);
                    this.updateGameState(jsonBody).then(() => {
                        this.getPlayerInfo();
                    })
                    
            })
        })
    }

    async getPlayerInfo(){
        if(this.state.playerId == null){
            console.log("No player ID");
            return;
        }
        return fetch(SERVER_URL + "/getPlayerInfo?gameStateId=" + this.state.lobbyId + "&playerId=" + this.state.playerId)
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
                                
                            });
                            
                        });
                    }
                });
    }

    voteToStart(){
        this.patchRequest("/voteToStart", this.state.lobbyId, JSON.stringify({playerId: this.state.playerId}))
        console.log("Voted to start")
    };

    discard(countryName){
        this.patchRequest("/countryChoice", this.state.lobbyId, JSON.stringify({playerId: this.state.playerId, countryName: countryName, choice:"DISCARD"}))
        console.log("Discarded")
    };

    proceed(){
        if(this.state.lobbyId == null){
            console.log("No lobby ID");
            return;
        }
        this.patchRequest("/proceedState", this.state.lobbyId, JSON.stringify({playerId: this.state.playerId}))
    }

    changePlagueType(){
        const cycle = {
            BACTERIA: "VIRUS",
            VIRUS: "BACTERIA"
        }
        console.log("Changing plague type");
        this.patchRequest("/changePlagueType", this.state.lobbyId, JSON.stringify({playerId: this.state.playerId, diseaseType: cycle[this.state.player.plague.diseaseType]}))
    };

    placeCountry(countryName){
        if(this.state.lobbyId === null){
            console.log("No lobby ID");
            return;
        }
        console.log("Placing country: " + JSON.stringify(countryName));
        this.patchRequest("/countryChoice", this.state.lobbyId, JSON.stringify({playerId: this.state.playerId, countryName: countryName, choice: "PLAY"}));
    }

    skipEvolve(){
        if(this.state.lobbyId === null){
            console.log("No lobby ID");
            return;
        }
        console.log("Skipping evolve");
        this.patchRequest("/skipEvolution", this.state.lobbyId, JSON.stringify({playerId: this.state.playerId}));
    }

    evolve(traitCard, traitSlot){
        if(this.state.lobbyId === null){
            console.log("No lobby ID");
            return;
        }
        console.log("Evolving");
        this.patchRequest("/evolve", this.state.lobbyId, JSON.stringify({playerId: this.state.playerId, traitIndex: traitCard, traitSlot: traitSlot}));
    }

    infect(countryName){
        if(this.state.lobbyId === null){
            console.log("No lobby ID");
            return;
        }
        console.log("Infecting " + countryName);
        this.patchRequest("/infect", this.state.lobbyId, JSON.stringify({playerId: this.state.playerId, countryName: countryName}));
    }

    kill(countryName){
        if(this.state.lobbyId === null){
            console.log("No lobby ID");
            return;
        }
        console.log("Killing " + countryName);
        return this.patchRequest("/rollDeathDice", this.state.lobbyId, JSON.stringify({playerId: this.state.playerId, countryName: countryName}));
    }
       
    render() {
        const joinGamePage = () => {
            if(this.state.lobbyId == null){
                return <JoinGamePage joinGame={(id) => this.loadLobby(id)} createGame={() => this.createGame()}/>
            }
        }

        const lobbyPage = () => {
            if(this.state.lobbyId !== null && this.state.game.playState === "INITIALIZATION" && this.state.game.playState !== undefined){
                return <Lobby joinGame={(color) => this.joinGame(color)} voteToStart={() => this.voteToStart()} changeType={() => this.changePlagueType()}  state={this.state}  />
            }
        }
        
        // eslint-disable-next-line
        const gamePage = () => {
            if(this.state.lobbyId != null && this.state.game.playState !== "INITIALIZATION" && this.state.game.playState !== undefined){
                return <GameView kill={(countryName) => this.kill(countryName)} infect={(countryName) => this.infect(countryName)} evolve={(traitCard, traitSlot) => this.evolve(traitCard, traitSlot)} skipEvolve={() => this.skipEvolve()} proceed={() => this.proceed()} state={this.state} discard={(countryName) => this.discard(countryName)} placeCountry={(countryName) => this.placeCountry(countryName)}/>
            }
            
        }
        
        return(
            <React.Fragment>{
                <div>
                    {gamePage()}
                    {joinGamePage()}
                    {lobbyPage()}
                </div>   
            }       
            </React.Fragment>
        )
    }
}

export default GameController;