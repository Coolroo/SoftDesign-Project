package com.softdesign.plagueinc.models.events;

import com.softdesign.plagueinc.models.gamestate.GameState;
import com.softdesign.plagueinc.models.plague.Plague;

public record EventCard(String name, GameStateAction condition, GameStateAction effect) {

    public void condition(Plague plague, GameState gameState){
        condition.op(plague, gameState);
    }

    public void resolveEffect(Plague plague, GameState gameState){
        effect.op(plague, gameState);
    }
    
    public static interface GameStateAction {

        public void op(Plague player, GameState gameState);
    
    }

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(!(o instanceof EventCard)){
            return false;
        }
        EventCard other = (EventCard) o;
        return name.equals(other.name);
    }
}
