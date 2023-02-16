import React, { Component } from "react";

import Board from './board/Board';
import TraitHand from './TraitHand';
import PlagueCard from './PlagueCard';
import CountryDraftZone from './countries/CountryDraftZone';

class GameView extends Component{

    render() {
        console.log(this.props.state)
        
        let proceed = () => {
            this.props.proceed();
        }    

        let skipEvolve = () => {
            this.props.skipEvolve();
        }

        console.log(this.props.state.player)

        var board = "";
        if(this.props.state.player.plague.color && this.props.state.player.plague.diseaseType){
            board = this.props.state.player.plague.color.toLowerCase() + this.props.state.player.plague.diseaseType.toLowerCase();
        }

        let skipEvolveButton = () => {
            if(this.props.state.game.playState === "EVOLVE" && this.props.state.game.currTurn === this.props.state.player.plague.color && !this.props.state.game.readyToProceed){
                return(
                    <div className="joinGameButton" style={{position:"relative", marginLeft: "2%", "--color": "red"}} onClick={skipEvolve}>Skip Evolve</div>
                )
            }
        }

        return(
            <React.Fragment>{
                <div style={{verticalAlign:"middle", display:"flex", flexDirection:"column", justifyContent:"center", alignItems:"center", marginTop: "0.5%"}}>
                    <div className="joinGameButton" style={{marginBottom: "0.5%", position:"relative", width:"15%", "--color": this.props.state.game.currTurn.toLowerCase()}}>{this.props.state.game.playState}</div>
                    <div className="gameView"> 
                        <div style={{width: "40%", height: "40%", marginLeft:"10%"}}>
                            <Board infect={this.props.infect} state={this.props.state} placeCountry={this.props.placeCountry}/>
                        </div>
                        <div style={{verticalAlign:"middle", marginRight: "2%", marginLeft:"2%", position:"relative"}}>
                            <CountryDraftZone state={this.props.state} discard={(countryName) => this.props.discard(countryName)}/>
                            <PlagueCard state={this.props.state} evolve={this.props.evolve} cardName={board}/>
                        </div>
                    </div> 
                    <div style={{verticalAlign:"middle", display:"flex", justifyContent:"center", alignItems:"center", marginTop: "1%"}}>
                        <div style={{width:"70%", height:"10%"}}>
                            <TraitHand hand={this.props.state.player.hand}/>
                        </div>
                        {skipEvolveButton()}
                        <div className={"joinGameButton " + (this.props.state.game.readyToProceed ? "" : "disabled")} style={{position:"relative", marginLeft: "2%"}} onClick={proceed}>Proceed State</div>
                    </div>
                </div>
                }
            </React.Fragment>
        )
    }
}

export default GameView;