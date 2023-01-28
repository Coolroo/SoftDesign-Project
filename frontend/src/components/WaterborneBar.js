import React, { Component } from "react";
import icon from '../images/waterborne.png';

class WaterborneBar extends Component{

    render() {
        return(
            <React.Fragment>{
                <div className="traitBar waterborneBar">
                    <span>WATERBORNE</span>
                    <span className="barIcon"><img src={icon} alt="img" height="20" width ="20"/></span>
                </div>
                }
            </React.Fragment>
        )
    }
}

export default WaterborneBar;