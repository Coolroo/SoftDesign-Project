package com.softdesign.plagueinc.models.plague.abilities;

import java.util.List;

import com.softdesign.plagueinc.models.gamestate.GameStateAction;
import com.softdesign.plagueinc.models.gamestate.InputSelection;
import com.softdesign.plagueinc.models.gamestate.selection_objects.TraitSlotSelection;
import com.softdesign.plagueinc.models.plague.trait_slot.TraitSlot;
import com.softdesign.plagueinc.models.traits.TraitCard;


public class RandomMutation extends Ability {

    private static final int DISCOUNT = 3;

    public RandomMutation(GameStateAction condition, GameStateAction action) {
        super("random_mutation", condition, action, List.of(InputSelection.TRAIT_SLOT));
    }

    @Override
    public Ability create(){

        GameStateAction condition = (plague, gameState, list) -> {
            if(plague.getTraitSlots().stream().allMatch(TraitSlot::hasCard)){
                logger.warn("Attempted to use ability {} when all trait slots are full", this.name);
                throw new IllegalArgumentException();
            }
        };

        GameStateAction action = (plague, gameState, list) -> {
            int traitSlotIndex = ((TraitSlotSelection)list.get(0)).getTraitSlotIndex();
            TraitSlot slot = plague.getTraitSlot(traitSlotIndex);
            if(slot.hasCard()){
                logger.warn("Attempted to use ability {} on a trait slot that already has a card", this.name);
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
