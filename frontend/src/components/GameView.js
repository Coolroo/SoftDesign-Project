import React, { Component } from "react";

import Board from './board/Board';
import TraitHand from './TraitHand';
import PlagueCard from './PlagueCard';
import CountryDraftZone from './countries/CountryDraftZone';

var w = window.innerWidth;
var h = window.innerHeight;

class GameView extends Component{

    render() {
        console.log(this.props.state.player)

        var board = "orangebacteria";
        if(this.props.state.player.plague.color && this.props.state.player.plague.diseaseType){
            board = this.props.state.player.plague.color.toLowerCase() + this.props.state.player.plague.diseaseType.toLowerCase();
        }

        let playerBoard = () => {
            if(this.props.state.playerId != null){
                return [<PlagueCard cardName={board}/>,
                    <span className="hand"><TraitHand hand={this.props.state.player.hand}/></span>]
            }
        }
        return(
            <React.Fragment>{
                <div className="gameView" >
                    <div style={{verticalAlign:"middle", position:"absolute", bottom:"0"}}>
                            {playerBoard()}  
                    </div>
                    <div style={{verticalAlign:"middle", position:"absolute", right:"0"}}>
                        <CountryDraftZone state={this.props.state}/>
                        <Board state={this.props.state}/>
                    </div>
                    
                </div> 
                }
            </React.Fragment>
        )
    }
}

export default GameView;