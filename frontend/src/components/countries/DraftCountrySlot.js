import React, { Component } from "react";
import DraftCountryCard from "./DraftCountryCard";


class DraftCountrySlot extends Component{

    render() {
        return(
            <React.Fragment>{
                    <DraftCountryCard cardName={this.props.name}/>
            }       
            </React.Fragment>
        )
    }
}

export default DraftCountrySlot;