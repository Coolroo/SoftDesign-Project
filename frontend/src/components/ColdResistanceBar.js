import React, { Component } from "react";
import icon from '../images/coldresistance.png';

class ColdResistanceBar extends Component{

    render() {
        return(
            <React.Fragment>{
                <div className="traitBar coldResistanceBar">
                    <span>COLD RESISTANCE</span>
                    <span className="barIcon"><img src={icon} alt="img" height="20" width ="20"/></span>
                </div>
                }
            </React.Fragment>
        )
    }
}

export default ColdResistanceBar;