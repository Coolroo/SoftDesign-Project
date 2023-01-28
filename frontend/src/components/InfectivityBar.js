import React, { Component } from "react";
import icon from '../images/infectivity.png';

class InfectivityBar extends Component{

    render() {
        return(
            <React.Fragment>{
                <div className="traitBar infectivityBar">
                    <span>INFECTIVITY +1</span>
                    <span className="barIcon"><img src={icon} alt="img" height="20" width ="20"/></span>
                </div>
                }
            </React.Fragment>
        )
    }
}

export default InfectivityBar;