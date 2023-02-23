package com.softdesign.plagueinc.models.gamestate;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Stack;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.softdesign.plagueinc.exceptions.ContinentFullException;
import com.softdesign.plagueinc.managers.futures.input_types.CountryChoice;
import com.softdesign.plagueinc.models.action_log.ActionLog;
import com.softdesign.plagueinc.models.action_log.CountryAction;
import com.softdesign.plagueinc.models.action_log.DrawEventCard;
import com.softdesign.plagueinc.models.action_log.EvolveTraitAction;
import com.softdesign.plagueinc.models.action_log.InfectCountryAction;
import com.softdesign.plagueinc.models.action_log.KillCountryAction;
import com.softdesign.plagueinc.models.countries.Continent;
import com.softdesign.plagueinc.models.countries.Country;
import com.softdesign.plagueinc.models.events.EventCard;
import com.softdesign.plagueinc.models.plague.DiseaseType;
import com.softdesign.plagueinc.models.plague.Plague;
import com.softdesign.plagueinc.models.plague.PlagueColor;
import com.softdesign.plagueinc.models.serializers.CountrySerializers.CountryNameSerializer;
import com.softdesign.plagueinc.models.serializers.PlagueSerializers.PlagueColorMapSerializer;
import com.softdesign.plagueinc.models.serializers.PlagueSerializers.PlagueListToColorSerializer;
import com.softdesign.plagueinc.models.serializers.PlagueSerializers.PlagueToColorSerializer;
import com.softdesign.plagueinc.models.traits.TraitCard;
import com.softdesign.plagueinc.models.traits.TraitType;
import com.softdesign.plagueinc.models.traits.travel.AirborneTrait;
import com.softdesign.plagueinc.models.traits.travel.WaterborneTrait;
import com.softdesign.plagueinc.util.CountryReference;
import com.softdesign.plagueinc.util.EventReference;
import com.softdesign.plagueinc.util.TraitReference;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameState {

    @JsonIgnore
    Logger logger = LoggerFactory.getLogger(GameState.class);

    //Overall state

    @JsonSerialize(using = PlagueColorMapSerializer.class)
    private List<Plague> plagues;
    
    @JsonSerialize(using = PlagueToColorSerializer.class)
    private Plague currTurn;

    private PlayState playState;

    private Map<Continent, List<Country>> board;
    
    @JsonIgnore
    private ArrayDeque<Country> countryDeck;

    @JsonSerialize(using = CountryNameSerializer.class)
    private List<Country> revealedCountries;

    @JsonSerialize(using = CountryNameSerializer.class)
    private List<Country> countryDiscard;

    @JsonIgnore
    private ArrayDeque<TraitCard> traitDeck;

    private List<TraitCard> traitDiscard;

    @JsonIgnore
    private ArrayDeque<EventCard> eventDeck;

    private List<EventCard> eventDiscard;

    private Map<PlagueColor, Boolean> votesToStart;

    private boolean readyToProceed;

    private boolean suddenDeath;

    @JsonIgnore
    private Stack<ActionLog> actions;

    @JsonSerialize(using = PlagueListToColorSerializer.class)
    private Queue<Plague> turnOrder;

    //Infect

    private int countriesToInfect;

    @JsonSerialize(using = CountryNameSerializer.class)
    private List<Country> choppingBlock;

    //Event Futures

    private Optional<Plague> eventPlayer;

    private Optional<ConditionalAction> action;

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

    private static final int RESPAWN_PENALTY = 7;


    public GameState(){
        this.plagues = new ArrayList<>();
        this.playState = PlayState.INITIALIZATION;
        this.board = new HashMap<>();
        Stream.of(Continent.values()).forEach(continent -> this.board.put(continent, new ArrayList<>()));
        this.votesToStart = new HashMap<>();
        this.actions = new Stack<>();
        this.readyToProceed = false;
        this.suddenDeath = false;
        
        this.countriesToInfect = 0;
        this.choppingBlock = List.of();
        this.eventPlayer = Optional.empty();
        this.action = Optional.empty();

        initTraitDeck();
        initEventDeck();
    }

    //INIT

    private void initTraitDeck(){
        List<TraitCard> defaultTraitDeck = TraitReference.getDefaultTraitDeck();
        Collections.shuffle(defaultTraitDeck);
        traitDeck = new ArrayDeque<>(defaultTraitDeck);
        traitDiscard = new ArrayList<>();
    }

    private void initEventDeck(){
        List<EventCard> defaultEventDeck = EventReference.getDefaultEventDeck();
        Collections.shuffle(defaultEventDeck);
        eventDeck = new ArrayDeque<>(defaultEventDeck);
        eventDiscard = new ArrayList<>();
    }

    private void initCountryDeck(List<Country> remainingCountries){
        if(countryDeck != null){
            logger.error("[INITIALIZATION] attempted to init the country deck, after its already been initialized");
        }
        List<Country> defaultCountryDeck = new ArrayList<>(CountryReference.getDefaultCountryDeck());
        defaultCountryDeck.addAll(remainingCountries);
        Collections.shuffle(defaultCountryDeck);
        defaultCountryDeck = defaultCountryDeck.subList(0, COUNTRIES_BY_PLAYER.get(plagues.size()));

        countryDeck = new ArrayDeque<>(defaultCountryDeck);
        revealedCountries = new ArrayList<>();
        countryDiscard = new ArrayList<>();

        refillRevealedCountries();
    }

    public void startGame(UUID playerId){
        validateState(PlayState.INITIALIZATION);
        
        //Find the plague with this UUID
        Plague plague = getPlague(playerId);

        if(votesToStart.get(plague.getColor())){
            logger.warn("[INITIALIZATION] (Plague {}) attempted to vote to start the game, but has already voted to start", plague.getPlayerId());
        }

        //mark this plague as voted to start
        votesToStart.put(plague.getColor(), true);
        
        logger.info("[INITIALIZATION] (Plague {}) has voted to start the game", playerId);

        //If all players have voted to start, and there are more than 1 players in the lobby, then start the game
        if(this.votesToStart.values().stream().allMatch(bool -> bool.booleanValue()) && this.plagues.size() > 1){
            logger.info("[INITIALIZATION] All players have voted to start the game, initializing game");
            
            //init starting country deck
            List<Country> startingCountries = new ArrayList<>(CountryReference.getStartingCountries());
            Collections.shuffle(startingCountries);

            //Init turn order
            Collections.shuffle(this.plagues);
            setTurnOrder(this.plagues);

            //Go through all the players, and initialize their play states (Starting country, starting DNA, starting Traits)
            IntStream.range(0, this.plagues.size()).forEach(index -> {
                Plague thisPlague = this.plagues.get(index);
                //Give player default points
                thisPlague.addDnaPoints(index);

                //Infect initial country
                Country startingCountry = startingCountries.remove(0);
                placeCountry(startingCountry);
                startingCountry.infectCountry(thisPlague);

                //draw initial traits
                drawTraitCards(5).forEach(card -> thisPlague.drawTraitCard(card));
                logger.info("[INITIALIZATION] (Plague {}) initialized", thisPlague.getPlayerId());
            });

            //Init the country deck in the game state
            initCountryDeck(startingCountries);
        
            this.currTurn = turnOrder.peek();

            //Prep the gamestate to proceed
            setPlayState(PlayState.START_OF_TURN);
            setReadyToProceed(true);
        }

    }

    /*
     * Gameplay 
     */

    //Join Game
    public UUID joinGame(PlagueColor plagueColor){
        validateState(PlayState.INITIALIZATION);

        if(this.plagues.size() >= MAX_PLAYERS){
            logger.warn("[INITIALIZATION]  Player attempted to join game but game is already full");
            throw new IllegalStateException();
        }

        if(this.plagues.stream().anyMatch(plague -> plague.getColor() == plagueColor)){
            logger.warn("[INITIALIZATION]  Player attempted to join game as {}, but player is already {}", plagueColor, plagueColor);
            throw new IllegalArgumentException();
        }

        //Create a new plague, and add it to the gameState
        Plague plague = new Plague(plagueColor);
        this.plagues.add(plague);
        this.votesToStart.put(plague.getColor(), false);
        logger.info("[INITIALIZATION] Plague with id ({}) created, and assigned color {}", plague.getPlayerId(), plague.getColor());
        return plague.getPlayerId();
    }

    // Change plague type in lobby
    public void changePlagueType(UUID playerId, DiseaseType diseaseType){
        validateState(PlayState.INITIALIZATION);

        this.getPlague(playerId).setDiseaseType(diseaseType);
    }

    public void proceedState(){
        if(!this.readyToProceed){
            logger.warn("Player attempted to proceed the game state before it was ready to move on (Current state: {})", this.playState);
            throw new IllegalStateException();
        }

        logger.info("Proceeding state from {}", this.playState);
    
        setReadyToProceed(false);
        switch(this.playState){
            case START_OF_TURN:
                //Score DNA Points & Mark as ready to proceed
                setPlayState(PlayState.DNA);
                scoreDNAPoints();
                break;
            case DNA:
                //Move on to choose country phase
                setPlayState(this.suddenDeath ? PlayState.EVOLVE : PlayState.COUNTRY);
                break;
            case COUNTRY:
                //Move on to evolve phase
                setPlayState(PlayState.EVOLVE);
                break;
            case EVOLVE:
                //Initialize the infect future, and then move onto the infect phase
                this.countriesToInfect = this.currTurn.getTraitCount(TraitType.INFECTIVITY);
                setPlayState(PlayState.INFECT);
                if(unableToMove(this.currTurn)){
                    setReadyToProceed(true);
                }
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
    
                if(this.suddenDeath && checkForWin()){
                    endGame();
                    return;
                }
                //Reset player's abilities
                //getCurrTurn().refreshAbilities();
    
                //Shift turn to next player in line
                clearActionLog();
                PlagueColor currColor = currTurn.getColor();
                this.currTurn = shiftTurnOrder();

                logger.info("Shifting player turns ({}) -> ({})", currColor, currTurn.getColor());
                
                setPlayState(PlayState.START_OF_TURN);
                checkIfRespawnNeeded();
                break;
            default:
                logger.error("Weird... this should never happen");
                break;
        }
    }

    //START_OF_TURN PHASE

    private void checkIfRespawnNeeded(){
        if(isPlagueEradicated(this.currTurn)){
            logger.info("[DNA] Plague ({}) has been eradicated, respawning", currTurn.getColor());
            respawnPlague(this.currTurn);
        }
        else{
            setReadyToProceed(true);
        }
    }

    private void respawnPlague(Plague plague){
        Country respawnCountry = drawCountry();
        if(this.board.get(respawnCountry.getContinent()).size() == MAX_COUNTRIES.get(respawnCountry.getContinent())){
            discardCountry(respawnCountry);
        }
        else{
            placeCountry(respawnCountry);
        }
        //TODO: Respawn ConditionalAction
        plague.spendDnaPoints(RESPAWN_PENALTY);

    }

    //DNA PHASE

    public void scoreDNAPoints(){
        if(this.readyToProceed){
            logger.warn("[DNA] Attempted to score DNA points multiple times");
            throw new IllegalStateException();
        }
        //Get all countries controlled by the player
        List<Country> controllingCountries = this.board
        .values()
        .stream()
        .flatMap(list -> list.stream())
        .filter(country -> country.getControllers()
        .contains(getCurrTurn()))
        .toList();

        //Give DNA points to the player
        this.currTurn.addDnaPoints(controllingCountries.size());
        logger.info("[DNA] Plague ({}) scored {} points", currTurn.getColor(), controllingCountries.size());
        setReadyToProceed(true);
    }

    //COUNTRY PHASE

    public void makeCountryChoice(String countryName, CountryChoice action){
        //Act on which response the player wishes to take

        validateState(PlayState.COUNTRY);

        logger.info("In make country choice: " + countryName + " " + action);
        Country drawnCountry;

        if(countryName.equals("countrycardback")){
            drawnCountry = drawCountry();
        }
        else{
            drawnCountry = takeRevealedCountry(countryName);
        }
        

        
        switch(action){
            case PLAY:
            logger.info("[COUNTRY] Attempting to place country {}", drawnCountry.getCountryName());
            //Try to place the country, if there's an issue, we have a problem
            try{
                placeCountry(drawnCountry);
                setReadyToProceed(true);
            }
            catch(ContinentFullException e){
                logger.warn("[COUNTRY] Continent {} is full, cannot place {} there", drawnCountry.getContinent(), drawnCountry.getCountryName());
                if(countryName.equals("countrycardback")){
                    countryDeck.add(drawnCountry);
                }
            }
            break;
            case DISCARD:
                //Discard the country
                discardCountryAction(drawnCountry);
                setReadyToProceed(true);
            break;
        }
    }

    public void placeCountry(Country country){
        if(this.readyToProceed && playState != PlayState.INITIALIZATION){
            logger.warn("[COUNTRYCHOICE] Cannot take an action if the gamestate is ready to proceed");
            throw new IllegalStateException("Already placed country");
        }
        if(this.board.values().stream().flatMap(list -> list.stream()).anyMatch(placedCountry -> placedCountry.equals(country))){
            logger.error("[COUNTRYCHOICE] Attempted to place a country {} that is already placed", country.getCountryName());
            throw new IllegalStateException("Country already placed");
        }

        if(this.board.get(country.getContinent()).size() == MAX_COUNTRIES.get(country.getContinent())){
            logger.warn("[COUNTRYCHOICE] Cannot place country as the continent {} is full", country.getContinent());
            throw new ContinentFullException();
        }

        this.board.get(country.getContinent()).add(country);
        logAction(new CountryAction(CountryChoice.PLAY, country));
        logger.info("[COUNTRYCHOICE] placed {} in {}", country.getCountryName(), country.getContinent());
    }

    public void discardCountryAction(Country country){
        if(this.readyToProceed){
            logger.warn("[COUNTRYCHOICE] Cannot take an action if the gamestate is ready to proceed");
            throw new IllegalStateException("Already placed country");
        }
        validateState(PlayState.COUNTRY);

        discardCountry(country);

        this.currTurn.clearHand().forEach(card -> discardTraitCard(card));

        drawTraitCards(5).forEach(card -> this.currTurn.drawTraitCard(card));

        logAction(new CountryAction(CountryChoice.DISCARD, country));

        logger.info("[COUNTRYCHOICE] {} discarded {}, and drew a new hand", this.currTurn.getColor(), country.getCountryName());

        setReadyToProceed(true);
    }
    
    //EVOLVE PHASE

    public void evolveTrait(int traitSlot, int traitIndex){
        if(this.readyToProceed){
            logger.warn("[EVOLVE] Cannot take an action if the gamestate is ready to proceed");
            throw new IllegalStateException("Already placed country");
        }
        validateState(PlayState.EVOLVE);
        try{
            //Try and evolve the card, and log it
            TraitCard card = this.currTurn.evolveTrait(traitIndex, traitSlot);
            logAction(new EvolveTraitAction(card));
            logger.info("[EVOLVE] Player {} evolved {} into slot {}", this.currTurn.getColor(), card.name(), traitSlot);
            setReadyToProceed(true);
        }
        catch(Exception e){
            logger.error(e.getMessage());
            throw e;
        }
    }

    public void skipEvolve(){
        validateState(PlayState.EVOLVE);
        logger.info("[EVOLVE] Player {} skipped evolve phase", this.currTurn.getColor());
        setReadyToProceed(true);
    }

    //INFECT PHASE

    public void attemptInfect(String countryName){
        if(this.readyToProceed){
            logger.warn("[INFECT] Attempted to infect {}, but the game is ready to proceed", countryName);
            throw new IllegalStateException();
        }

        validateState(PlayState.INFECT);

        if(this.countriesToInfect <= 0){
            logger.warn("[INFECT] Attempted to infect {}, but the player has 0 infections left", countryName);
            throw new IllegalStateException();
        }

        Country country = getCountry(countryName);

        logger.info("[INFECT] Received request to infect country {}", country.getCountryName());
        infectCountry(country);
        this.countriesToInfect--;
        
        setReadyToProceed(this.countriesToInfect == 0 || unableToMove(this.currTurn));
    }

    private void infectCountry(Country country){
        if(this.readyToProceed){
            logger.warn("[INFECT] Cannot take an action if the gamestate is ready to proceed");
            throw new IllegalStateException("Already placed country");
        }
        validateState(PlayState.INFECT);
        if(!canInfectCountry(country, this.currTurn)){
            logger.warn("[INFECT] (Plague {}) attempted to infect {}, but is unable to", this.currTurn.getPlayerId(), country.getCountryName());
            throw new IllegalStateException();
        }

        country.infectCountry(this.currTurn);
        logger.info("[INFECT] Player {} successfully infected country {}", this.currTurn.getColor(), country.getCountryName());
        logAction(new InfectCountryAction(country));

    }

    //DEATH PHASE

    private void initDeathPhase(){
        //get all killable countries, then make a future from them
       List<Country> choppingBlock = this.board
        .values()
        .stream()
        .flatMap(continent -> continent.stream())
        .filter(country -> country.isFull())
        .filter(country -> country.getControllers().contains(this.currTurn))
        .toList();
        logger.info("[DEATH] Player {} will now attempt to kill countries {}", this.currTurn.getColor(), choppingBlock);
        this.choppingBlock = new ArrayList<>(choppingBlock);
        if(this.choppingBlock.isEmpty()){
            setReadyToProceed(true);
        }
    }

    public int rollDeathDice(String countryName){
        if(this.readyToProceed){
            logger.error("[DEATH] Attempted to roll the death dice when the game state is ready to proceed");
            throw new IllegalStateException();
        }
        if(this.choppingBlock.isEmpty()){
            logger.error("[DEATH] Attempted to roll death dice, but there are no countries to kill!");
            throw new IllegalStateException("Death state issue");
        }
        validateState(PlayState.DEATH);
        Country country = getCountry(countryName);
        int randomNum = ThreadLocalRandom.current().nextInt(1, 7);
        logger.info("[DEATH] Plague {} rolled a {}!", this.currTurn.getColor(), randomNum);
        rollDeathDice(randomNum, country);
        choppingBlock.remove(country);
        setReadyToProceed(choppingBlock.isEmpty());
        return randomNum;
    }

    private void rollDeathDice(int result, Country country){
        if(result <= this.currTurn.getTraitCount(TraitType.LETHALITY)){
            killCountry(country, result);
        }
        else{
            failKillCountry(country, result);
        }
        
    }

    private void killCountry(Country country, int roll){
        if(this.readyToProceed){
            logger.warn("[DEATH] Cannot take an action if the gamestate is ready to proceed");
            throw new IllegalStateException("Already placed country");
        }
        validateState(PlayState.DEATH);

        //Give players points based on how many tokens they have in the country
        Map<Plague, Long> infectionCount = country.getInfectionByPlayer();
        infectionCount.keySet().forEach(plague -> plague.addDnaPoints(infectionCount.get(plague).intValue()));

        //Give players back their tokens
        infectionCount.entrySet().forEach(entry -> entry.getKey().returnPlagueTokens(entry.getValue().intValue()));

        //Discard the country that was killed, and mark it as killed by this player
        discardCountry(country);
        this.currTurn.killCountry(country);
        board.get(country.getContinent()).remove(country);
        //give everyone who was present an event card, and log the kill
        infectionCount.keySet().stream().filter(plague -> plague.getEventCards().size() < 3).forEach(plague -> {
            EventCard drawnCard = drawEventCard();
            plague.addEventCard(drawnCard);
            this.logAction(new DrawEventCard(this.playState, plague, drawnCard));
        });
        logger.info("[DEATH] Player {} successfully killed {}", this.currTurn.getColor(), country.getCountryName());
        logAction(new KillCountryAction(country, roll, true));
    }

    private void failKillCountry(Country country, int roll){
        logger.info("[DEATH] Player {} failed to kill {}", this.currTurn.getColor(), country.getCountryName());
        logAction(new KillCountryAction(country, roll, false));
    }

    //Trait Deck

    public List<TraitCard> drawTraitCards(int numCards){
        List<TraitCard> drawnCards = new ArrayList<>();
        for(int i = 0; i<numCards; i++){
            drawnCards.add(drawTraitCard());
        }
        return drawnCards;
    }

    public TraitCard drawTraitCard(){
        if(this.traitDeck.size() == 0){
            refillTraitDeck();
        }

        return this.traitDeck.pop();
    }

    private void discardTraitCard(TraitCard card){
        this.traitDiscard.add(card);
    }

    private void refillTraitDeck(){

        if(this.traitDeck.size() > 0){
            logger.warn("Attempted to refill the deck when there were cards present in it");
            throw new IllegalAccessError();
        }
        Collections.shuffle(this.traitDiscard);
        traitDeck = new ArrayDeque<>(this.traitDiscard);
        traitDiscard = new ArrayList<>();
        logger.info("Refilled trait deck");
    }

    //Event Deck

    private EventCard drawEventCard(){
        if(this.eventDeck.size() == 0){
            refillEventDeck();
        }
        return this.eventDeck.pop();
    }

    public void discardEventCard(EventCard card){
        this.eventDiscard.add(card);
    }

    private void refillEventDeck(){
        if(this.eventDeck.size() > 0){
            logger.warn("Attempted to refill the deck when there were cards present in it");
            throw new IllegalAccessError();
        }
        Collections.shuffle(this.eventDiscard);
        eventDeck = new ArrayDeque<>(this.eventDiscard);
        eventDiscard = new ArrayList<>();
        logger.info("Refilled Event Deck");
    }

    //Country Deck

    private Country drawCountry(){
        if(this.countryDeck.size() == 0){
            logger.error("Attempted to draw a card from the country deck, but it is empty");
            throw new IndexOutOfBoundsException();
        }
        return this.countryDeck.pop();
    }

    private void discardCountry(Country country){
        if(this.countryDiscard.contains(country)){
            logger.warn("Attempted to discard country {}, but this country has already been discarded", country.getCountryName());
            return;
        }
        this.countryDiscard.add(country);
    }

    private Country takeRevealedCountry(String countryName){
        
        Country takenCountry = this.revealedCountries.stream()
        .collect(Collectors.toMap(country -> country.getCountryName(), Function.identity()))
        .get(countryName);

        this.revealedCountries.remove(takenCountry);

        refillRevealedCountries();
        if(this.revealedCountries.size() == 0){
            suddenDeath = true;
        }
        return takenCountry;
    }

    private void refillRevealedCountries(){
        while(this.revealedCountries.size() < 3 && this.countryDeck.size() > 0){
            Country revealed = this.countryDeck.pop();
            this.revealedCountries.add(revealed);
            logger.info("Revealed {}", revealed.getCountryName());
        }
    }

    //Action Logging

    public void logAction(ActionLog actionLog){
        this.actions.push(actionLog);
    }

    private void clearActionLog(){
        this.actions = new Stack<>();
    }

    //Turn Order

    private void setTurnOrder(List<Plague> order){
        this.turnOrder = new LinkedList<>(order);
    }

    private Plague shiftTurnOrder(){
        Plague oldTurn = this.turnOrder.poll();
        this.turnOrder.add(oldTurn);
        return this.turnOrder.peek();
    }

    //End of game 

    private void endGame(){
        //TODO: Add logic for first to do X gets it
        
        //Give each plague points from their trait slots
        this.plagues.forEach(plague -> plague.getTraitSlots()
        .stream()
        .filter(slot -> slot.hasCard()).map(slot -> slot.getCard())
        .forEach(card -> plague.addDnaPoints(card.cost())));

        //Lucky escape bonus
        luckyEscape();

        //Continent Killer Bonus
        continentKiller();

        //Ultimate Wipeout Bonus
        ultimateWipeout();

        playState = PlayState.END_OF_GAME;
    }

    private void luckyEscape(){
        this.plagues.stream()
        .collect(Collectors.toMap(Function.identity(), Plague::getPlagueTokens))
        .entrySet()
        .stream()
        .filter(entry -> entry.getValue() == this.plagues.stream().map(Plague::getPlagueTokens).max(Comparator.comparing(tokens -> tokens)).get())
        .forEach(entry -> entry.getKey().addDnaPoints(LUCKY_ESCAPE_POINTS));
    }

    private void continentKiller(){
        Map<Continent, Long> maxKillsPerContinent = Stream.of(Continent.values())
        .collect(Collectors
        .toMap(Function.identity(), continent -> this.plagues.stream()
            .map(Plague::getKilledCountries)
            .map(set -> set.stream().filter(country -> country.getContinent() == continent)).count())); 
        
        Stream.of(Continent.values())
        .forEach(continent -> this.plagues.stream()
            .filter(plague -> plague.getKilledCountries()
                .stream()
                .filter(country -> country.getContinent() == continent)
                .count() == maxKillsPerContinent.get(continent))
            .forEach(plague -> plague.addDnaPoints(CONTINENT_KILLER_POINTS)));
    }

    private void ultimateWipeout(){
        final int largestNumCities = this.plagues.stream()
        .map(plague -> plague.getKilledCountries())
        .flatMap(set -> set.stream())
        .max(Comparator.comparing(country -> country.getCities().size()))
        .get()
        .getCities()
        .size();

        this.plagues
        .stream()
        .filter(plague -> plague.getKilledCountries().stream().anyMatch(country -> country.getCities().size() == largestNumCities))
        .forEach(plague -> plague.addDnaPoints(ULTIMATE_WIPEOUT_POINTS));
    }

    @JsonIgnore
    public List<Plague> getWinners(){
        validateState(PlayState.END_OF_GAME);
        int maxPoints = this.plagues.stream().mapToInt(plague -> plague.getDnaPoints()).max().getAsInt();
        return this.plagues.stream().filter(plague -> plague.getDnaPoints() == maxPoints).toList();
    }
    

    //Util

    @JsonIgnore
    public boolean isPlagueEradicated(Plague plague){
        return this.board.values()
        .stream()
        .flatMap(continent -> continent.stream())
        .flatMap(country -> country.getCities().stream())
        .filter(Optional::isPresent)
        .map(Optional::get)
        .noneMatch(thisPlague -> thisPlague.equals(plague));
    }

    public boolean unableToMove(Plague plague){
        return this.board.values()
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
    public boolean canInfectCountry(Country country, Plague plague){

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
        List<Country> infectedCountries = this.board.values()
        .stream()
        .flatMap(continent -> continent.stream())
        .filter(thisCountry -> thisCountry.getCities().stream().filter(Optional::isPresent).map(Optional::get).anyMatch(thisPlague -> thisPlague.equals(plague)))
        .toList();

        logger.info("Found countries player {} infects: {}", plague.getColor(), infectedCountries.stream().map(Country::getCountryName).toList());

        //Check what continent the player is present in
        List<Continent> continentPresence = Stream.of(Continent.values())
        .filter(continent -> infectedCountries.stream()
        .anyMatch(thisCountry -> thisCountry.getContinent().equals(continent)))
        .toList();

        //If the player infects a country in the same continent, then they can infect this country
        if(continentPresence.contains(country.getContinent())){
            logger.info("Player {} is present in the same continent as {}, so they can infect it", plague.getColor(), country.getCountryName());
            return true;
        }

        //If the player infects a country with an airport, and this country has an airport
        boolean airportConnected = infectedCountries.stream().anyMatch(thisCountry -> thisCountry.getTravelTypes().contains(new AirborneTrait()))
        && country.getTravelTypes().contains(new AirborneTrait())
        && plague.getTraits().contains(TraitType.AIRBORNE);
        logger.info("Player {} traits: {}", plague.getColor(), plague.getTraits());

        //If the player infects a country with a seaport, and this country has a seaport
        boolean waterportConnected = infectedCountries.stream().anyMatch(thisCountry -> thisCountry.getTravelTypes().contains(new WaterborneTrait())) 
        && country.getTravelTypes().contains(new WaterborneTrait())
        && plague.getTraits().contains(TraitType.WATERBORNE);

        return airportConnected || waterportConnected;
    }

    private boolean checkForWin(){

        return this.plagues.stream().anyMatch(plague -> isPlagueEradicated(plague) || unableToMove(plague));
    }

    private void validateState(PlayState state){
        if(state != this.playState){
            logger.warn("Attempted to verify game state, but discrepency found {} != {}", state, this.playState);
            throw new IllegalStateException("State Invalid");
        }
        if(this.eventPlayer.isPresent()){
            logger.warn("Attempted to validate state, but there is an event player present ({})", this.eventPlayer.get().getColor());
            throw new IllegalStateException("Event Player Present");
        }
    }

    @JsonIgnore
    public Plague getPlague(UUID playerId){
        return this.plagues
        .stream()
        .filter(pla -> pla.getPlayerId().equals(playerId))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Couldn't find player with this ID"));
    }

    @JsonIgnore
    public Country getCountry(String countryName){
        return this.board.values()
        .stream()
        .flatMap(continent -> continent.stream())
        .filter(country -> country.getCountryName().equals(countryName))
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);
    }

    public void verifyTurn(UUID playerId){
        if(!this.currTurn.getPlayerId().equals(playerId)){
            throw new IllegalAccessError();
        }
    }

    //EVENT CARDS

    public void playEventCard(int eventCardIndex, UUID playerId){
        List<DrawEventCard> drawnThisTurn = actions.stream()
        .filter(action -> action instanceof DrawEventCard)
        .map(action -> (DrawEventCard)action)
        .filter(action -> action.getPlague().getPlayerId().equals(playerId))
        .toList();

        Plague plague = getPlague(playerId);
        EventCard eventCard = plague.getEventCards().get(eventCardIndex);
        if(drawnThisTurn.stream().anyMatch(action -> action.getCard().equals(eventCard))){
            logger.warn("(Plague {}) attempted to play the event card ({}), but they drew it this turn", plague.getColor(), eventCard.getName());
            throw new IllegalAccessError();
        }
        //TODO: Implement event logic
    }

}
    