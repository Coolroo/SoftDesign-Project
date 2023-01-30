package com.softdesign.plagueinc.models.gamestate;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softdesign.plagueinc.exceptions.ContinentFullException;
import com.softdesign.plagueinc.managers.futures.input_types.CountryChoice;
import com.softdesign.plagueinc.models.action_log.ActionLog;
import com.softdesign.plagueinc.models.action_log.CountryAction;
import com.softdesign.plagueinc.models.action_log.CountryChosenAction;
import com.softdesign.plagueinc.models.action_log.EvolveTraitAction;
import com.softdesign.plagueinc.models.action_log.InfectCountryAction;
import com.softdesign.plagueinc.models.action_log.KillCountryAction;
import com.softdesign.plagueinc.models.countries.Continent;
import com.softdesign.plagueinc.models.countries.Country;
import com.softdesign.plagueinc.models.events.Event;
import com.softdesign.plagueinc.models.gamestate.selection_objects.CitySelection;
import com.softdesign.plagueinc.models.plague.DiseaseType;
import com.softdesign.plagueinc.models.plague.Plague;
import com.softdesign.plagueinc.models.traits.TraitCard;
import com.softdesign.plagueinc.models.traits.TraitType;
import com.softdesign.plagueinc.models.traits.travel.AirborneTrait;
import com.softdesign.plagueinc.models.traits.travel.WaterborneTrait;
import com.softdesign.plagueinc.util.CountryReference;
import com.softdesign.plagueinc.util.EventReference;
import com.softdesign.plagueinc.util.TraitReference;

import lombok.Getter;
import lombok.Setter;


@JsonIgnoreProperties(value = {
    "countryDeck",
    "traitDeck",
    "eventDeck",
    "actions",
    "countryChoice",
    "infectChoice",
    "deathFuture"
})
@Getter
@Setter
public class GameState {

    Logger logger = LoggerFactory.getLogger(GameState.class);

    private List<Plague> plagues;
    
    private Plague currTurn;

    private PlayState playState;

    private Map<Continent, List<Country>> board;

    private ArrayDeque<Country> countryDeck;

    private List<Country> revealedCountries;

    private Set<Country> countryDiscard;

    private ArrayDeque<TraitCard> traitDeck;

    private Set<TraitCard> traitDiscard;

    private ArrayDeque<Event> eventDeck;

    private Set<Event> eventDiscard;

    private Map<Plague, Boolean> votesToStart;

    private boolean readyToProceed;

    private boolean suddenDeath;

    private Stack<ActionLog> actions;

    private Queue<Plague> turnOrder;

    private Optional<CompletableFuture<CountryChoice>> countryChoice;

    private Optional<CompletableFuture<Country>> infectChoice;

    private Optional<CompletableFuture<Integer>> deathFuture;

    //Event Futures

    private Optional<CompletableFuture<CitySelection>> citySelectionFuture;

    private Optional<CompletableFuture<Country>> countrySelectionFuture;

    private Optional<CompletableFuture<TraitCard>> selectTraitCard;

    private Optional<CompletableFuture<Continent>> selectContinent;

    private Optional<Plague> eventPlayer;

    private static final int MAX_PLAYERS = 4;

    private static final Map<Integer, Integer> COUNTRIES_BY_PLAYER = Map.of(2, 24, 3,27, 4, 32);

    private static final Map<Continent, Integer> MAX_COUNTRIES = Map.of(Continent.NORTH_AMERICA, 3, 
                                                                      Continent.SOUTH_AMERICA, 4, 
                                                                      Continent.EUROPE, 5, 
                                                                      Continent.ASIA, 5, 
                                                                      Continent.AFRICA, 5, 
                                                                      Continent.OCEANIA, 3);
    private static final int LUCKY_ESCAPE_POINTS = 4;

    private static final int CONTINENT_KILLER_POINTS = 6;

    private static final int ULTIMATE_WIPEOUT_POINTS = 7;

