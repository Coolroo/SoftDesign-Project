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
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.comparator.Comparators;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softdesign.plagueinc.models.action_log.ActionLog;
import com.softdesign.plagueinc.models.countries.Continent;
import com.softdesign.plagueinc.models.countries.Country;
import com.softdesign.plagueinc.models.events.Event;
import com.softdesign.plagueinc.models.plague.Plague;
import com.softdesign.plagueinc.models.traits.TraitCard;
import com.softdesign.plagueinc.models.traits.travel.AirborneTrait;
import com.softdesign.plagueinc.models.traits.travel.WaterborneTrait;
import com.softdesign.plagueinc.util.CountryReference;
import com.softdesign.plagueinc.util.EventReference;
import com.softdesign.plagueinc.util.TraitReference;


@JsonIgnoreProperties(value = {
    "countryDeck",
    "traitDeck",
    "eventDeck"
})
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

    public static final int MAX_PLAYERS = 4;

    public static final Map<Integer, Integer> COUNTRIES_BY_PLAYER = Map.of(2, 24, 3,27, 4, 32);

    public static final Map<Continent, Integer> MAX_COUNTRIES = Map.of(Continent.NORTH_AMERICA, 3, 
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

        initTraitDeck();
        initEventDeck();
    }


    public List<Plague> getPlagues() {
        return this.plagues;
    }

    public Plague getCurrTurn() {
        return this.currTurn;
    }

    public void setCurrTurn(Plague currTurn) {
        this.currTurn = currTurn;
    }

    public PlayState getPlayState() {
        return this.playState;
    }

    public void setPlayState(PlayState playState) {
        this.playState = playState;
    }

    public Map<Continent,List<Country>> getBoard() {
        return this.board;
    }

    public ArrayDeque<Country> getCountryDeck() {
        return this.countryDeck;
    }

    public List<Country> getRevealedCountries() {
        return this.revealedCountries;
    }

    public Set<Country> getCountryDiscard() {
        return this.countryDiscard;
    }

    public ArrayDeque<TraitCard> getTraitDeck() {
        return this.traitDeck;
    }

    public Set<TraitCard> getTraitDiscard() {
        return this.traitDiscard;
    }

    public ArrayDeque<Event> getEventDeck() {
        return this.eventDeck;
    }

    public Set<Event> getEventDiscard() {
        return this.eventDiscard;
    }

    public boolean getReadyToProceed(){
        return readyToProceed;
    }

    public void setReadyToProceed(boolean readyToProceed){
        this.readyToProceed = readyToProceed;
    }

    public Map<Plague, Boolean> getVotesToStart(){
        return votesToStart;
    }

    public boolean getSuddenDeath(){
        return suddenDeath;
    }

    public Stack<ActionLog> getActions(){
        return actions;
    }

    public void logAction(ActionLog actionLog){
        actions.push(actionLog);
    }

    public Queue<Plague> getTurnOrder(){
        return turnOrder;
    }

    public void setTurnOrder(List<Plague> order){
        turnOrder = new LinkedList<>(order);
    }

    public Plague shiftTurnOrder(){
        Plague oldTurn = turnOrder.poll();
        turnOrder.add(oldTurn);
        return turnOrder.peek();
    }

    public List<TraitCard> drawTraitCards(int numCards){
        List<TraitCard> drawnCards = new ArrayList<>();
        for(int i = 0; i<numCards; i++){
            drawnCards.add(drawTraitCard());
        }
        return drawnCards;
    }

    public TraitCard drawTraitCard(){
        if(getTraitDeck().size() == 0){
            refillTraitDeck();
        }

        return getTraitDeck().pop();
    }

    public void discardTraitCard(TraitCard card){
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

    public Event drawEventCard(){
        if(eventDeck.size() == 0){
            refillEventDeck();
        }
        return eventDeck.pop();
    }

    public void discardEventCard(Event card){
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

    public void initCountryDeck(List<Country> remainingCountries){
        if(countryDeck != null){
            logger.error("attempted to init the country deck, after its already been initialized");
        }
        List<Country> defaultCountryDeck = new ArrayList<>(CountryReference.getDefaultCountryDeck());
        defaultCountryDeck.addAll(remainingCountries);
        Collections.shuffle(defaultCountryDeck);
        defaultCountryDeck = defaultCountryDeck.subList(0, GameState.COUNTRIES_BY_PLAYER.get(plagues.size()));

        countryDeck = new ArrayDeque<>(defaultCountryDeck);
        revealedCountries = new ArrayList<>();
        countryDiscard = new HashSet<>();

        refillRevealedCountries();
    }

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

    public Country drawCountry(){
        if(countryDeck.size() == 0){
            logger.error("Attempted to draw a card from the country deck, but it is empty");
            throw new IndexOutOfBoundsException();
        }
        return countryDeck.pop();
    }

    public void discardCountry(Country country){
        if(countryDiscard.contains(country)){
            logger.warn("Attempted to discard country {}, but this country has already been discarded", country.getCountryName());
            return;
        }
        countryDiscard.add(country);
    }

    public Country takeRevealedCountry(int index){
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

    public void clearActionLog(){
        actions = new Stack<>();
    }

    public void endGame(){
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

    public List<Plague> getWinners(){
        if(playState != PlayState.END_OF_GAME){
            logger.warn("Attempted to get the game winner, but the game is still in progress");
            throw new IllegalAccessError();
        }
        int maxPoints = plagues.stream().mapToInt(plague -> plague.getDnaPoints()).max().getAsInt();
        return plagues.stream().filter(plague -> plague.getDnaPoints() == maxPoints).toList();
    }

    public boolean isPlagueEradicated(Plague plague){
        return board.values()
        .stream()
        .flatMap(continent -> continent.stream())
        .flatMap(country -> country.getCities().values().stream())
        .filter(Optional::isPresent)
        .map(Optional::get)
        .anyMatch(thisPlague -> thisPlague.equals(plague));
    }

    public boolean unableToMove(Plague plague){
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

}
    