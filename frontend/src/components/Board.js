import React, { Component } from "react";
import BoardCountrySlot from "./BoardCountrySlot";

class Board extends Component{

    render() {
        return(
            <React.Fragment>{
                    <div>
                        <img src={`/Board.jpg`} className="board" alt="img"/>
                        <div style={{top:'60px', left:'131px'}} className="boardCountrySlot"><BoardCountrySlot/></div>
                    </div>
                }
            </React.Fragment>
        )
    }
}

export default Board;