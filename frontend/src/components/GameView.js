import React, { Component } from "react";

import Board from './Board';
import TraitHand from './TraitHand';
import PlagueCard from './PlagueCard';
import CountryDraftZone from './CountryDraftZone';

var w = window.innerWidth;
var h = window.innerHeight;

class GameView extends Component{

    render() {
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