package com.softdesign.plagueinc.models.gamestate;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softdesign.plagueinc.models.action_log.ActionLog;
import com.softdesign.plagueinc.models.countries.Continent;
import com.softdesign.plagueinc.models.countries.Country;
import com.softdesign.plagueinc.models.events.Event;
import com.softdesign.plagueinc.models.plague.Plague;
import com.softdesign.plagueinc.models.traits.TraitCard;
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

    public static final int MAX_PLAYERS = 4;

    public static final Map<Continent, Integer> maxCountries = Map.of(Continent.NORTH_AMERICA, 3, 
                                                                      Continent.SOUTH_AMERICA, 4, 
                                                                      Continent.EUROPE, 5, 
                                                                      Continent.ASIA, 5, 
                                                                      Continent.AFRICA, 5, 
                                                                      Continent.OCEANIA, 3);

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
            throw new IllegalStateException();
        }
        List<Country> defaultCountryDeck = new ArrayList<>(CountryReference.getDefaultCountryDeck());
        defaultCountryDeck.addAll(remainingCountries);
        Collections.shuffle(defaultCountryDeck);
        defaultCountryDeck = defaultCountryDeck.subList(0, CountryReference.getCountriesByPlayer().get(plagues.size()));

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

}
    