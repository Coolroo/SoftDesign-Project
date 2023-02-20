package com.softdesign.plagueinc.models.plague.abilities;

import java.util.List;

import com.softdesign.plagueinc.models.gamestate.GameStateAction;
import com.softdesign.plagueinc.models.gamestate.PlayState;

public class BonusDNA extends Ability {

    private BonusDNA(GameStateAction condition, GameStateAction action) {
        super("bonus_dna", condition, action, List.of());
    }

    @Override
    public Ability create(){
        GameStateAction condition = (plague, gameState, inputs) -> {
            if(gameState.getPlayState() != PlayState.DNA){
                logger.warn("Attempted to score bonus DNA in non-DNA phase");
                throw new IllegalAccessError();
            }
        };

        GameStateAction action = (plague, gameState, inputs) -> {
            gameState.getCurrTurn().addDnaPoints(1);
        };
        return new BonusDNA(condition, action);
    }
}
