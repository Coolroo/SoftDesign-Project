package com.softdesign.plagueinc.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.softdesign.plagueinc.exceptions.ContinentFullException;
import com.softdesign.plagueinc.managers.futures.input_types.CountryChoice;
import com.softdesign.plagueinc.models.action_log.CountryAction;
import com.softdesign.plagueinc.models.action_log.CountryChosenAction;
import com.softdesign.plagueinc.models.action_log.EvolveTraitAction;
import com.softdesign.plagueinc.models.action_log.InfectCountryAction;
import com.softdesign.plagueinc.models.action_log.KillCountryAction;
import com.softdesign.plagueinc.models.countries.Country;
import com.softdesign.plagueinc.models.events.Event;
import com.softdesign.plagueinc.models.gamestate.GameState;
import com.softdesign.plagueinc.models.gamestate.PlayState;
import com.softdesign.plagueinc.models.plague.Plague;
import com.softdesign.plagueinc.models.traits.TraitCard;
import com.softdesign.plagueinc.models.traits.TraitType;
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
    public Plague joinGame(){
        if(gameState.getPlayState() != PlayState.INITIALIZATION)
        {
            logger.warn("Player attempted to join game but game is already started");
            throw new IllegalStateException();
        }

        if(gameState.getPlagues().size() >= 4){
            logger.warn("Player attempted to join game but game is already full");
            throw new IllegalStateException();
        }

        //Create a new plague, and add it to the gameState
        Plague plague = new Plague();
        gameState.getPlagues().add(plague);
        gameState.getVotesToStart().put(plague, false);
        return plague;
    }

    public void startGame(UUID playerId){
        if(gameState.getPlayState() != PlayState.INITIALIZATION){
            logger.warn("(Plague {}) voted to start the game, but the game has already started");
            throw new IllegalStateException();
        }
        
        //Find the plague with this UUID
        Plague plague = gameState.getPlagues()
        .stream()
        .filter(pla -> pla.getPlayerId() == playerId)
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);

        if(gameState.getVotesToStart().get(plague)){
            logger.warn("(Plague {}) attempted to vote to start the game, but has already voted to start", plague.getPlayerId());
        }

        //mark this plague as voted to start
        gameState.getVotesToStart().put(plague, true);
        
        logger.info("(Plague {}) has voted to start the game");

        //If all players have voted to start, and there are more than 1 players in the lobby, then start the game
        if(gameState.getVotesToStart().values().stream().allMatch(bool -> bool) && gameState.getPlagues().size() > 1){
            logger.info("All players have voted to start the game, initializing game");
            
            //init starting country deck
            List<Country> startingCountries = new ArrayList<>(CountryReference.getStartingCountries());
            Collections.shuffle(startingCountries);

            //Init turn order
            List<Plague> players = gameState.getPlagues();
            Collections.shuffle(players);
            gameState.setTurnOrder(players);

            //Go through all the players, and initialize their play states (Starting country, starting DNA, starting Traits)
            IntStream.range(0, players.size()).forEach(index -> {
                Plague thisPlague = players.get(index);
                //Give player default points
                thisPlague.addDnaPoints(index);

                //Infect initial country
                Country startingCountry = startingCountries.remove(0);
                placeCountry(startingCountry);
                countryManager.infectCountry(startingCountry, thisPlague);

                //draw initial traits
                gameState.drawTraitCards(5).forEach(card -> thisPlague.drawTraitCard(card));
                logger.info("(Plague {}) initialized", thisPlague.getPlayerId());
            });

            //Init the country deck in the game state
            gameState.initCountryDeck(startingCountries);
        
            gameState.setCurrTurn(gameState.getTurnOrder().poll());

            //Prep the gamestate to proceed
            gameState.setPlayState(PlayState.START_OF_TURN);
            gameState.setReadyToProceed(true);
        }

    }

    public void proceedState(){
    if(!gameState.getReadyToProceed()){
        logger.warn("Player attempted to proceed the game state before it was ready to move on (Current state: {})", gameState.getPlayState());
        throw new IllegalStateException();
    }

    gameState.setReadyToProceed(false);
    switch(gameState.getPlayState()){
        case START_OF_TURN:
            //Score DNA Points & Mark as ready to proceed
            gameState.setPlayState(PlayState.DNA);
            scoreDNAPoints();
            break;
        case DNA:
            //Move on to choose country phase
            gameState.setPlayState(PlayState.CHOOSECOUNTRY);
            break;
        case CHOOSECOUNTRY:
            //init playCountry future so player can asynchronously choose which action to take, and then proceed to the play country state
            Country chosenCountry = gameState.getActions()
            .stream()
            .filter(action -> action.getClass().equals(CountryChosenAction.class))
            .map(action -> (CountryChosenAction)action)
            .findFirst()
            .get()
            .getCountry();

            initCountryChoiceFuture(chosenCountry);
            gameState.setPlayState(PlayState.PLAYCOUNTRY);
            break;
        case PLAYCOUNTRY:
            //Move on to evolve phase
            gameState.setPlayState(PlayState.EVOLVE);
            break;
        case EVOLVE:
            //Initialize the infect future, and then move onto the infect phase
            initInfectFuture(gameState.getCurrTurn().getTraitCount(TraitType.INFECTIVITY));
            gameState.setPlayState(PlayState.INFECT);
            break;
        case INFECT:
            //Init the death phase, by creating a recursive future that will go through all the killable countries, and then move on to the death state
            initDeathPhase();
            gameState.setPlayState(PlayState.DEATH);
            break;
        case DEATH:
            //Move on to the end of round state
            gameState.setPlayState(PlayState.END_OF_TURN);
            gameState.setReadyToProceed(true);
            break;
        case END_OF_TURN:

            if(checkForWin()){
                gameState.endGame();
                return;
            }
            //Shift turn to next player in line
            gameState.clearActionLog();
            gameState.shiftTurnOrder();

            gameState.setPlayState(PlayState.START_OF_TURN);
            gameState.setReadyToProceed(true);
            break;
        default:
            logger.error("Weird... this should never happen");
            break;
    }
}

    private boolean checkForWin(){

        return gameState.getPlagues().stream().anyMatch(plague -> gameState.isPlagueEradicated(plague) || gameState.unableToMove(plague));
    }

    //DNA PHASE

    public void scoreDNAPoints(){
        if(gameState.getReadyToProceed()){
            logger.warn("Attempted to score DNA points multiple times");
            throw new IllegalStateException();
        }
        //Get all countries controlled by the player
        List<Country> controllingCountries = gameState.getBoard()
        .values()
        .stream()
        .flatMap(list -> list.stream())
        .filter(country -> countryManager.getControllers(country)
        .contains(gameState.getCurrTurn()))
        .toList();

        //Give DNA points to the player
        gameState.getCurrTurn().addDnaPoints(controllingCountries.size());
        gameState.setReadyToProceed(true);
    }

    //COUNTRY PHASE

    public Country drawCountry(){
        if(gameState.getReadyToProceed()){
            logger.warn("Attempted to draw a country card when the gamestate was ready to proceed");
            throw new IllegalStateException();
        }
        if(gameState.getPlayState() != PlayState.CHOOSECOUNTRY){
            logger.error("Attempting to draw a country, when the play state is {}", gameState.getPlayState());
            throw new IllegalStateException();
        }

        //draw country, and log the action
        Country drawnCountry = gameState.drawCountry();
        gameState.logAction(new CountryChosenAction(drawnCountry));
        gameState.setReadyToProceed(true);
        return drawnCountry;
    }

    public Country selectCountryFromRevealed(int index){
        if(gameState.getReadyToProceed()){
            logger.warn("Attempted to choose a revealed country card when the gamestate was ready to proceed");
            throw new IllegalStateException();
        }
        if(gameState.getPlayState() != PlayState.CHOOSECOUNTRY){
            logger.error("Attempting to choose a country, when the play state is {}", gameState.getPlayState());
            throw new IllegalStateException();
        }

        //choose country, and log the action
        Country chosenCountry = gameState.takeRevealedCountry(index);
        initCountryChoiceFuture(chosenCountry);
        gameState.logAction(new CountryChosenAction(chosenCountry));
        gameState.setReadyToProceed(true);
        return chosenCountry;
    }

    private void initCountryChoiceFuture(Country drawnCountry){
        if(countryChoice.isPresent()){
            countryChoice.get().cancel(true);
        }
        
        //This part can get complicated! Please reach out to Wyatt if you have any issues understanding
        countryChoice = Optional.of(new CompletableFuture<>());
        countryChoice.get().whenComplete((result, ex) -> {
            if(ex != null){
                logger.error("Error with country choice future EX: {}", ex.getMessage());
                initCountryChoiceFuture(drawnCountry);
            }
            else{
                //Act on which response the player wishes to take
                switch(result){
                    case PLAY:
                    //Try to place the country, if there's an issue, we have a problem
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
                        //Discard the country
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

        if(gameState.getBoard().get(country.getContinent()).size() == GameState.MAX_COUNTRIES.get(country.getContinent())){
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
            //Try and evolve the card, and log it
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
        if(gameState.unableToMove(gameState.getCurrTurn())){
            logger.info("(Plague {}) cannot place any tokens, skipping infect phase", gameState.getCurrTurn().getPlayerId());
            gameState.setReadyToProceed(true);
            return;
        }

        //This part can get complicated! Please reach out to Wyatt if you have any issues understanding
        infectChoice = Optional.of(new CompletableFuture<>());
        infectChoice.get().whenComplete((country, ex) -> {
            if(ex != null){
                logger.warn("Error with infection choice future EX: {}", ex.getMessage());
            }
            else{
                //Try and infect the chosen country, then if there are any more countries that the player can infect, create another future
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

    public void attemptInfect(String countryName){
        if(gameState.getPlayState() != PlayState.INFECT){
            logger.warn("Attempted to infect {}, but the game state is {}", countryName, gameState.getPlayState());
            throw new IllegalStateException();
        }
        if(gameState.getReadyToProceed()){
            logger.warn("Attempted to infect {}, but the game is ready to proceed", countryName);
            throw new IllegalStateException();
        }
        if(infectChoice.isEmpty()){
            logger.warn("Attempted to infect {}, but the future is not valid", countryName);
            throw new IllegalStateException();
        }

        Country country = gameState.getBoard()
        .values()
        .stream()
        .flatMap(continent -> continent.stream())
        .filter(thisCountry -> thisCountry.getCountryName().equals(countryName))
        .findFirst().orElseThrow(IllegalArgumentException::new);

        infectChoice.get().complete(country);
    }

    private void infectCountry(Country country){
        if(gameState.getReadyToProceed()){
            logger.warn("Cannot take an action if the gamestate is ready to proceed");
            throw new IllegalStateException("Already placed country");
        }
        if(gameState.getPlayState() != PlayState.INFECT){
            logger.error("Attempting to infect a country, but the game state is {}", gameState.getPlayState());
            throw new IllegalStateException();
        }
        if(!gameState.canInfectCountry(country, gameState.getCurrTurn())){
            logger.warn("(Plague {}) attempted to infect {}, but is unable to", gameState.getCurrTurn().getPlayerId(), country.getCountryName());
            throw new IllegalStateException();
        }

        countryManager.infectCountry(country, gameState.getCurrTurn());
        gameState.logAction(new InfectCountryAction(country));

    }

    //DEATH PHASE

    private void initDeathPhase(){
        //get all killable countries, then make a future from them
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

        if(choppingBlock.size() == 0){
            logger.info("(Plague {}) has no countries to kill, skipping death phase", gameState.getCurrTurn().getPlayerId());
            gameState.setReadyToProceed(true);
            return;
        }
        //This part can get complicated! Please reach out to Wyatt if you have any issues understanding
        deathFuture = Optional.of(new CompletableFuture<>());
        deathFuture.get().whenComplete((result, ex) -> {
            if(ex != null){
                logger.warn("Death future failed! For reason EX: {}", ex.getMessage());
            }
            else{
                //If the players lethality is greater than or equal to the roll, then kill the country
                if(result <= gameState.getCurrTurn().getTraitCount(TraitType.LETHALITY)){
                    killCountry(choppingBlock.get(0), result);
                }
                else{
                    failKillCountry(choppingBlock.get(0), result);
                }
                //If there are more countries to kill, then reset the future
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

        //Give players points based on how many tokens they have in the country
        Map<Plague, Long> infectionCount = country.getInfectionByPlayer();
        infectionCount.keySet().forEach(plague -> plague.addDnaPoints(infectionCount.get(plague).intValue()));

        //Give players back their tokens
        infectionCount.entrySet().forEach(entry -> entry.getKey().returnPlagueTokens(entry.getValue().intValue()));

        //Discard the country that was killed, and mark it as killed by this player
        gameState.discardCountry(country);
        gameState.getCurrTurn().killCountry(country);
        //give everyone who was present an event card, and log the kill
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

    public GameState getGameState(){
        return gameState;
    }

    public boolean verifyTurn(UUID playerId){
        return gameState.getCurrTurn().getPlayerId().equals(playerId);
    }
    
}
