package com.softdesign.plagueinc.controllers.managers;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.softdesign.plagueinc.models.countries.Continent;
import com.softdesign.plagueinc.models.countries.Country;
import com.softdesign.plagueinc.models.gamestate.GameState;
import com.softdesign.plagueinc.models.gamestate.PlayState;
import com.softdesign.plagueinc.models.plague.Plague;
import com.softdesign.plagueinc.models.traits.TraitCard;
import com.softdesign.plagueinc.models.traits.travel.AirborneTrait;
import com.softdesign.plagueinc.models.traits.travel.WaterborneTrait;

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



    public boolean canInfectCountry(Country country, Plague plague){

        //If the country is already full
        if(country.getCities().values().stream().allMatch(thisPlague -> thisPlague.isPresent())){
            return false;
        }

        //If the country has a restriction, make sure the player has it
        if(country.hasRestriction() && !plague.hasTrait(country.getRestriction().get().getTrait())){
            return false;
        }

        //Get all countries that the player infects
        List<Country> infectedCountries = gameState.getBoard()
        .values()
        .stream()
        .flatMap(continent -> continent.stream())
        .filter(thisCountry -> thisCountry.getCities().values().stream().filter(Optional::isPresent).map(Optional::get).anyMatch(thisPlague -> thisPlague.equals(plague)))
        .toList();

        //Check what continent the player is present in
        List<Continent> continentPresence = Stream.of(Continent.values())
        .filter(continent -> infectedCountries.stream()
        .anyMatch(thisCountry -> thisCountry.getContinent() == continent))
        .toList();

        //If the player infects a country in the same continent, then they can infect this country
        if(continentPresence.contains(country.getContinent())){
            return true;
        }

        //If the player infects a country with an airport, and this country has an airport
        boolean airportConnected = infectedCountries.stream().anyMatch(thisCountry -> thisCountry.getTravelTypes().contains(new AirborneTrait()))
        && country.getTravelTypes().contains(new AirborneTrait());

        //If the player infects a country with a seaport, and this country has a seaport
        boolean waterportConnected = infectedCountries.stream().anyMatch(thisCountry -> thisCountry.getTravelTypes().contains(new WaterborneTrait())) 
        && country.getTravelTypes().contains(new WaterborneTrait());

        return airportConnected || waterportConnected;
    }

    public GameState getGameState(){
        return gameState;
    }
    
}
