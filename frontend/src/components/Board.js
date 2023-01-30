import React, { Component } from "react";
import BoardCountrySlot from "./BoardCountrySlot";

class Board extends Component{

    render() {
        return(
            <React.Fragment>{
                    <div className="board">
                        <img src={`/Board.jpg`} alt="img"/>
                        
                        {/* North America slots*/}
                        <div style={{top:'11.3%', left:'18.2%'}} className="boardCountrySlot"><BoardCountrySlot/></div>
                        <div style={{top:'29.7%', left:'8.5%'}} className="boardCountrySlot"><BoardCountrySlot/></div>
                        <div style={{top:'29.6%', left:'18.2%'}} className="boardCountrySlot"><BoardCountrySlot/></div>

                        {/* Europe slots*/}
                        <div style={{top:'11.3%', left:'40.9%'}} className="boardCountrySlot"><BoardCountrySlot/></div>
                        <div style={{top:'11.2%', left:'50.4%'}} className="boardCountrySlot"><BoardCountrySlot/></div>
                        <div style={{top:'29.6%', left:'31.2%'}} className="boardCountrySlot"><BoardCountrySlot/></div>
                        <div style={{top:'29.5%', left:'40.75%'}} className="boardCountrySlot"><BoardCountrySlot/></div>
                        <div style={{top:'29.5%', left:'50.3%'}} className="boardCountrySlot"><BoardCountrySlot/></div>


                        {/* Asia slots*/}
                        <div style={{top:'11.2%', left:'63.5%'}} className="boardCountrySlot"><BoardCountrySlot/></div>
                        <div style={{top:'11.1%', left:'73.3%'}} className="boardCountrySlot"><BoardCountrySlot/></div>
                        <div style={{top:'29.4%', left:'63.4%'}} className="boardCountrySlot"><BoardCountrySlot/></div>
                        <div style={{top:'29.3%', left:'73.1%'}} className="boardCountrySlot"><BoardCountrySlot/></div>
                        <div style={{top:'29.2%', left:'82.9%'}} className="boardCountrySlot"><BoardCountrySlot/></div>

                        {/* South America slots*/}
                        <div style={{top:'52.6%', left:'14.5%'}} className="boardCountrySlot"><BoardCountrySlot/></div>
                        <div style={{top:'52.55%', left:'24.05%'}} className="boardCountrySlot"><BoardCountrySlot/></div>
                        <div style={{top:'70.9%', left:'14.3%'}} className="boardCountrySlot"><BoardCountrySlot/></div>
                        <div style={{top:'70.8%', left:'23.9%'}} className="boardCountrySlot"><BoardCountrySlot/></div>

                        {/* Africa slots*/}
                        <div style={{top:'52.4%', left:'37.1%'}} className="boardCountrySlot"><BoardCountrySlot/></div>
                        <div style={{top:'52.2%', left:'46.6%'}} className="boardCountrySlot"><BoardCountrySlot/></div>
                        <div style={{top:'70.5%', left:'37.0%'}} className="boardCountrySlot"><BoardCountrySlot/></div>
                        <div style={{top:'70.5%', left:'46.5%'}} className="boardCountrySlot"><BoardCountrySlot/></div>
                        <div style={{top:'70.5%', left:'56.1%'}} className="boardCountrySlot"><BoardCountrySlot/></div>

                        {/* Oceania slots*/}
                        <div style={{top:'52.4%', left:'69.45%'}} className="boardCountrySlot"><BoardCountrySlot/></div>
                        <div style={{top:'52.35%', left:'79.2%'}} className="boardCountrySlot"><BoardCountrySlot/></div>
                        <div style={{top:'70.7%', left:'79.1%'}} className="boardCountrySlot"><BoardCountrySlot/></div>
                    </div>
                }
            </React.Fragment>
        )
    }
}

export default Board;