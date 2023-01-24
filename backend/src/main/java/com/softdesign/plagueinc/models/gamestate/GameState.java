package com.softdesign.plagueinc.models.gamestate;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softdesign.plagueinc.models.countries.Continent;
import com.softdesign.plagueinc.models.countries.Country;
import com.softdesign.plagueinc.models.events.Event;
import com.softdesign.plagueinc.models.plague.Plague;
import com.softdesign.plagueinc.models.traits.TraitCard;


@JsonIgnoreProperties(value = {
    "countryDeck",
    "traitDeck",
    "eventDeck"
})
public class GameState {

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
        this.readyToProceed = false;
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

    public void setCountryDeck(ArrayDeque<Country> countryDeck) {
        this.countryDeck = countryDeck;
    }

    public List<Country> getRevealedCountries() {
        return this.revealedCountries;
    }

    public void setRevealedCountries(List<Country> revealedCountries) {
        this.revealedCountries = revealedCountries;
    }

    public Set<Country> getCountryDiscard() {
        return this.countryDiscard;
    }

    public void setCountryDiscard(Set<Country> countryDiscard) {
        this.countryDiscard = countryDiscard;
    }

    public ArrayDeque<TraitCard> getTraitDeck() {
        return this.traitDeck;
    }

    public void setTraitDeck(ArrayDeque<TraitCard> traitDeck) {
        this.traitDeck = traitDeck;
    }

    public Set<TraitCard> getTraitDiscard() {
        return this.traitDiscard;
    }

    public void setTraitDiscard(Set<TraitCard> traitDiscard) {
        this.traitDiscard = traitDiscard;
    }

    public ArrayDeque<Event> getEventDeck() {
        return this.eventDeck;
    }

    public void setEventDeck(ArrayDeque<Event> eventDeck) {
        this.eventDeck = eventDeck;
    }

    public Set<Event> getEventDiscard() {
        return this.eventDiscard;
    }

    public void setEventDiscard(Set<Event> eventDiscard) {
        this.eventDiscard = eventDiscard;
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

}
    