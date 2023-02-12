import React, { Component } from "react";
import DraftCountryCard from "./DraftCountryCard";

class DraftCountrySlot extends Component{

    render() {
        return(
            <React.Fragment>{
                <div>
                    <DraftCountryCard cardName={this.props.name}/>
                </div>   
            }       
            </React.Fragment>
        )
    }
}

export default DraftCountrySlot;