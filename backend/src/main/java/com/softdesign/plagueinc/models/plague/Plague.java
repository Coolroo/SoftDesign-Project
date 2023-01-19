package com.softdesign.plagueinc.models.plague;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.softdesign.plagueinc.models.countries.Continent;
import com.softdesign.plagueinc.models.countries.Country;
import com.softdesign.plagueinc.models.events.Event;
import com.softdesign.plagueinc.models.plague.trait_slot.TraitSlot;
import com.softdesign.plagueinc.models.traits.TraitCard;
import com.softdesign.plagueinc.models.traits.TraitType;

public class Plague {

    Logger logger = LoggerFactory.getLogger(Plague.class);
    
    private int playerId;

    private int dnaPoints;

    private int plagueTokens;

    private List<TraitSlot> traitSlots;

    private List<TraitType> traits;

    private List<TraitCard> hand;

    private List<Event> eventCards;

    private Set<Country> killedCountries;

    private static final int INITIAL_PLAGUE_TOKENS = 16;

    private static final List<TraitType> DEFAULT_TRAITS = List.of(TraitType.INFECTIVITY, TraitType.INFECTIVITY, TraitType.LETHALITY);

    public Plague(int playerId){
        this.playerId = playerId;
        this.dnaPoints = 0;
        this.plagueTokens = INITIAL_PLAGUE_TOKENS;
        this.traits = new ArrayList<>();
        this.traits.addAll(DEFAULT_TRAITS);
        this.hand = new ArrayList<>();
        this.eventCards = new ArrayList<>();
        this.traitSlots = new ArrayList<>();
        this.killedCountries = new HashSet<>();
        //TODO: Implement disease abilities
        for(int i = 0; i<5; i++){
            this.traitSlots.add(new TraitSlot(Optional.empty()));
        }
    }

    public int getPlayerId(){ return playerId; }

    public int getDnaPoints(){ return dnaPoints; }

    public int getPlagueTokens(){ return plagueTokens; }

    public List<TraitSlot> getTraitSlots(){ return traitSlots; }

    public List<TraitType> getTraits(){ return traits; }

    public List<TraitCard> getHand(){ return hand; }

    public List<Event> getEventCards(){ return eventCards; }

    public Set<Country> getKilledCountries(){ return killedCountries; }

    public int getTraitCount(TraitType trait){
        return traits.stream().filter(thisTrait -> thisTrait == trait).toList().size();
    }

    public List<TraitCard> clearHand(){
        List<TraitCard> oldHand = hand;
        hand = new ArrayList<>();
        return oldHand;
    }

    public void drawTraitCard(TraitCard card){
        hand.add(card);
    }

    public void addEventCard(Event event){
        if(eventCards.size() >= 3){
            throw new IllegalStateException("Cannot add a new event card to this players hand as they already have 3 cards");
        }
        eventCards.add(event);
    }
    
    public void addDnaPoints(int points){
        if(points < 0){
            throw new IllegalArgumentException("Cannot subtract from DNA points using the addDnaPoints method");
        }
        this.dnaPoints += points;
    }

    public void spendDnaPoints(int points){
        if(points < 0){
            throw new IllegalArgumentException("Must provide a positive number to the spendDnaPoints method");
        }
        this.dnaPoints = Math.max(0, this.dnaPoints - points);
    }

    public void placePlagueToken(){
        if(this.plagueTokens <= 0){
            throw new IllegalStateException("Cannot place a plague token if the plague has none in the supply");
        }
        this.plagueTokens--;
    }

    public void returnPlagueTokens(int numTokens){
        if(plagueTokens + numTokens > INITIAL_PLAGUE_TOKENS){
            throw new IllegalArgumentException("Cannot add plague tokens which would cause the plague to have a greater number of tokens than their initial amount (numTokens=" + numTokens + ", plagueTokens=" + plagueTokens + ")");
        }
        plagueTokens += numTokens;
    }

    public void killCountry(Country country){
        killedCountries.add(country);
    }


    public Map<Continent, List<Country>> filterKilledCountriesByContinent(){
        return Stream.of(Continent.values())
                     .collect(Collectors.toMap(Function.identity(), 
                              continent -> killedCountries.stream().filter(country -> country.getContinent() == continent).toList()));
    }


}
