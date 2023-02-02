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
                        <div style={{top:'94.8%', left:'27.1%'}} className="boardDNASlot"><BoardDNASlot score={28}/></div>
                        <div style={{top:'95.0%', left:'23.4%'}} className="boardDNASlot"><BoardDNASlot score={29}/></div>
                        <div style={{top:'93.6%', left:'20.0%'}} className="boardDNASlot"><BoardDNASlot score={30}/></div>

                        <div style={{top:'92.0%', left:'16.7%'}} className="boardDNASlot"><BoardDNASlot score={31}/></div>
                        <div style={{top:'92.3%', left:'13.2%'}} className="boardDNASlot"><BoardDNASlot score={32}/></div>
                        <div style={{top:'94.1%', left:'10.0%'}} className="boardDNASlot"><BoardDNASlot score={33}/></div>
                        <div style={{top:'95.3%', left:'7.0%'}} className="boardDNASlot"><BoardDNASlot score={34}/></div>
                        <div style={{top:'95.9%', left:'3.8%'}} className="boardDNASlot"><BoardDNASlot score={35}/></div>
                        <div style={{top:'92.2%', left:'2.1%'}} className="boardDNASlot"><BoardDNASlot score={36}/></div>
                        <div style={{top:'88.3%', left:'1.8%'}} className="boardDNASlot"><BoardDNASlot score={37}/></div>
                        <div style={{top:'85.0%', left:'3.2%'}} className="boardDNASlot"><BoardDNASlot score={38}/></div>
                        <div style={{top:'82.4%', left:'5.7%'}} className="boardDNASlot"><BoardDNASlot score={39}/></div>
                        <div style={{top:'80.2%', left:'8.2%'}} className="boardDNASlot"><BoardDNASlot score={40}/></div>

                        <div style={{top:'76.7%', left:'9.7%'}} className="boardDNASlot"><BoardDNASlot score={41}/></div>
                        <div style={{top:'74.4%', left:'7.1%'}} className="boardDNASlot"><BoardDNASlot score={42}/></div>
                        <div style={{top:'72.7%', left:'4.4%'}} className="boardDNASlot"><BoardDNASlot score={43}/></div>
                        <div style={{top:'70.2%', left:'1.8%'}} className="boardDNASlot"><BoardDNASlot score={44}/></div>
                        <div style={{top:'66.4%', left:'1.3%'}} className="boardDNASlot"><BoardDNASlot score={45}/></div>
                        <div style={{top:'62.2%', left:'2.3%'}} className="boardDNASlot"><BoardDNASlot score={46}/></div>
                        <div style={{top:'59.2%', left:'4.3%'}} className="boardDNASlot"><BoardDNASlot score={47}/></div>
                        <div style={{top:'56.8%', left:'6.8%'}} className="boardDNASlot"><BoardDNASlot score={48}/></div>
                        <div style={{top:'53.8%', left:'9.0%'}} className="boardDNASlot"><BoardDNASlot score={49}/></div>
                        <div style={{top:'50.8%', left:'6.7%'}} className="boardDNASlot"><BoardDNASlot score={50}/></div>

                        <div style={{top:'49.1%', left:'3.9%'}} className="boardDNASlot"><BoardDNASlot score={51}/></div>
                        <div style={{top:'46.2%', left:'1.8%'}} className="boardDNASlot"><BoardDNASlot score={52}/></div>
                        <div style={{top:'42.2%', left:'1.6%'}} className="boardDNASlot"><BoardDNASlot score={53}/></div>
                        <div style={{top:'38.1%', left:'2.9%'}} className="boardDNASlot"><BoardDNASlot score={54}/></div>
                        <div style={{top:'34.2%', left:'4.3%'}} className="boardDNASlot"><BoardDNASlot score={55}/></div>
                        <div style={{top:'30.3%', left:'4.3%'}} className="boardDNASlot"><BoardDNASlot score={56}/></div>
                        <div style={{top:'26.5%', left:'3.2%'}} className="boardDNASlot"><BoardDNASlot score={57}/></div>
                        <div style={{top:'22.5%', left:'2.6%'}} className="boardDNASlot"><BoardDNASlot score={58}/></div>
                        <div style={{top:'21.1%', left:'5.9%'}} className="boardDNASlot"><BoardDNASlot score={59}/></div>
                        <div style={{top:'17.2%', left:'6.8%'}} className="boardDNASlot"><BoardDNASlot score={60}/></div>

                        <div style={{top:'13.7%', left:'6.2%'}} className="boardDNASlot"><BoardDNASlot score={61}/></div>
                        <div style={{top:'11.1%', left:'3.7%'}} className="boardDNASlot"><BoardDNASlot score={62}/></div>
                        <div style={{top:'7.7%', left:'1.2%'}} className="boardDNASlot"><BoardDNASlot score={63}/></div>
                        <div style={{top:'4.1%', left:'2.8%'}} className="boardDNASlot"><BoardDNASlot score={64}/></div>
                        <div style={{top:'1.7%', left:'5.3%'}} className="boardDNASlot"><BoardDNASlot score={65}/></div>
                        <div style={{top:'1.8%', left:'8.1%'}} className="boardDNASlot"><BoardDNASlot score={66}/></div>
                        <div style={{top:'3.5%', left:'11.0%'}} className="boardDNASlot"><BoardDNASlot score={67}/></div>
                        <div style={{top:'4.5%', left:'13.8%'}} className="boardDNASlot"><BoardDNASlot score={68}/></div>
                        <div style={{top:'3.9%', left:'16.7%'}} className="boardDNASlot"><BoardDNASlot score={69}/></div>
                        <div style={{top:'3.0%', left:'19.8%'}} className="boardDNASlot"><BoardDNASlot score={70}/></div>

                        <div style={{top:'3.5%', left:'22.7%'}} className="boardDNASlot"><BoardDNASlot score={71}/></div>
                        <div style={{top:'5.2%', left:'25.4%'}} className="boardDNASlot"><BoardDNASlot score={72}/></div>
                        <div style={{top:'5.2%', left:'28.2%'}} className="boardDNASlot"><BoardDNASlot score={73}/></div>
                        <div style={{top:'3.4%', left:'31.0%'}} className="boardDNASlot"><BoardDNASlot score={74}/></div>
                        <div style={{top:'1.8%', left:'34.2%'}} className="boardDNASlot"><BoardDNASlot score={75}/></div>
                        <div style={{top:'1.9%', left:'37.0%'}} className="boardDNASlot"><BoardDNASlot score={76}/></div>
                        <div style={{top:'3.5%', left:'39.8%'}} className="boardDNASlot"><BoardDNASlot score={77}/></div>
                        <div style={{top:'5.4%', left:'42.4%'}} className="boardDNASlot"><BoardDNASlot score={78}/></div>
                        <div style={{top:'5.3%', left:'45.2%'}} className="boardDNASlot"><BoardDNASlot score={79}/></div>
                        <div style={{top:'3.6%', left:'48.0%'}} className="boardDNASlot"><BoardDNASlot score={80}/></div>

                        <div style={{top:'1.8%', left:'51.0%'}} className="boardDNASlot"><BoardDNASlot score={81}/></div>
                        <div style={{top:'1.9%', left:'54.0%'}} className="boardDNASlot"><BoardDNASlot score={82}/></div>
                        <div style={{top:'3.4%', left:'56.7%'}} className="boardDNASlot"><BoardDNASlot score={83}/></div>
                        <div style={{top:'5.3%', left:'59.3%'}} className="boardDNASlot"><BoardDNASlot score={84}/></div>
                        <div style={{top:'5.3%', left:'62.3%'}} className="boardDNASlot"><BoardDNASlot score={85}/></div>
                        <div style={{top:'3.5%', left:'65.1%'}} className="boardDNASlot"><BoardDNASlot score={86}/></div>
                        <div style={{top:'1.8%', left:'68.2%'}} className="boardDNASlot"><BoardDNASlot score={87}/></div>
                        <div style={{top:'1.8%', left:'71.2%'}} className="boardDNASlot"><BoardDNASlot score={88}/></div>
                        <div style={{top:'3.6%', left:'73.9%'}} className="boardDNASlot"><BoardDNASlot score={89}/></div>
                        <div style={{top:'5.0%', left:'76.8%'}} className="boardDNASlot"><BoardDNASlot score={90}/></div>

                        <div style={{top:'4.9%', left:'79.9%'}} className="boardDNASlot"><BoardDNASlot score={91}/></div>
                        <div style={{top:'3.1%', left:'82.6%'}} className="boardDNASlot"><BoardDNASlot score={92}/></div>
                        <div style={{top:'1.4%', left:'85.4%'}} className="boardDNASlot"><BoardDNASlot score={93}/></div>
                        <div style={{top:'1.6%', left:'88.1%'}} className="boardDNASlot"><BoardDNASlot score={94}/></div>
                        <div style={{top:'4.2%', left:'90.7%'}} className="boardDNASlot"><BoardDNASlot score={95}/></div>
                        <div style={{top:'8.2%', left:'87.5%'}} className="boardDNASlot"><BoardDNASlot score={96}/></div>
                        <div style={{top:'11.8%', left:'85.7%'}} className="boardDNASlot"><BoardDNASlot score={97}/></div>
                        <div style={{top:'15.9%', left:'84.6%'}} className="boardDNASlot"><BoardDNASlot score={98}/></div>
                        <div style={{top:'20.5%', left:'85.3%'}} className="boardDNASlot"><BoardDNASlot score={99}/></div>
                        <div style={{top:'22.5%', left:'88.6%'}} className="boardDNASlot"><BoardDNASlot score={100}/></div>
                    </div>
                }
            </React.Fragment>
        )
    }
}

export default Board;