import React, { Component } from "react";

import Board from './board/Board';
import TraitHand from './TraitHand';
import PlagueCard from './PlagueCard';
import CountryDraftZone from './countries/CountryDraftZone';

var w = window.innerWidth;
var h = window.innerHeight;

class GameView extends Component{

    render() {
        console.log(this.props.state)
        let proceedButton = [];
        
        let proceed = () => {
            this.props.proceed();
        }    

        console.log(this.props.state.player)

        var board = "";
        if(this.props.state.player.plague.color && this.props.state.player.plague.diseaseType){
            board = this.props.state.player.plague.color.toLowerCase() + this.props.state.player.plague.diseaseType.toLowerCase();
        }

        return(
            <React.Fragment>{
                <div style={{verticalAlign:"middle", display:"flex", flexDirection:"column", justifyContent:"center", alignItems:"center", marginTop: "2vh"}}>
                    <div className="joinGameButton" style={{marginBottom: "2vh", position:"relative", width:"15vw"}}>{this.props.state.game.playState}</div>
                    <div className="gameView"> 
                        <Board state={this.props.state}/>
                        <div style={{verticalAlign:"middle", marginRight: "5vw", marginLeft:"1vw", position:"relative"}}>
                            <CountryDraftZone state={this.props.state}/>
                            <PlagueCard cardName={board}/>
                        </div>
                    </div> 
                    <div style={{verticalAlign:"middle", display:"flex", justifyContent:"center", alignItems:"center", marginTop: "2vh"}}>
                        <div style={{width:"50vw"}}>
                            <TraitHand hand={this.props.state.player.hand}/>
                        </div>
                        <div className="joinGameButton" style={{position:"relative", marginLeft: "5vw"}} onClick={proceed}>Proceed State</div>
                    </div>
                </div>
                }
            </React.Fragment>
        )
    }
}

export default GameView;