package com.softdesign.plagueinc.models.plague.abilities;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.softdesign.plagueinc.models.gamestate.GameState;
import com.softdesign.plagueinc.models.gamestate.PlayState;
import com.softdesign.plagueinc.models.plague.trait_slot.TraitSlot;
import com.softdesign.plagueinc.models.traits.TraitCard;

public class RandomMutation extends Ability {

    private static final int DISCOUNT = 3;

    public RandomMutation() {
        super("random_mutation");
    }

    @Override
    public void resolveAbility(GameState gameState){
        TraitCard card = gameState.drawTraitCard();
        if(gameState.getCurrTurn().getDnaPoints() >= card.cost() - DISCOUNT){
            gameState.setPlayState(PlayState.ABILITY_ACTIVATION);
            gameState.setSelectTraitSlot(Optional.of(selectTraitSlotFuture(gameState, card)));
        }
        else{
            gameState.getCurrTurn().drawTraitCard(card);
            gameState.setReadyToProceed(true);
        }
    }

    public CompletableFuture<Integer> selectTraitSlotFuture(GameState gameState, TraitCard traitCard){
        CompletableFuture<Integer> future = new CompletableFuture<>();
        future.whenComplete((result, ex) -> {
            if(ex != null){
                logger.error("Error with Random Mutation Future EX: {}", ex.getMessage());
                gameState.setSelectTraitSlot(Optional.of(selectTraitSlotFuture(gameState, traitCard)));
            }
            else{
                TraitSlot slot = gameState.getCurrTurn().getTraitSlots().get(result);
                if(slot.hasCard()){
                    logger.warn("Plague attempted to use Random Evolve on a slot that already has {} in it", slot.getCard().name());
                    gameState.setSelectTraitSlot(Optional.of(selectTraitSlotFuture(gameState, traitCard)));
                    return;
                }
                else{
                    gameState.getCurrTurn().evolveTrait(traitCard, result, DISCOUNT);
                    gameState.setPlayState(PlayState.EVOLVE);
                    gameState.setReadyToProceed(true);
                }
            }
        });
        return future;
    }
}
