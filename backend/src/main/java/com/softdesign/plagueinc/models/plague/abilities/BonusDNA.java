package com.softdesign.plagueinc.models.plague.abilities;

import com.softdesign.plagueinc.models.gamestate.GameState;
import com.softdesign.plagueinc.models.gamestate.PlayState;

public class BonusDNA extends Ability {

    public BonusDNA() {
        super("bonus_dna");
    }

    @Override
    public void resolveAbility(GameState gameState){
        if(gameState.getPlayState() != PlayState.DNA){
            logger.warn("Attempted to score bonus DNA in non-DNA phase");
            throw new IllegalAccessError();
        }
        if(activated){
            logger.warn("Attempted to score bonus DNA more than once per turn");
            throw new IllegalAccessError();
        }
        gameState.getCurrTurn().addDnaPoints(1);
        activated = true;
    }
}
