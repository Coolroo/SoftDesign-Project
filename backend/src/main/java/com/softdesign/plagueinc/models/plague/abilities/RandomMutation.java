package com.softdesign.plagueinc.models.plague.abilities;

import java.util.List;

import com.softdesign.plagueinc.models.gamestate.GameStateAction;
import com.softdesign.plagueinc.models.gamestate.InputSelection;
import com.softdesign.plagueinc.models.plague.trait_slot.TraitSlot;
import com.softdesign.plagueinc.models.traits.TraitCard;
import com.softdesign.plagueinc.rest_controllers.DTOs.selection_objects.TraitSlotSelection;


public class RandomMutation extends Ability {

    private static final int DISCOUNT = 3;

    public RandomMutation(GameStateAction condition, GameStateAction action) {
        super("random_mutation", condition, action, List.of(InputSelection.TRAIT_SLOT));
    }

    public static Ability create(){

        GameStateAction condition = (plague, gameState, list) -> {
            if(plague.getTraitSlots().stream().allMatch(TraitSlot::hasCard)){
                throw new IllegalArgumentException();
            }
        };

        GameStateAction action = (plague, gameState, list) -> {
            int traitSlotIndex = ((TraitSlotSelection)list.get(0)).getTraitSlotIndex();
            TraitSlot slot = plague.getTraitSlot(traitSlotIndex);
            if(slot.hasCard()){
                throw new IllegalArgumentException();
            }
            TraitCard drawnCard = gameState.drawTraitCard();
            if(plague.getDnaPoints() < drawnCard.cost() - DISCOUNT){
                plague.drawTraitCard(drawnCard);
            }
            else{
                plague.evolveTrait(drawnCard, traitSlotIndex, DISCOUNT);
            }
        };
        return new RandomMutation(condition, action);
    }
}
