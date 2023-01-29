import React, { Component } from "react";

class Board extends Component{

    render() {
        return(
            <React.Fragment>{
                    <div>
                        <img src={`/Board.jpg`}  className="board" alt="img"/>
                    </div>
                }
            </React.Fragment>
        )
    }
}

export default Board;