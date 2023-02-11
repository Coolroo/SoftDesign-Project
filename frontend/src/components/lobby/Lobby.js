import React, { Component } from "react";

class Lobby extends Component{
    
    render(){
        const backgrounds = {"RED": "radial-gradient(50% 50% at 50% 50%, rgba(0, 0, 0, 0) 0%, rgba(255, 0, 0, 0.4) 100%) ",
                        "BLUE": "radial-gradient(50% 50% at 50% 50%, rgba(0, 0, 0, 0) 0%, rgba(0, 0, 255, 0.36) 100%) ",
                        "YELLOW": "radial-gradient(50% 50% at 50% 50%, rgba(0, 0, 0, 0) 0%, rgba(255, 255, 0, 0.36) 100%) ",
                        "PURPLE": "radial-gradient(50% 50% at 50% 50%, rgba(0, 0, 0, 0) 0%, rgba(255, 0, 199, 0.4) 100%) "};
        return(<React.Fragment>
            <div class="playerBox" style={{top: "20%", 
                                           left: "15%", 
                                           border: "0.2vw solid #FF0000", 
                                           background: backgrounds["RED"]}}/>
            <div class="playerBox" style={{top: "20%", 
                                           left: "35%", 
                                           border: "0.2vw solid #FF0000", 
                                           background: backgrounds["BLUE"]}}/>
            <div class="playerBox" style={{top: "20%", 
                                           left: "55%", 
                                           border: "0.2vw solid #FF0000", 
                                           background: backgrounds["YELLOW"]
                                           }}/>
            <div class="playerBox" style={{top: "20%", 
                                           left: "75%", 
                                           border: "0.2vw solid #FF0000", 
                                           background: backgrounds["PURPLE"]}}/>
        </React.Fragment>);
    }
}

export default Lobby;