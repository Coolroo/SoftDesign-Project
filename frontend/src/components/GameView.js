import React, { Component } from "react";

import Board from './Board';
import TraitHand from './TraitHand';

class GameView extends Component{

    render() {
        return(
            <React.Fragment>{
                <div>
                    <Board/>
                    <TraitHand/>
                </div> 
                }
            </React.Fragment>
        )
    }
}

export default GameView;