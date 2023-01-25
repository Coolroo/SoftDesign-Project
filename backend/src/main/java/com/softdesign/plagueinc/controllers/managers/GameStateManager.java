package com.softdesign.plagueinc.controllers.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.softdesign.plagueinc.controllers.futures.input_types.CountryChoice;
import com.softdesign.plagueinc.exceptions.ContinentFullException;
import com.softdesign.plagueinc.models.action_log.CountryAction;
import com.softdesign.plagueinc.models.action_log.CountryChosenAction;
import com.softdesign.plagueinc.models.action_log.EvolveTraitAction;
import com.softdesign.plagueinc.models.action_log.InfectCountryAction;
import com.softdesign.plagueinc.models.action_log.KillCountryAction;
import com.softdesign.plagueinc.models.countries.Continent;
import com.softdesign.plagueinc.models.countries.Country;
import com.softdesign.plagueinc.models.events.Event;
import com.softdesign.plagueinc.models.gamestate.GameState;
import com.softdesign.plagueinc.models.gamestate.PlayState;
import com.softdesign.plagueinc.models.plague.Plague;
import com.softdesign.plagueinc.models.traits.TraitCard;
import com.softdesign.plagueinc.models.traits.TraitType;
import com.softdesign.plagueinc.models.traits.travel.AirborneTrait;
import com.softdesign.plagueinc.models.traits.travel.WaterborneTrait;
import com.softdesign.plagueinc.util.CountryReference;

public class GameStateManager {

    Logger logger = LoggerFactory.getLogger(GameStateManager.class);

    @Autowired
    private PlagueManager plagueManager;

    @Autowired
    private CountryManager countryManager;

    private GameState gameState;

    private Optional<CompletableFuture<CountryChoice>> countryChoice;

    private Optional<CompletableFuture<Country>> infectChoice;

    private Optional<CompletableFuture<Integer>> deathFuture;

    public GameStateManager(){
        this.gameState = new GameState();
    }

/**
 * function that allows a player to join the game.
 *

 * @docauthor Trelent
 */
    public Optional<Plague> joinGame(){
        if(gameState.getPlayState() != PlayState.INITIALIZATION)
        {
            logger.warn("Player attempted to join game but game is already started");
            return Optional.empty();
        }

        if(gameState.getPlagues().size() >= GameState.MAX_PLAYERS){
            logger.warn("Player attempted to join game but game is already full");
            return Optional.empty();
        }

        Plague plague = new Plague();
        gameState.getPlagues().add(plague);
        gameState.getVotesToStart().put(plague, false);
        return Optional.of(plague);
    }

    public void startGame(UUID playerId){
        if(gameState.getPlayState() != PlayState.INITIALIZATION){
            logger.warn("(Plague {}) voted to start the game, but the game has already started");
            throw new IllegalStateException();
        }
        
        //
        Plague plague = gameState.getPlagues().stream().filter(pla -> pla.getPlayerId() == playerId).findFirst().orElseThrow(IllegalArgumentException::new);
        gameState.getVotesToStart().put(plague, true);
        
        logger.info("(Plague {}) has voted to start the game");

        if(gameState.getVotesToStart().values().stream().allMatch(bool -> bool) && gameState.getPlagues().size() > 1){
            logger.info("All players have voted to start the game, initializing game");
            
            List<Country> startingCountries = new ArrayList<>(CountryReference.getStartingCountries());
            Collections.shuffle(startingCountries);
            gameState.getPlagues().forEach(thisPlague -> {
                //Give player default points
                thisPlague.addDnaPoints(thisPlague.getPlayerId());

                //Infect initial country
                Country startingCountry = startingCountries.remove(0);
                placeCountry(startingCountry);
                countryManager.infectCountry(startingCountry, thisPlague);

                //draw initial traits
                gameState.drawTraitCards(5).forEach(card -> thisPlague.drawTraitCard(card));
                logger.info("(Plague {}) initialized", thisPlague.getPlayerId());
            });
            gameState.initCountryDeck(startingCountries);
            gameState.setCurrTurn(gameState.getPlagues().stream().filter(thisPlague -> thisPlague.getPlayerId() == 0).findFirst().get());
            gameState.setPlayState(PlayState.START_OF_TURN);
            gameState.setReadyToProceed(true);
        }

    }

