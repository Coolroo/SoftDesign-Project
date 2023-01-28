import React, { Component } from "react";
import icon from '../images/heatresistance.png';

class HeatResistanceBar extends Component{

    render() {
        return(
            <React.Fragment>{
                <div className="traitBar heatResistanceBar">
                    <span>HEAT RESISTANCE</span>
                    <span className="barIcon"><img src={icon} alt="img" height="20" width ="20"/></span>
                </div>
                }
            </React.Fragment>
        )
    }
}

export default HeatResistanceBar;