import React, { Component } from "react";
import { Droppable } from 'react-drag-and-drop';
class PlagueCardTraitSlot extends Component{

    drop = (item) => {
        this.props.evolve(item.trait, this.props.index);
    };

    render() {
       
        let card = () => {
            if(this.props.trait){
                return <img src={`/traitcards/${this.props.trait}.png`} alt="img" className="fullSize curvedBorder" style={{"--borderRadius": "10px"}}/>
            }
        }

        return(
            <React.Fragment>{
                <Droppable className="plagueCardTraitSlot" types={['trait']} onDrop={this.drop} style={{...this.props.loc, position:"absolute"}} >
                    {card()}
                </Droppable> 
    }       </React.Fragment>
        )
    }
}

export default PlagueCardTraitSlot;