    public GameState(){
        this.plagues = new ArrayList<>();
        this.playState = PlayState.INITIALIZATION;
        this.board = new HashMap<>();
        Stream.of(Continent.values()).forEach(continent -> this.board.put(continent, new ArrayList<>()));
        this.votesToStart = new HashMap<>();
        this.actions = new Stack<>();
        this.readyToProceed = false;
        //TODO: Implement sudden death logic
        this.suddenDeath = false;
        
        this.infectChoice = Optional.empty();
        this.countryChoice = Optional.empty();
        this.deathFuture = Optional.empty();

        initTraitDeck();
        initEventDeck();
    }

    //INIT

    private void initTraitDeck(){
        List<TraitCard> defaultTraitDeck = TraitReference.getDefaultTraitDeck();
        Collections.shuffle(defaultTraitDeck);
        traitDeck = new ArrayDeque<>(defaultTraitDeck);
        traitDiscard = new HashSet<>();
    }

    private void initEventDeck(){
        List<Event> defaultEventDeck = EventReference.getDefaultEventDeck();
        Collections.shuffle(defaultEventDeck);
        eventDeck = new ArrayDeque<>(defaultEventDeck);
        eventDiscard = new HashSet<>();
    }

    private void initCountryDeck(List<Country> remainingCountries){
        if(countryDeck != null){
            logger.error("attempted to init the country deck, after its already been initialized");
        }
        List<Country> defaultCountryDeck = new ArrayList<>(CountryReference.getDefaultCountryDeck());
        defaultCountryDeck.addAll(remainingCountries);
        Collections.shuffle(defaultCountryDeck);
        defaultCountryDeck = defaultCountryDeck.subList(0, COUNTRIES_BY_PLAYER.get(plagues.size()));

        countryDeck = new ArrayDeque<>(defaultCountryDeck);
        revealedCountries = new ArrayList<>();
        countryDiscard = new HashSet<>();

        refillRevealedCountries();
    }

    public void startGame(UUID playerId){
        if(getPlayState() != PlayState.INITIALIZATION){
            logger.warn("(Plague {}) voted to start the game, but the game has already started");
            throw new IllegalStateException();
        }
        
        //Find the plague with this UUID
        Plague plague = getPlagues()
        .stream()
        .filter(pla -> pla.getPlayerId() == playerId)
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);

        if(getVotesToStart().get(plague)){
            logger.warn("(Plague {}) attempted to vote to start the game, but has already voted to start", plague.getPlayerId());
        }

        //mark this plague as voted to start
        getVotesToStart().put(plague, true);
        
        logger.info("(Plague {}) has voted to start the game");

