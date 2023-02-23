package com.softdesign.plagueinc.models.plague.abilities;

import java.util.List;

import com.softdesign.plagueinc.models.gamestate.GameStateAction;
import com.softdesign.plagueinc.models.gamestate.PlayState;

public class BonusDNA extends Ability {

    private BonusDNA(GameStateAction condition, GameStateAction action) {
        super("bonus_dna", condition, action, List.of());
    }

    public static BonusDNA create(){
        GameStateAction condition = (plague, gameState, inputs) -> {
            if(gameState.getPlayState() != PlayState.DNA){
                throw new IllegalAccessError();
            }
        };

        GameStateAction action = (plague, gameState, inputs) -> {
            gameState.getCurrTurn().addDnaPoints(1);
        };
        return new BonusDNA(condition, action);
    }
}
