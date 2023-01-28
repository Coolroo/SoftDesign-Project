import React, { Component } from "react";
import icon from '../images/airborne.png';

class AirborneBar extends Component{

    render() {
        return(
            <React.Fragment>{
                <div className="traitBar airborneBar">
                    <span>AIRBORNE</span>
                    <span className="barIcon"><img src={icon} alt="img" height="20" width ="20"/></span>
                </div>
                }
            </React.Fragment>
        )
    }
}

export default AirborneBar;