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

        if (this.props.state.game.readyToProceed)
        {
            proceedButton.push(<div className="proceedButton" onClick={proceed}>PROCEED</div>)
        }
        else
        {
            proceedButton.push(<div className="disabledProceedButton">PROCEED</div>)
        }

        console.log(this.props.state.player)

        var board = "orangebacteria";
        if(this.props.state.player.plague.color && this.props.state.player.plague.diseaseType){
            board = this.props.state.player.plague.color.toLowerCase() + this.props.state.player.plague.diseaseType.toLowerCase();
        }

        let playerBoard = () => {
            if(this.props.state.playerId != null){
                return [<span className="plagueCard"><PlagueCard cardName={board}/></span>,
                <span className="hand"><TraitHand hand={this.props.state.player.hand}/></span>]
            }
        }
        return(
            <React.Fragment>{
                <div className="gameView" height={h} width={w}>
                    
                    <div>
                        <CountryDraftZone state={this.props.state}/>
                    </div>
            
                    <div>
                        <Board state={this.props.state}/>
                    </div>

                    <div>{proceedButton}</div>
        
                    <div className="bottomBar">
                        {playerBoard()}  
                    </div>
                </div> 
                }
            </React.Fragment>
        )
    }
}

export default GameView;