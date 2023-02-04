import React, { Component } from "react";
import GameView from "./GameView";

class GameController extends Component{

    render() {
        return(
            <React.Fragment>{
                <div>
                    <GameView />
                </div>   
    }       </React.Fragment>
        )
    }
}

export default GameController;