import React, { Component } from "react";
import TokenSlot from './TokenSlot';

class CountryCard extends Component{
    render() {
        // 2 slot countries:
        // greenland, iceland, libya, panama
        if  (this.props.cardName === 'greenland' ||
             this.props.cardName === 'iceland' ||
             this.props.cardName === 'libya' ||
             this.props.cardName === 'panama'){
            return(
                <React.Fragment>{
                        <div>
                            <img src={`/countries/${this.props.cardName}.png`} className="card"alt="img"/>
    
                            <div style={{top:'22%', left:'16.5%'}} className="hexagon"><TokenSlot/></div>
                            <div style={{top:'61.5%', left:'66.0%'}} className="hexagon"><TokenSlot/></div>
                        </div>   
                }
                </React.Fragment>
            )
        }

        // 3 slot countries:
        // australia, bolivia, canada, cuba, kazakhstan, madagascar, mongolia, morocco, newzealand, norway, peru, pnewguinea, sweden
        else if  (this.props.cardName === 'australia' ||
                  this.props.cardName === 'bolivia' ||
                  this.props.cardName === 'canada' ||
                  this.props.cardName === 'cuba' ||
                  this.props.cardName === 'kazakhstan' ||
                  this.props.cardName === 'madagascar' ||
                  this.props.cardName === 'mongolia' ||
                  this.props.cardName === 'morocco' ||
                  this.props.cardName === 'newzealand' ||
                  this.props.cardName === 'norway' ||
                  this.props.cardName === 'peru' ||
                  this.props.cardName === 'pnewguinea' ||
                  this.props.cardName === 'sweden'){
            return(
                <React.Fragment>{
                        <div>
                            <img src={`/countries/${this.props.cardName}.png`} className="card"alt="img"/>
    
                            <div style={{top:'22%', left:'40.5%'}} className="hexagon"><TokenSlot/></div>
                            <div style={{top:'61.5%', left:'15.5%'}} className="hexagon"><TokenSlot/></div>
                            <div style={{top:'61.5%', left:'64.0%'}} className="hexagon"><TokenSlot/></div>
                        </div>   
                }
                </React.Fragment>
            )
        }

        // 4 slot countries:
        // chile, colombia, iran, iraq, poland, saudiarabia, spain, argentina, sudan, uk, ukraine, venezuela
        else if  (this.props.cardName === 'argentina' ||
                  this.props.cardName === 'chile' ||
                  this.props.cardName === 'colombia' ||
                  this.props.cardName === 'iran' ||
                  this.props.cardName === 'iraq' ||
                  this.props.cardName === 'poland' ||
                  this.props.cardName === 'saudiarabia' ||
                  this.props.cardName === 'spain' ||
                  this.props.cardName === 'sudan' ||
                  this.props.cardName === 'uk' ||
                  this.props.cardName === 'ukraine' ||
                  this.props.cardName === 'venezuela'){
            return(
                <React.Fragment>{
                        <div>
                            <img src={`/countries/${this.props.cardName}.png`} className="card"alt="img"/>
    
                            <div style={{top:'20%', left:'15.5%'}} className="hexagon"><TokenSlot/></div>
                            <div style={{top:'20%', left:'66%'}} className="hexagon"><TokenSlot/></div>
                            <div style={{top:'61.0%', left:'16%'}} className="hexagon"><TokenSlot/></div>
                            <div style={{top:'60.5%', left:'66.0%'}} className="hexagon"><TokenSlot/></div>
                        </div>   
                }
                </React.Fragment>
            )
        }

        // 5 slot countries:
        // egypt, germany, kenya, philippines, france, southafrica, southkorea, turkey
        else if  (this.props.cardName === 'egypt' ||
                  this.props.cardName === 'france' ||
                  this.props.cardName === 'germany' ||
                  this.props.cardName === 'kenya' ||
                  this.props.cardName === 'philippines' ||    
                  this.props.cardName === 'southafrica' ||
                  this.props.cardName === 'southkorea' ||
                  this.props.cardName === 'turkey'){
            return(
                <React.Fragment>{
                        <div>
                            <img src={`/countries/${this.props.cardName}.png`} className="card"alt="img"/>
    
                            <div style={{top:'20%', left:'24.5%'}} className="hexagon"><TokenSlot/></div>
                            <div style={{top:'20%', left:'57%'}} className="hexagon"><TokenSlot/></div>
                            <div style={{top:'60.0%', left:'7%'}} className="hexagon"><TokenSlot/></div>
                            <div style={{top:'60.0%', left:'40%'}} className="hexagon"><TokenSlot/></div>
                            <div style={{top:'60.0%', left:'74.0%'}} className="hexagon"><TokenSlot/></div>
                        </div>   
                }
                </React.Fragment>
            )
        }

        // 6 slot countries:
        // ethiopia, mexico, nigeria, pakistan, russia, japan
        else if  (this.props.cardName === 'ethiopia' ||
                  this.props.cardName === 'japan' ||
                  this.props.cardName === 'mexico' ||
                  this.props.cardName === 'nigeria' ||
                  this.props.cardName === 'pakistan' ||
                  this.props.cardName === 'russia'){
            return(
                <React.Fragment>{
                        <div>
                            <img src={`/countries/${this.props.cardName}.png`} className="card"alt="img"/>

                            <div style={{top:'21%', left:'8%'}} className="hexagon"><TokenSlot/></div>
                            <div style={{top:'21%', left:'40.5%'}} className="hexagon"><TokenSlot/></div>
                            <div style={{top:'21%', left:'75%'}} className="hexagon"><TokenSlot/></div>
                            <div style={{top:'60.5%', left:'7.5%'}} className="hexagon"><TokenSlot/></div>
                            <div style={{top:'60.5%', left:'40.5%'}} className="hexagon"><TokenSlot/></div>
                            <div style={{top:'60.5%', left:'74.5%'}} className="hexagon"><TokenSlot/></div>
                        </div>   
                    }
                </React.Fragment>
                )
        }

        // 7 slot countries:
        // brazil, indonesia, usa
        else if  (this.props.cardName === 'brazil' ||
                  this.props.cardName === 'indonesia' ||
                  this.props.cardName === 'usa'){
            return(
                <React.Fragment>{
                        <div>
                            <img src={`/countries/${this.props.cardName}.png`} className="card"alt="img"/>

                            <div style={{top:'20.5%', left:'25%'}} className="hexagon"><TokenSlot/></div>
                            <div style={{top:'21%', left:'57%'}} className="hexagon"><TokenSlot/></div>
                            <div style={{top:'40%', left:'9%'}} className="hexagon"><TokenSlot/></div>
                            <div style={{top:'40%', left:'72.5%'}} className="hexagon"><TokenSlot/></div>
                            <div style={{top:'60%', left:'8.5%'}} className="hexagon"><TokenSlot/></div>
                            <div style={{top:'60%', left:'41.5%'}} className="hexagon"><TokenSlot/></div>
                            <div style={{top:'60%', left:'72.5%'}} className="hexagon"><TokenSlot/></div>
                        </div>   
                    }
                </React.Fragment>
                )
        }

        // 8 slot countries:
        // china, india
        else if  (this.props.cardName === 'china' ||
                  this.props.cardName === 'india'){
            return(
                <React.Fragment>{
                        <div>
                            <img src={`/countries/${this.props.cardName}.png`} className="card"alt="img"/>

                            <div style={{top:'21%', left:'8%'}} className="hexagon"><TokenSlot/></div>
                            <div style={{top:'21.5%', left:'42%'}} className="hexagon"><TokenSlot/></div>
                            <div style={{top:'21%', left:'75%'}} className="hexagon"><TokenSlot/></div>
                            <div style={{top:'41%', left:'7.5%'}} className="hexagon"><TokenSlot/></div>
                            <div style={{top:'41.5%', left:'74.5%'}} className="hexagon"><TokenSlot/></div>
                            <div style={{top:'61.%', left:'7%'}} className="hexagon"><TokenSlot/></div>
                            <div style={{top:'61%', left:'41.5%'}} className="hexagon"><TokenSlot/></div>
                            <div style={{top:'61%', left:'75%'}} className="hexagon"><TokenSlot/></div>
                        </div>   
                    }
                </React.Fragment>
                )
        }
    }


}

export default CountryCard;