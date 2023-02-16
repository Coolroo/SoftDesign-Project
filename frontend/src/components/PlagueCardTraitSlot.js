import React, { Component } from "react";
import { Droppable } from 'react-drag-and-drop';
class PlagueCardTraitSlot extends Component{

    drop = (item) => {
        this.props.evolve(item.trait, this.props.index);
    };

    preventDefault = () => (event) => {
        event.preventDefault();
        console.log("prevent default")
      }

    render() {
       
        let card = () => {
            if(this.props.trait){
                return <img src={`/traitcards/${this.props.trait}.png`} alt="img" className="fullSize"/>
            }
        }

        return(
            <React.Fragment>{
                <Droppable className="plagueCardTraitSlot" onDragOver={this.preventDefault()} types={['trait']} onDrop={this.drop} style={{...this.props.loc, position:"absolute"}} >
                    {card()}
                </Droppable> 
    }       </React.Fragment>
        )
    }
}

export default PlagueCardTraitSlot;