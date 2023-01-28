import React, { Component } from "react";
import icon from '../images/lethality.png';

class LethalityBar extends Component{

    render() {
        return(
            <React.Fragment>{
                <div className="traitBar lethalityBar">
                    <span>LETHALITY +1</span>
                    <span className="barIcon"><img src={icon} alt="img" height="20" width ="20"/></span>
                </div>
                }
            </React.Fragment>
        )
    }
}

export default LethalityBar;