    public void proceedState(){
        if(!gameState.getReadyToProceed()){
            logger.warn("Player attempted to proceed the game state before it was ready to move on (Current state: {})", gameState.getPlayState());
            throw new IllegalStateException();
        }

        boolean readyToProceedState = false;
        switch(gameState.getPlayState()){
            case START_OF_TURN:
                gameState.setPlayState(PlayState.DNA);
                scoreDNAPoints();
                readyToProceedState = true;
                break;
            case DNA:
                gameState.setPlayState(PlayState.CHOOSECOUNTRY);
                break;
            case CHOOSECOUNTRY:
                gameState.setPlayState(PlayState.PLAYCOUNTRY);
                break;
            case PLAYCOUNTRY:
                gameState.setPlayState(PlayState.EVOLVE);
                break;
            case EVOLVE:
                gameState.setPlayState(PlayState.INFECT);
                initInfectFuture(gameState.getCurrTurn().getTraitCount(TraitType.INFECTIVITY));
                break;
            case INFECT:
                gameState.setPlayState(PlayState.DEATH);
                initDeathPhase();
                break;
            case DEATH:
                gameState.setPlayState(PlayState.END_OF_TURN);
                readyToProceedState = true;
                break;
            case END_OF_TURN:
                gameState.setCurrTurn(gameState
                .getPlagues()
                .stream()
                .filter(plague -> plague.getPlayerId() == (gameState.getCurrTurn().getPlayerId() + 1) % gameState.getPlagues().size())
                .findFirst()
                .get());

                gameState.clearActionLog();
                gameState.setPlayState(PlayState.START_OF_TURN);
                readyToProceedState = true;
                break;
            default:
                break;
        }
        gameState.setReadyToProceed(readyToProceedState);
    }

    //DNA PHASE

    public void scoreDNAPoints(){
        if(gameState.getReadyToProceed()){
            logger.warn("Attempted to score DNA points multiple times");
            throw new IllegalStateException();
        }
        List<Country> controllingCountries = gameState.getBoard().values().stream().flatMap(list -> list.stream()).filter(country -> countryManager.getControllers(country).contains(gameState.getCurrTurn())).toList();
        gameState.getCurrTurn().addDnaPoints(controllingCountries.size());
        gameState.setReadyToProceed(true);
    }

    //COUNTRY PHASE

    public void drawCountry(){
        if(gameState.getReadyToProceed()){
            logger.warn("Attempted to draw a country card when the gamestate was ready to proceed");
            throw new IllegalStateException();
        }
        if(gameState.getPlayState() != PlayState.CHOOSECOUNTRY){
            logger.error("Attempting to draw a country, when the play state is {}", gameState.getPlayState());
            throw new IllegalStateException();
        }
        Country drawnCountry = gameState.drawCountry();
        initCountryChoiceFuture(drawnCountry);
        gameState.logAction(new CountryChosenAction(drawnCountry));
        gameState.setReadyToProceed(true);
    }

    public void selectCountryFromRevealed(int index){
        if(gameState.getReadyToProceed()){
            logger.warn("Attempted to choose a revealed country card when the gamestate was ready to proceed");
            throw new IllegalStateException();
        }
        if(gameState.getPlayState() != PlayState.CHOOSECOUNTRY){
            logger.error("Attempting to choose a country, when the play state is {}", gameState.getPlayState());
            throw new IllegalStateException();
        }

        Country chosenCountry = gameState.takeRevealedCountry(index);
        initCountryChoiceFuture(chosenCountry);
        gameState.logAction(new CountryChosenAction(chosenCountry));
        gameState.setReadyToProceed(true);
    }

    private void initCountryChoiceFuture(Country drawnCountry){
        if(countryChoice.isPresent()){
            countryChoice.get().cancel(true);
        }
        countryChoice = Optional.of(new CompletableFuture<>());
        countryChoice.get().whenComplete((result, ex) -> {
            if(ex != null){
                logger.error("Error with country choice future EX: {}", ex.getMessage());
                initCountryChoiceFuture(drawnCountry);
            }
            else{
                switch(result){
                    case PLAY:
                    try{
                        placeCountry(drawnCountry);
                        gameState.setReadyToProceed(true);
                        countryChoice = Optional.empty();
                    }
                    catch(ContinentFullException e){
                        initCountryChoiceFuture(drawnCountry);
                    }
                    break;
                    case DISCARD:
                        discardCountry(drawnCountry);
                        gameState.setReadyToProceed(true);
                        countryChoice = Optional.empty();
                    break;
                }
            }
        });
    }

