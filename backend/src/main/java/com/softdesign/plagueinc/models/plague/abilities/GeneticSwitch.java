package com.softdesign.plagueinc.models.plague.abilities;

import java.util.List;
import com.softdesign.plagueinc.models.gamestate.GameStateAction;
import com.softdesign.plagueinc.models.gamestate.InputSelection;
import com.softdesign.plagueinc.models.gamestate.PlayState;
import com.softdesign.plagueinc.models.plague.trait_slot.TraitSlot;
import com.softdesign.plagueinc.models.traits.TraitCard;
import com.softdesign.plagueinc.rest_controllers.DTOs.selection_objects.TraitCardSelection;
import com.softdesign.plagueinc.rest_controllers.DTOs.selection_objects.TraitSlotSelection;
public class GeneticSwitch extends Ability {

    private GeneticSwitch(GameStateAction condition, GameStateAction action) {
        super("genetic_switch", condition, action, List.of(InputSelection.TRAIT_CARD, InputSelection.TRAIT_SLOT));
    }

    public static Ability create(){
        GameStateAction condition = (plague, gameState, list) -> {
            if(gameState.getPlayState() != PlayState.EVOLVE){
                throw new IllegalAccessError();
            }
        };

        GameStateAction action = (plague, gameState, list) -> {
            //Convert Actions
            int traitCardIndex = ((TraitCardSelection)(list.get(0))).getTraitCardSlot();
            int traitSlotIndex = ((TraitSlotSelection)(list.get(1))).getTraitSlotIndex();

            //Get Card/Slot
            TraitCard traitCard = plague.getTraitCardFromHand(traitCardIndex);
            TraitSlot traitSlot = plague.getTraitSlot(traitSlotIndex);

            //If the slot doesn't have a card, throw exception
            if(!traitSlot.hasCard()){
                throw new IllegalArgumentException();
            }
            //Get the card in the slot
            TraitCard slotCard = traitSlot.getCard();

            //Calculate the discount
            int discount = Math.max(0, traitCard.cost() - slotCard.cost()); 

            //If the player doesn't have enough DNA, throw exception
            if(plague.getDnaPoints() < (traitCard.cost() - discount)){
                throw new IllegalArgumentException();
            }

            //Remove the card from the slot
            traitSlot.refundCard();

            //Evolve the card into the slot and get the card back
            plague.evolveTrait(traitCardIndex, traitSlotIndex, discount);
            plague.drawTraitCard(slotCard);

        };
        return new GeneticSwitch(condition, action);
    }
}
