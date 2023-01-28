import React, { Component } from "react";
import DNAicon from '../images/DNA.png';
import WaterborneBar from './WaterborneBar';
import AirborneBar from './AirborneBar';
import LethalityBar from './LethalityBar';
import ColdResistanceBar from './ColdResistanceBar';
import HeatResistanceBar from './HeatResistanceBar';
import InfectivityBar from './InfectivityBar';
import icon from '../images/DNA.png';

class TraitCard extends Component{

    render() {
        return(
            <React.Fragment>{
                    <div className="traitCard">
                        <div className="topCardBar">
                            <span className="traitCardCost">
                                <img src={DNAicon} alt="img" height="12" width="12"/>
                                {this.props.cost}
                            </span>
                            <span className="traitCardName">{this.props.name}</span>
                        </div>
                        <div className="traitCardIcon">
                            <img src={icon} alt="img" height="100" width="100"/>
                        </div>
                        <React.Fragment><WaterborneBar /></React.Fragment>
                        <React.Fragment><AirborneBar /></React.Fragment>
                        <React.Fragment><LethalityBar /></React.Fragment>
                        <React.Fragment><ColdResistanceBar /></React.Fragment>
                        <React.Fragment><HeatResistanceBar /></React.Fragment>
                        <React.Fragment><InfectivityBar /></React.Fragment>
                        <div className="traitCardDescription">{this.props.description}</div>
                    </div>   
                }
            </React.Fragment>
        )
    }
}

export default TraitCard;