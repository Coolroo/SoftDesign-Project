import React, { Component } from "react";

import Board from './Board';
import TraitHand from './TraitHand';

var w = window.innerWidth;
var h = window.innerHeight;

class GameView extends Component{

    render() {
        return(
            <React.Fragment>{
                <div className="gameView" height={h} width={w}>
                    <Board/>
                    <TraitHand/>
                </div> 
                }
            </React.Fragment>
        )
    }
}

export default GameView;