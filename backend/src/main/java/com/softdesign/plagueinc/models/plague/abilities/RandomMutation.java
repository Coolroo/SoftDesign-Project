package com.softdesign.plagueinc.models.plague.abilities;

import java.util.List;

import com.softdesign.plagueinc.models.gamestate.GameStateAction;
import com.softdesign.plagueinc.models.gamestate.InputSelection;


public class RandomMutation extends Ability {

    private static final int DISCOUNT = 3;

    public RandomMutation(GameStateAction condition, GameStateAction action) {
        super("random_mutation", condition, action, List.of(InputSelection.TRAIT_SLOT));
    }

    @Override
    public Ability create(){
        //TODO: Implement this ability
        return null;
    }
}
