import React, { Component } from "react";

class Board extends Component{

    render() {
        return(
            <React.Fragment>{
                    <div className="card">
                        <img src={`/Board.jpg`}  alt="img" height="603" width="825"/>
                    </div>   
                }
            </React.Fragment>
        )
    }
}

export default Board;