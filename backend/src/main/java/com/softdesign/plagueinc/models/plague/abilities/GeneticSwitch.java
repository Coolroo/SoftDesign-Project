package com.softdesign.plagueinc.models.plague.abilities;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.softdesign.plagueinc.models.gamestate.GameState;
import com.softdesign.plagueinc.models.gamestate.PlayState;
import com.softdesign.plagueinc.models.plague.trait_slot.TraitSlot;
import com.softdesign.plagueinc.models.traits.TraitCard;

public class GeneticSwitch extends Ability {

    public GeneticSwitch() {
        super("genetic_switch");
    }

    @Override
    public void resolveAbility(GameState gameState){
        if(gameState.getPlayState() != PlayState.EVOLVE || gameState.isReadyToProceed()){
            logger.warn("Player attempted to use Genetic Switch, but the game state is invalid (PlayState: {}, readyToProceed: {}", gameState.getPlayState(), gameState.isReadyToProceed());
            return;
        }
        gameState.setPlayState(PlayState.ABILITY_ACTIVATION);
        gameState.setSelectTraitCard(Optional.of(selectTraitCard(gameState)));

    }

    private CompletableFuture<TraitCard> selectTraitCard(GameState gameState){
        CompletableFuture<TraitCard> cardFuture = new CompletableFuture<>();
        cardFuture.whenComplete((result, ex) -> {
            if(ex != null){
                logger.error("Error with Genetic Switch Future EX: {}", ex.getMessage());
                gameState.setSelectTraitCard(Optional.of(selectTraitCard(gameState)));
            }
            else{
                if(!gameState.getCurrTurn().getHand().contains(result)){
                    logger.warn("The selected card is not in the players hand");
                    gameState.setSelectTraitCard(Optional.of(selectTraitCard(gameState)));
                    return;
                }
                    gameState.setSelectTraitCard(Optional.empty());
                    gameState.setSelectTraitSlot(Optional.of(selectTraitSlot(gameState, result)));
            }
        });
        return cardFuture;
    }

    private CompletableFuture<Integer> selectTraitSlot(GameState gameState, TraitCard traitCard){
        CompletableFuture<Integer> slotFuture = new CompletableFuture<>();
        slotFuture.whenComplete((result, ex) -> {
            if(ex != null){
                logger.error("Error with Genetic Switch Trait Slot Future EX: {}", ex.getMessage());
                gameState.setSelectTraitSlot(Optional.of(selectTraitSlot(gameState, traitCard)));
            }
            else{
                TraitSlot slot = gameState.getCurrTurn().getTraitSlots().get(result);
                int discount = 0;
                if(slot.hasCard()){
                    //TODO: Implement check to ensure player is not softlocked
                    TraitCard card = slot.getCard();
                    if(gameState.getCurrTurn().getDnaPoints() >= traitCard.cost() - card.cost()){
                        slot.activate(gameState);
                        discount = card.cost();
                        gameState.getCurrTurn().drawTraitCard(card);
                    }
                    else{
                        logger.error("Plague does not have enough DNA points to evolve this trait");
                        gameState.setSelectTraitSlot(Optional.of(selectTraitSlot(gameState, traitCard)));
                        return;
                    }
                }
                gameState.getCurrTurn().evolveTrait(traitCard, result, discount);
                gameState.setPlayState(PlayState.EVOLVE);
                gameState.setReadyToProceed(true);
            }
        });
        return slotFuture;
    }
}
