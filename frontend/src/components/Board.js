import React, { Component } from "react";
import BoardCountrySlot from "./BoardCountrySlot";
import BoardDNASlot from "./BoardDNASlot";

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
                   
                        {/*DNA Score slots*/}
                        <div style={{top:'10.1%', left:'94.3%'}} className="boardDNASlot"><BoardDNASlot score={0}/></div>
                        <div style={{top:'20.7%', left:'94.6%'}} className="boardDNASlot"><BoardDNASlot score={1}/></div>
                        <div style={{top:'28.1%', left:'95.5%'}} className="boardDNASlot"><BoardDNASlot score={2}/></div>
                        <div style={{top:'36.3%', left:'95.8%'}} className="boardDNASlot"><BoardDNASlot score={3}/></div>
                        <div style={{top:'44.3%', left:'96.6%'}} className="boardDNASlot"><BoardDNASlot score={4}/></div>
                        <div style={{top:'51.1%', left:'94.6%'}} className="boardDNASlot"><BoardDNASlot score={5}/></div>
                        <div style={{top:'58.1%', left:'92.0%'}} className="boardDNASlot"><BoardDNASlot score={6}/></div>
                        <div style={{top:'65.9%', left:'91.4%'}} className="boardDNASlot"><BoardDNASlot score={7}/></div>
                        <div style={{top:'73.8%', left:'93.1%'}} className="boardDNASlot"><BoardDNASlot score={8}/></div>
                        <div style={{top:'80.8%', left:'95.9%'}} className="boardDNASlot"><BoardDNASlot score={9}/></div>
                        <div style={{top:'88.0%', left:'96.4%'}} className="boardDNASlot"><BoardDNASlot score={10}/></div>
                        <div style={{top:'95.0%', left:'94.4%'}} className="boardDNASlot"><BoardDNASlot score={11}/></div>
                        <div style={{top:'93.5%', left:'88.1%'}} className="boardDNASlot"><BoardDNASlot score={12}/></div>
                        <div style={{top:'92.3%', left:'83.5%'}} className="boardDNASlot"><BoardDNASlot score={13}/></div>
                        <div style={{top:'92.3%', left:'79.2%'}} className="boardDNASlot"><BoardDNASlot score={14}/></div>
                        <div style={{top:'93.8%', left:'75.1%'}} className="boardDNASlot"><BoardDNASlot score={15}/></div>
                        <div style={{top:'94.8%', left:'71.1%'}} className="boardDNASlot"><BoardDNASlot score={16}/></div>
                        <div style={{top:'95.0%', left:'66.9%'}} className="boardDNASlot"><BoardDNASlot score={17}/></div>
                        <div style={{top:'93.5%', left:'62.9%'}} className="boardDNASlot"><BoardDNASlot score={18}/></div>
                        <div style={{top:'91.8%', left:'59.2%'}} className="boardDNASlot"><BoardDNASlot score={19}/></div>
                        <div style={{top:'92.1%', left:'55.3%'}} className="boardDNASlot"><BoardDNASlot score={20}/></div>
                        <div style={{top:'93.5%', left:'51.6%'}} className="boardDNASlot"><BoardDNASlot score={21}/></div>
                        <div style={{top:'94.7%', left:'48.1%'}} className="boardDNASlot"><BoardDNASlot score={22}/></div>
                        <div style={{top:'94.9%', left:'44.3%'}} className="boardDNASlot"><BoardDNASlot score={23}/></div>
                        <div style={{top:'93.4%', left:'40.9%'}} className="boardDNASlot"><BoardDNASlot score={24}/></div>
                        <div style={{top:'91.7%', left:'37.5%'}} className="boardDNASlot"><BoardDNASlot score={25}/></div>
                        <div style={{top:'92.2%', left:'34.1%'}} className="boardDNASlot"><BoardDNASlot score={26}/></div>
                        <div style={{top:'93.7%', left:'30.6%'}} className="boardDNASlot"><BoardDNASlot score={27}/></div>
                    </div>
                }
            </React.Fragment>
        )
    }
}

export default Board;