        //If all players have voted to start, and there are more than 1 players in the lobby, then start the game
        if(getVotesToStart().values().stream().allMatch(bool -> bool) && getPlagues().size() > 1){
            logger.info("All players have voted to start the game, initializing game");
            
            //init starting country deck
            List<Country> startingCountries = new ArrayList<>(CountryReference.getStartingCountries());
            Collections.shuffle(startingCountries);

            //Init turn order
            List<Plague> players = getPlagues();
            Collections.shuffle(players);
            setTurnOrder(players);

            //Go through all the players, and initialize their play states (Starting country, starting DNA, starting Traits)
            IntStream.range(0, players.size()).forEach(index -> {
                Plague thisPlague = players.get(index);
                //Give player default points
                thisPlague.addDnaPoints(index);

                //Infect initial country
                Country startingCountry = startingCountries.remove(0);
                placeCountry(startingCountry);
                startingCountry.infectCountry(thisPlague);

                //draw initial traits
                drawTraitCards(5).forEach(card -> thisPlague.drawTraitCard(card));
                logger.info("(Plague {}) initialized", thisPlague.getPlayerId());
            });

            //Init the country deck in the game state
            initCountryDeck(startingCountries);
        
            setCurrTurn(getTurnOrder().poll());

            //Prep the gamestate to proceed
            setPlayState(PlayState.START_OF_TURN);
            setReadyToProceed(true);
        }

    }

    /*
     * Gameplay 
     */

    //Join Game
    public Plague joinGame(DiseaseType diseaseType){
        if(getPlayState() != PlayState.INITIALIZATION)
        {
            logger.warn("Player attempted to join game but game is already started");
            throw new IllegalStateException();
        }

        if(getPlagues().size() >= MAX_PLAYERS){
            logger.warn("Player attempted to join game but game is already full");
            throw new IllegalStateException();
        }

        //Create a new plague, and add it to the gameState
        Plague plague = new Plague(diseaseType);
        getPlagues().add(plague);
        getVotesToStart().put(plague, false);
        return plague;
    }

    public void proceedState(){
        if(!isReadyToProceed()){
            logger.warn("Player attempted to proceed the game state before it was ready to move on (Current state: {})", getPlayState());
            throw new IllegalStateException();
        }
    
        setReadyToProceed(false);
        switch(getPlayState()){
            case START_OF_TURN:
                //Score DNA Points & Mark as ready to proceed
                setPlayState(PlayState.DNA);
                scoreDNAPoints();
                break;
            case DNA:
                //Move on to choose country phase
                setPlayState(PlayState.CHOOSECOUNTRY);
                break;
            case CHOOSECOUNTRY:
                //init playCountry future so player can asynchronously choose which action to take, and then proceed to the play country state
                Country chosenCountry = getActions()
                .stream()
                .filter(action -> action.getClass().equals(CountryChosenAction.class))
                .map(action -> (CountryChosenAction)action)
                .findFirst()
                .get()
                .getCountry();
    
                initCountryChoiceFuture(chosenCountry);
                setPlayState(PlayState.PLAYCOUNTRY);
                break;
            case PLAYCOUNTRY:
                //Move on to evolve phase
                setPlayState(PlayState.EVOLVE);
                break;
            case EVOLVE:
                //Initialize the infect future, and then move onto the infect phase
                initInfectFuture(getCurrTurn().getTraitCount(TraitType.INFECTIVITY));
                setPlayState(PlayState.INFECT);
                break;
            case INFECT:
                //Init the death phase, by creating a recursive future that will go through all the killable countries, and then move on to the death state
                initDeathPhase();
                setPlayState(PlayState.DEATH);
                break;
            case DEATH:
                //Move on to the end of round state
                setPlayState(PlayState.END_OF_TURN);
                setReadyToProceed(true);
                break;
            case END_OF_TURN:
    
                if(checkForWin()){
                    endGame();
                    return;
                }
                //Reset player's abilities
                //getCurrTurn().refreshAbilities();
    
                //Shift turn to next player in line
                clearActionLog();
                shiftTurnOrder();
    
    
                setPlayState(PlayState.START_OF_TURN);
                setReadyToProceed(true);
                break;
            default:
                logger.error("Weird... this should never happen");
                break;
        }
    }

    //DNA PHASE

    public void scoreDNAPoints(){
        if(isReadyToProceed()){
            logger.warn("Attempted to score DNA points multiple times");
            throw new IllegalStateException();
        }
        //Get all countries controlled by the player
        List<Country> controllingCountries = getBoard()
        .values()
        .stream()
        .flatMap(list -> list.stream())
        .filter(country -> country.getControllers()
        .contains(getCurrTurn()))
        .toList();

        //Give DNA points to the player
        getCurrTurn().addDnaPoints(controllingCountries.size());
        setReadyToProceed(true);
    }

    //COUNTRY PHASE

    public Country drawCountryAction(){
        if(isReadyToProceed()){
            logger.warn("Attempted to draw a country card when the gamestate was ready to proceed");
            throw new IllegalStateException();
        }
        if(getPlayState() != PlayState.CHOOSECOUNTRY){
            logger.error("Attempting to draw a country, when the play state is {}", getPlayState());
            throw new IllegalStateException();
        }

        //draw country, and log the action
        Country drawnCountry = drawCountry();
        logger.info("Drew Country {}", drawnCountry.getCountryName());
        logAction(new CountryChosenAction(drawnCountry));
        setReadyToProceed(true);
        return drawnCountry;
    }

    public Country selectCountryFromRevealed(int index){
        if(isReadyToProceed()){
            logger.warn("Attempted to choose a revealed country card when the gamestate was ready to proceed");
            throw new IllegalStateException();
        }
        if(getPlayState() != PlayState.CHOOSECOUNTRY){
            logger.error("Attempting to choose a country, when the play state is {}", getPlayState());
            throw new IllegalStateException();
        }

        //choose country, and log the action
        Country chosenCountry = takeRevealedCountry(index);
        initCountryChoiceFuture(chosenCountry);
        logAction(new CountryChosenAction(chosenCountry));
        setReadyToProceed(true);
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
                    logger.info("Attempting to place country {}", drawnCountry.getCountryName());
                    //Try to place the country, if there's an issue, we have a problem
                    try{
                        placeCountry(drawnCountry);
                        setReadyToProceed(true);
                        countryChoice = Optional.empty();
                    }
                    catch(ContinentFullException e){
                        logger.warn("Continent {} is full, cannot place {} there", drawnCountry.getContinent(), drawnCountry.getCountryName());
                        initCountryChoiceFuture(drawnCountry);
                    }
                    break;
                    case DISCARD:
                        //Discard the country
                        discardCountry(drawnCountry);
                        setReadyToProceed(true);
                        countryChoice = Optional.empty();
                    break;
                }
            }
        });
    }

    public void makeCountryChoice(CountryChoice choice){
        if(isReadyToProceed()){
            logger.warn("Attempted to make a country choice, but the gamestate is ready to proceed");
            throw new IllegalStateException();
        }
        if(countryChoice.isEmpty()){
            logger.error("Attempted to make a country choice, but the gamestate is not waiting for a country choice");
            throw new IllegalStateException();
        }
        if(getPlayState() != PlayState.PLAYCOUNTRY){
            logger.error("Attempting to play a country, but the game state is {}", getPlayState());
            throw new IllegalStateException();
        }
        logger.info("Received country choice of {}", choice.toString());
        countryChoice.get().complete(choice);
    } 

    public void placeCountry(Country country){
        if(isReadyToProceed()){
            logger.warn("Cannot take an action if the gamestate is ready to proceed");
            throw new IllegalStateException("Already placed country");
        }
        if(getBoard().values().stream().flatMap(list -> list.stream()).anyMatch(placedCountry -> placedCountry.equals(country))){
            logger.error("Attempted to place a country {} that is already placed", country.getCountryName());
            throw new IllegalStateException("Country already placed");
        }

        if(getBoard().get(country.getContinent()).size() == MAX_COUNTRIES.get(country.getContinent())){
            logger.warn("Cannot place country as the continent {} is full", country.getContinent());
            throw new ContinentFullException();
        }

        getBoard().get(country.getContinent()).add(country);
        logAction(new CountryAction(CountryChoice.PLAY, country));
        setReadyToProceed(true);
    }

    public void discardCountryAction(Country country){
        if(isReadyToProceed()){
            logger.warn("Cannot take an action if the gamestate is ready to proceed");
            throw new IllegalStateException("Already placed country");
        }
        if(getPlayState() != PlayState.PLAYCOUNTRY){
            logger.error("Attempting to discard a country, but the game state is {}", getPlayState());
            throw new IllegalStateException();
        }

        discardCountry(country);

        getCurrTurn().clearHand().forEach(card -> discardTraitCard(card));

        drawTraitCards(5).forEach(card -> getCurrTurn().drawTraitCard(card));

        logAction(new CountryAction(CountryChoice.DISCARD, country));

        setReadyToProceed(true);
    }
    
    //EVOLVE PHASE

    public void evolveTrait(int traitSlot, int traitIndex){
        if(isReadyToProceed()){
            logger.warn("Cannot take an action if the gamestate is ready to proceed");
            throw new IllegalStateException("Already placed country");
        }
        if(getPlayState() != PlayState.EVOLVE){
            logger.error("Attempting to evolve a trait, but the game state is {}", getPlayState());
            throw new IllegalStateException();
        }
        try{
            //Try and evolve the card, and log it
            TraitCard card = getCurrTurn().evolveTrait(traitIndex, traitSlot);
            logAction(new EvolveTraitAction(card));
            setReadyToProceed(true);
        }
        catch(Exception e){
            logger.error(e.getMessage());
            throw e;
        }
    }

    public void skipEvolve(){
        if(getPlayState() != PlayState.EVOLVE){
            logger.error("Attempting to skip evolution, but the game state is {}", getPlayState());
            throw new IllegalStateException();
        }
        setReadyToProceed(true);
    }

    //INFECT PHASE

    private void initInfectFuture(int citiesToInfect){
        if(infectChoice.isPresent()){
            infectChoice.get().cancel(true);
        }
        if(unableToMove(getCurrTurn())){
            logger.info("(Plague {}) cannot place any tokens, skipping infect phase", getCurrTurn().getPlayerId());
            setReadyToProceed(true);
            return;
        }

        //This part can get complicated! Please reach out to Wyatt if you have any issues understanding
        infectChoice = Optional.of(new CompletableFuture<>());
        infectChoice.get().whenComplete((country, ex) -> {
            if(ex != null){
                logger.warn("Error with infection choice future EX: {}", ex.getMessage());
                initInfectFuture(citiesToInfect);
            }
            else{
                //Try and infect the chosen country, then if there are any more countries that the player can infect, create another future
                try{
                    infectCountry(country);
                    if(citiesToInfect > 1 && getCurrTurn().getPlagueTokens() > 0){
                        initInfectFuture(citiesToInfect - 1);
                    }
                    else{
                        setReadyToProceed(true);
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
        if(getPlayState() != PlayState.INFECT){
            logger.warn("Attempted to infect {}, but the game state is {}", countryName, getPlayState());
            throw new IllegalStateException();
        }
        if(isReadyToProceed()){
            logger.warn("Attempted to infect {}, but the game is ready to proceed", countryName);
            throw new IllegalStateException();
        }
        if(infectChoice.isEmpty()){
            logger.warn("Attempted to infect {}, but the future is not valid", countryName);
            throw new IllegalStateException();
        }

        Country country = getBoard()
        .values()
        .stream()
        .flatMap(continent -> continent.stream())
        .filter(thisCountry -> thisCountry.getCountryName().equals(countryName))
        .findFirst().orElseThrow(IllegalArgumentException::new);

        logger.info("Received request to infect country {}", country.getCountryName());
        infectChoice.get().complete(country);
    }

    private void infectCountry(Country country){
        if(isReadyToProceed()){
            logger.warn("Cannot take an action if the gamestate is ready to proceed");
            throw new IllegalStateException("Already placed country");
        }
        if(getPlayState() != PlayState.INFECT){
            logger.error("Attempting to infect a country, but the game state is {}", getPlayState());
            throw new IllegalStateException();
        }
        if(!canInfectCountry(country, getCurrTurn())){
            logger.warn("(Plague {}) attempted to infect {}, but is unable to", getCurrTurn().getPlayerId(), country.getCountryName());
            throw new IllegalStateException();
        }

        country.infectCountry(getCurrTurn());
        logAction(new InfectCountryAction(country));

    }

    //DEATH PHASE

    private void initDeathPhase(){
        //get all killable countries, then make a future from them
       List<Country> choppingBlock = getBoard()
        .values()
        .stream()
        .flatMap(continent -> continent.stream())
        .filter(country -> country.isFull())
        .filter(country -> country.getControllers().contains(getCurrTurn()))
        .toList();
        createDeathFuture(choppingBlock);
    }

    public int rollDeathDice(){
        if(isReadyToProceed()){
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
            logger.info("(Plague {}) has no countries to kill, skipping death phase", getCurrTurn().getPlayerId());
            setReadyToProceed(true);
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
                if(result <= getCurrTurn().getTraitCount(TraitType.LETHALITY)){
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
                    setReadyToProceed(true);
                }
            }
        });
    }

    private void killCountry(Country country, int roll){
        if(isReadyToProceed()){
            logger.warn("Cannot take an action if the gamestate is ready to proceed");
            throw new IllegalStateException("Already placed country");
        }
        if(getPlayState() != PlayState.DEATH){
            logger.error("Attempting to kill a country, but the game state is {}", getPlayState());
            throw new IllegalStateException();
        }

        //Give players points based on how many tokens they have in the country
        Map<Plague, Long> infectionCount = country.getInfectionByPlayer();
        infectionCount.keySet().forEach(plague -> plague.addDnaPoints(infectionCount.get(plague).intValue()));

        //Give players back their tokens
        infectionCount.entrySet().forEach(entry -> entry.getKey().returnPlagueTokens(entry.getValue().intValue()));

        //Discard the country that was killed, and mark it as killed by this player
        discardCountry(country);
        getCurrTurn().killCountry(country);
        //give everyone who was present an event card, and log the kill
        infectionCount.keySet().stream().filter(plague -> plague.getEventCards().size() < 3).forEach(plague -> plague.addEventCard(drawEventCard()));
        logAction(new KillCountryAction(country, roll, true));
    }

    private void failKillCountry(Country country, int roll){
        logAction(new KillCountryAction(country, roll, false));
    }

    //Trait Deck

    private List<TraitCard> drawTraitCards(int numCards){
        List<TraitCard> drawnCards = new ArrayList<>();
        for(int i = 0; i<numCards; i++){
            drawnCards.add(drawTraitCard());
        }
        return drawnCards;
    }

    private TraitCard drawTraitCard(){
        if(getTraitDeck().size() == 0){
            refillTraitDeck();
        }

        return getTraitDeck().pop();
    }

    private void discardTraitCard(TraitCard card){
        traitDiscard.add(card);
    }

    private void refillTraitDeck(){

        if(getTraitDeck().size() > 0){
            logger.warn("Attempted to refill the deck when there were cards present in it");
            throw new IllegalAccessError();
        }
        List<TraitCard> discard = getTraitDiscard().stream().toList();
        Collections.shuffle(discard);
        traitDeck = new ArrayDeque<>(discard);
        traitDiscard = new HashSet<>();
    }

    //Event Deck

    private Event drawEventCard(){
        if(eventDeck.size() == 0){
            refillEventDeck();
        }
        return eventDeck.pop();
    }

    private void discardEventCard(Event card){
        eventDiscard.add(card);
    }

    private void refillEventDeck(){
        if(eventDeck.size() > 0){
            logger.warn("Attempted to refill the deck when there were cards present in it");
            throw new IllegalAccessError();
        }
        List<Event> discard = eventDiscard.stream().toList();
        Collections.shuffle(discard);
        eventDeck = new ArrayDeque<>(discard);
        eventDiscard = new HashSet<>();
    }

    //Country Deck

    private Country drawCountry(){
        if(countryDeck.size() == 0){
            logger.error("Attempted to draw a card from the country deck, but it is empty");
            throw new IndexOutOfBoundsException();
        }
        return countryDeck.pop();
    }

    private void discardCountry(Country country){
        if(countryDiscard.contains(country)){
            logger.warn("Attempted to discard country {}, but this country has already been discarded", country.getCountryName());
            return;
        }
        countryDiscard.add(country);
    }

    private Country takeRevealedCountry(int index){
        if(index < 0 || index >= revealedCountries.size()){
            logger.error("attempted to take a card from an index that is out of bounds ({})", index);
            throw new IndexOutOfBoundsException();
        }
        Country takenCountry = revealedCountries.remove(index);
        refillRevealedCountries();
        if(revealedCountries.size() == 0){
            suddenDeath = true;
        }
        return takenCountry;
    }

    private void refillRevealedCountries(){
        while(revealedCountries.size() < 3 && countryDeck.size() > 0){
            revealedCountries.add(countryDeck.pop());
        }
    }

    //Action Logging

    public void logAction(ActionLog actionLog){
        actions.push(actionLog);
    }

    private void clearActionLog(){
        actions = new Stack<>();
    }

    //Turn Order

    private void setTurnOrder(List<Plague> order){
        turnOrder = new LinkedList<>(order);
    }

    private Plague shiftTurnOrder(){
        Plague oldTurn = turnOrder.poll();
        turnOrder.add(oldTurn);
        return turnOrder.peek();
    }

    //End of turn    

    private void endGame(){
        //TODO: Add logic for first to do X gets it
        
        //Give each plague points from their trait slots
        plagues.forEach(plague -> plague.getTraitSlots()
        .stream()
        .filter(slot -> slot.hasCard()).map(slot -> slot.getCard())
        .forEach(card -> plague.addDnaPoints(card.cost())));

        //Lucky escape bonus
        plagues.stream()
        .collect(Collectors.toMap(Function.identity(), Plague::getPlagueTokens))
        .entrySet()
        .stream()
        .filter(entry -> entry.getValue() == plagues.stream().map(Plague::getPlagueTokens).max(Comparator.comparing(tokens -> tokens)).get())
        .forEach(entry -> entry.getKey().addDnaPoints(LUCKY_ESCAPE_POINTS));

        //Continent Killer Bonus
        Map<Continent, Long> maxKillsPerContinent = Stream.of(Continent.values())
        .collect(Collectors
        .toMap(Function.identity(), continent -> plagues.stream()
            .map(Plague::getKilledCountries)
            .map(set -> set.stream().filter(country -> country.getContinent() == continent)).count())); 
        
        Stream.of(Continent.values())
        .forEach(continent -> plagues.stream()
            .filter(plague -> plague.getKilledCountries()
                .stream()
                .filter(country -> country.getContinent() == continent)
                .count() == maxKillsPerContinent.get(continent))
            .forEach(plague -> plague.addDnaPoints(CONTINENT_KILLER_POINTS)));

        //Ultimate Wipeout Bonus
        final int largestNumCities = plagues.stream()
        .map(plague -> plague.getKilledCountries())
        .flatMap(set -> set.stream())
        .max(Comparator.comparing(country -> country.getCities().size()))
        .get()
        .getCities()
        .size();

        plagues.stream()
        .filter(plague -> plague.getKilledCountries().stream().anyMatch(country -> country.getCities().size() == largestNumCities))
        .forEach(plague -> plague.addDnaPoints(ULTIMATE_WIPEOUT_POINTS));

        playState = PlayState.END_OF_GAME;
    }

    private List<Plague> getWinners(){
        if(playState != PlayState.END_OF_GAME){
            logger.warn("Attempted to get the game winner, but the game is still in progress");
            throw new IllegalAccessError();
        }
        int maxPoints = plagues.stream().mapToInt(plague -> plague.getDnaPoints()).max().getAsInt();
        return plagues.stream().filter(plague -> plague.getDnaPoints() == maxPoints).toList();
    }

    //Util

    private boolean isPlagueEradicated(Plague plague){
        return board.values()
        .stream()
        .flatMap(continent -> continent.stream())
        .flatMap(country -> country.getCities().values().stream())
        .filter(Optional::isPresent)
        .map(Optional::get)
        .anyMatch(thisPlague -> thisPlague.equals(plague));
    }

    private boolean unableToMove(Plague plague){
        return board.values()
        .stream()
        .flatMap(continent -> continent.stream())
        .noneMatch(country -> canInfectCountry(country, plague));
    }

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
        List<Country> infectedCountries = board.values()
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

    private boolean checkForWin(){

        return getPlagues().stream().anyMatch(plague -> isPlagueEradicated(plague) || unableToMove(plague));
    }

    //EVENT CARDS

    private void playEventCard(Event eventCard){
        //TODO: Implement this method
    }

    public void verifyTurn(UUID playerId){
        if(!getCurrTurn().getPlayerId().equals(playerId)){
            throw new IllegalAccessError();
        }
    }

}
    