    public void makeCountryChoice(CountryChoice choice){
        if(gameState.getReadyToProceed()){
            logger.warn("Attempted to make a country choice, but the gamestate is ready to proceed");
            throw new IllegalStateException();
        }
        if(countryChoice.isEmpty()){
            logger.error("Attempted to make a country choice, but the gamestate is not waiting for a country choice");
            throw new IllegalStateException();
        }
        if(gameState.getPlayState() != PlayState.PLAYCOUNTRY){
            logger.error("Attempting to play a country, but the game state is {}", gameState.getPlayState());
            throw new IllegalStateException();
        }
        countryChoice.get().complete(choice);
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
            throw new ContinentFullException();
        }

        gameState.getBoard().get(country.getContinent()).add(country);
        gameState.logAction(new CountryAction(CountryChoice.PLAY, country));
        gameState.setReadyToProceed(true);
    }

    public void discardCountry(Country country){
        if(gameState.getReadyToProceed()){
            logger.warn("Cannot take an action if the gamestate is ready to proceed");
            throw new IllegalStateException("Already placed country");
        }
        if(gameState.getPlayState() != PlayState.PLAYCOUNTRY){
            logger.error("Attempting to discard a country, but the game state is {}", gameState.getPlayState());
            throw new IllegalStateException();
        }

        gameState.discardCountry(country);

        gameState.getCurrTurn().clearHand().forEach(card -> gameState.discardTraitCard(card));

        gameState.drawTraitCards(5).forEach(card -> gameState.getCurrTurn().drawTraitCard(card));

        gameState.logAction(new CountryAction(CountryChoice.DISCARD, country));

        gameState.setReadyToProceed(true);
    }
    
    //EVOLVE PHASE

    public void evolveTrait(int traitSlot, int traitIndex){
        if(gameState.getReadyToProceed()){
            logger.warn("Cannot take an action if the gamestate is ready to proceed");
            throw new IllegalStateException("Already placed country");
        }
        if(gameState.getPlayState() != PlayState.EVOLVE){
            logger.error("Attempting to evolve a trait, but the game state is {}", gameState.getPlayState());
            throw new IllegalStateException();
        }
        try{
            TraitCard card = plagueManager.evolveTrait(gameState.getCurrTurn(), traitIndex, traitSlot);
            gameState.logAction(new EvolveTraitAction(card));
            gameState.setReadyToProceed(true);
        }
        catch(Exception e){
            logger.error(e.getMessage());
            throw e;
        }
    }

    public void skipEvolve(){
        if(gameState.getPlayState() != PlayState.EVOLVE){
            logger.error("Attempting to skip evolution, but the game state is {}", gameState.getPlayState());
            throw new IllegalStateException();
        }
        gameState.setReadyToProceed(true);
    }

    //INFECT PHASE

    private void initInfectFuture(int citiesToInfect){
        if(infectChoice.isPresent()){
            infectChoice.get().cancel(true);
        }
        infectChoice = Optional.of(new CompletableFuture<>());
        infectChoice.get().whenComplete((country, ex) -> {
            if(ex != null){
                logger.warn("Error with infection choice future EX: {}", ex.getMessage());
            }
            else{
                try{
                    infectCountry(country);
                    if(citiesToInfect > 1 && gameState.getCurrTurn().getPlagueTokens() > 0){
                        initInfectFuture(citiesToInfect - 1);
                    }
                    else{
                        gameState.setReadyToProceed(true);
                        infectChoice = Optional.empty();
                    }
                }
                catch(Exception e){
                    initInfectFuture(citiesToInfect);
                }
                
            }
        });
    }

    public void infectCountry(Country country){
        if(gameState.getReadyToProceed()){
            logger.warn("Cannot take an action if the gamestate is ready to proceed");
            throw new IllegalStateException("Already placed country");
        }
        if(gameState.getPlayState() != PlayState.INFECT){
            logger.error("Attempting to infect a country, but the game state is {}", gameState.getPlayState());
            throw new IllegalStateException();
        }
        if(!canInfectCountry(country, gameState.getCurrTurn())){
            logger.warn("(Plague {}) attempted to infect {}, but is unable to", gameState.getCurrTurn().getPlayerId(), country.getCountryName());
            throw new IllegalStateException();
        }

        countryManager.infectCountry(country, gameState.getCurrTurn());
        gameState.logAction(new InfectCountryAction(country));

    }

    //DEATH PHASE

    private void initDeathPhase(){
       List<Country> choppingBlock = gameState.getBoard()
        .values()
        .stream()
        .flatMap(continent -> continent.stream())
        .filter(country -> country.isFull())
        .filter(country -> countryManager.getControllers(country).contains(gameState.getCurrTurn()))
        .toList();
        createDeathFuture(choppingBlock);
    }

    public int rollDeathDice(){
        if(gameState.getReadyToProceed()){
            logger.error("Attempted to roll the death dice when the game state is ready to proceed");
            throw new IllegalStateException();
        }
        if(deathFuture.isEmpty()){
            logger.error("Attempted to roll death dice, but the future is invalid!");
            throw new IllegalStateException("Death state issue");
        }
        int randomNum = ThreadLocalRandom.current().nextInt(1, 7);
        deathFuture.get().complete(randomNum);
        return randomNum;
    }

    private void createDeathFuture(List<Country> choppingBlock){
        if(deathFuture.isPresent()){
            deathFuture.get().cancel(true);
        }
        deathFuture = Optional.of(new CompletableFuture<>());
        deathFuture.get().whenComplete((result, ex) -> {
            if(ex != null){
                logger.warn("Death future failed! For reason EX: {}", ex.getMessage());
            }
            else{
                if(result <= gameState.getCurrTurn().getTraitCount(TraitType.LETHALITY)){
                    killCountry(choppingBlock.get(0), result);
                }
                else{
                    failKillCountry(choppingBlock.get(0), result);
                }
                if(choppingBlock.size() > 1){
                    createDeathFuture(choppingBlock.subList(1, choppingBlock.size()));
                }
                else{
                    gameState.setReadyToProceed(true);
                }
            }
        });
    }

    private void killCountry(Country country, int roll){
        if(gameState.getReadyToProceed()){
            logger.warn("Cannot take an action if the gamestate is ready to proceed");
            throw new IllegalStateException("Already placed country");
        }
        if(gameState.getPlayState() != PlayState.DEATH){
            logger.error("Attempting to kill a country, but the game state is {}", gameState.getPlayState());
            throw new IllegalStateException();
        }
        Map<Plague, Long> infectionCount = countryManager.getInfectionByPlayer(country);
        infectionCount.keySet().forEach(plague -> plague.addDnaPoints(infectionCount.get(plague).intValue()));
        gameState.discardCountry(country);
        infectionCount.keySet().stream().filter(plague -> plague.getEventCards().size() < 3).forEach(plague -> plague.addEventCard(gameState.drawEventCard()));
        gameState.logAction(new KillCountryAction(country, roll, true));
    }

    private void failKillCountry(Country country, int roll){
        gameState.logAction(new KillCountryAction(country, roll, false));
    }

    //EVENT CARDS

    public void playEventCard(Event eventCard){
        //TODO: Implement this method
    }

    //UTIL

/**
 * The private function canInfectCountry checks if the country has a travel restriction, and if they do, it makes sure that the player has this restriction.
 * It also takes the continent the country is in, and then sees if there are any countries in those continents that have been infected by this player.
 * Otherwise, it determines if the player is present in any country with a seaport/airport, if the country provided has one of those.
 *
 * @return True if the player infects a country in the same continent, or if they have an airport/seaport to travel between continents
 *
 * @docauthor Trelent
 */
    private boolean canInfectCountry(Country country, Plague plague){

        //If the country is already full
        if(country.isFull()){
            logger.warn("(Plague {}) attempted to infect {}, but all the cities are full", plague.getPlayerId(), country.getCountryName());
            return false;
        }

        //If the country has a restriction, make sure the player has it
        if(country.hasRestriction() && !plague.hasTrait(country.getRestriction().get().getTrait())){
            logger.warn("(Plague {}) attempted to infect {}, but does not have the necessary climate restriction", plague.getPlayerId(), country.getCountryName());
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
