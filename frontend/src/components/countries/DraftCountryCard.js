import React, { Component } from "react";
import { useDrag } from 'react-dnd'
import { ItemTypes } from '../../util/ItemTypes.js'

export default function DraftCountryCard(props) {
    const cardName = props.cardName;
    const [{ opacity }, dragRef] = useDrag(
        () => ({
          type: ItemTypes.COUNTRY,
          item: { cardName },
          collect: (monitor) => ({
            opacity: monitor.isDragging() ? 0.5 : 1
          })
        }),
        []
      )
        return(
                <div ref={dragRef} style={{opacity}}>
                    <img src={`/countries/${cardName}.png`} className="card"alt="img"/>
                </div>
        )
        }
