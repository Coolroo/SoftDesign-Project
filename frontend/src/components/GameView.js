import React, { Component } from "react";

import Board from './Board';
import TraitHand from './TraitHand';
import PlagueCard from './PlagueCard';
import CountryDraftZone from './CountryDraftZone';

var w = window.innerWidth;
var h = window.innerHeight;

class GameView extends Component{

    render() {
        return(
            <React.Fragment>{
                <div className="gameView" height={h} width={w}>
                    <div>
                        <CountryDraftZone/>
                    </div>
                    <div>
                        <Board/>
                    </div>
                    <div className="bottomBar">
                        <span className="plagueCard"><PlagueCard cardName="orangebacteria"/></span><span className="hand"><TraitHand/></span>    
                    </div>
                </div> 
                }
            </React.Fragment>
        )
    }
}

export default GameView;