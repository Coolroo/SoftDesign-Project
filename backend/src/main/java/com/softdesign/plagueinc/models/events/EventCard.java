package com.softdesign.plagueinc.models.events;

import com.softdesign.plagueinc.models.gamestate.GameState;
import com.softdesign.plagueinc.models.plague.Plague;

public record EventCard(String name, Event effect) {

    public void resolveEffect(Plague plague, GameState gameState){
        effect.op(plague, gameState);
    }
    
}
