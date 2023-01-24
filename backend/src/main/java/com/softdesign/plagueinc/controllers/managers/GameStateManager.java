package com.softdesign.plagueinc.controllers.managers;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.softdesign.plagueinc.models.countries.Country;
import com.softdesign.plagueinc.models.gamestate.GameState;
import com.softdesign.plagueinc.models.gamestate.PlayState;
import com.softdesign.plagueinc.models.plague.Plague;
import com.softdesign.plagueinc.models.traits.TraitCard;

public class GameStateManager {

    Logger logger = LoggerFactory.getLogger(GameStateManager.class);

    @Autowired
    private PlagueManager plagueManager;

    @Autowired
    private CountryManager countryManager;

    private GameState gameState;

    

    public GameStateManager(){
        this.gameState = new GameState();
    }

    public Optional<Plague> joinGame(){
        if(gameState.getPlayState() != PlayState.INITIALIZATION)
        {
            logger.warn("Player attempted to join game but game is already started");
            return Optional.empty();
        }

        if(gameState.getPlagues().size() >= 4){
            logger.warn("Player attempted to join game but game is already full");
            return Optional.empty();
        }

        Plague plague = new Plague(gameState.getPlagues().size());
        gameState.getPlagues().add(plague);
        gameState.getVotesToStart().put(plague, false);
        return Optional.of(plague);
    }

    public void startGame(int playerId){
        Plague plague = gameState.getPlagues().stream().filter(pla -> pla.getPlayerId() == playerId).findFirst().orElseThrow(IllegalArgumentException::new);
        gameState.getVotesToStart().put(plague, true);
        
        logger.info("(Plague {}) has voted to start the game");

        if(gameState.getVotesToStart().values().stream().allMatch(bool -> bool) && gameState.getPlagues().size() > 1){
            logger.info("All players have voted to start the game, initializing game");
            //TODO: Implement initialization
        }

    }

    public void proceedState(){
        //TODO: Implement this method
    }

    public void scoreDNAPoints(){
        if(gameState.getReadyToProceed()){
            logger.warn("Attempted to score DNA points multiple times");
            throw new IllegalStateException();
        }
        List<Country> controllingCountries = gameState.getBoard().values().stream().flatMap(list -> list.stream()).filter(country -> countryManager.getControllers(country).contains(gameState.getCurrTurn())).toList();
        gameState.getCurrTurn().addDnaPoints(controllingCountries.size());
        gameState.setReadyToProceed(true);
    }

    public List<TraitCard> drawTraitCards(int numCards){
        List<TraitCard> drawnCards = new ArrayList<>();
        for(int i = 0; i<numCards; i++){
            drawnCards.add(drawTraitCard());
        }
        return drawnCards;
    }

    public TraitCard drawTraitCard(){
        if(gameState.getTraitDeck().size() == 0){
            refillTraitDeck();
        }

        return gameState.getTraitDeck().pop();
    }

    public void refillTraitDeck(){
        if(gameState.getTraitDeck().size() > 0){
            logger.warn("Attempted to refill the deck when there were cards present in it");
            throw new IllegalAccessError();
        }
        List<TraitCard> discard = gameState.getTraitDiscard().stream().toList();
        Collections.shuffle(discard);
        gameState.setTraitDeck(new ArrayDeque<>(discard));
        gameState.setTraitDiscard(new HashSet<>());
    }

    public void drawCountry(){
        //TODO: Implement this method
    }

    public void selectCountryFromRevealed(int index){
        //TODO: Implement this method
    }

    public void placeCountry(Country country){
        if(gameState.getReadyToProceed()){
            logger.warn("Cannot take an action if the gamestate is ready to proceed");
            throw new IllegalStateException("Already placed country");
        }
        if(gameState.getBoard().values().stream().flatMap(list -> list.stream()).anyMatch(placedCountry -> placedCountry.equals(country))){
            logger.error("Attempted to place a country {} that is already placed", country.getCountryName());
            throw new IllegalStateException("Country already placed");
        }

        if(gameState.getBoard().get(country.getContinent()).size() == GameState.maxCountries.get(country.getContinent())){
            logger.warn("Cannot place country as the continent {} is full", country.getContinent());
            throw new IllegalStateException("Continent is full");
        }

        gameState.getBoard().get(country.getContinent()).add(country);
        gameState.setReadyToProceed(true);
    }

    public void discardCountry(Country country){
        if(gameState.getReadyToProceed()){
            logger.warn("Cannot take an action if the gamestate is ready to proceed");
            throw new IllegalStateException("Already placed country");
        }

        gameState.getCurrTurn().clearHand().forEach(card -> gameState.getTraitDiscard().add(card));

        drawTraitCards(5).forEach(card -> gameState.getCurrTurn().drawTraitCard(card));

        gameState.setReadyToProceed(true);
    }

    public void evolveTrait(int traitSlot, int traitIndex){
        try{
            plagueManager.evolveTrait(gameState.getCurrTurn(), traitIndex, traitSlot);
            gameState.setReadyToProceed(true);
        }
        catch(Exception e){
            logger.error(e.getMessage());
            throw e;
        }
    }

    public void skipEvolve(){
        gameState.setReadyToProceed(true);
    }



    public GameState getGameState(){
        return gameState;
    }
    
}
