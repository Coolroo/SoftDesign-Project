import React, { Component } from "react";
import BoardCountrySlot from "./BoardCountrySlot";

class Board extends Component{

    render() {
        return(
            <React.Fragment>{
                    <div className="board">
                        <img src={`/Board.jpg`} alt="img"/>
                        <div style={{top:'11.3%', left:'18.2%'}} className="boardCountrySlot"><BoardCountrySlot/></div>
                    </div>
                }
            </React.Fragment>
        )
    }
}

export default Board;