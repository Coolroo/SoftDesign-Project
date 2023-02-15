import React, { Component } from "react";
import { Droppable } from 'react-drag-and-drop';
class PlagueCardTraitSlot extends Component{

    drop = (item) => {
        console.log("drop");
        this.props.evolve(item.index, this.props.index);
    };

    preventDefault = () => (event) => {
        event.preventDefault();
        console.log("prevent default")
      }

    render() {
        

        return(
            <React.Fragment>{
                <Droppable className="plagueCardTraitSlot" onDragOver={this.preventDefault()} types={['trait']} onDrop={this.drop} style={{...this.props.loc, position:"absolute"}} >
                    <div >

                    </div>
                </Droppable> 
    }       </React.Fragment>
        )
    }
}

export default PlagueCardTraitSlot;