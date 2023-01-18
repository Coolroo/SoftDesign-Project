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
import com.softdesign.plagueinc.models.traits.Trait;
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

    public void useEventCard(int eventCardIndex){
        if(eventCardIndex < 0 || eventCardIndex >= eventCards.size()){
            throw new IllegalArgumentException("Cannot play event card which has an index which is out of bounds (eventCardIndex=" + eventCardIndex + ", eventCard Size=" + eventCards.size() + ")");
        }
        Event event = eventCards.get(eventCardIndex);
        
        try{
            event.resolveEffect(this);
            eventCards.remove(eventCardIndex);
        }
        //TODO: Implement error checking
        catch(Exception e){

        }
        

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

    public void activateTraitSlot(int slot){
        if(slot >= traitSlots.size()){
            throw new IndexOutOfBoundsException("Index is greater than the number of slots that exist");
        }
        TraitSlot traitSlot = traitSlots.get(slot);
        try{
            TraitCard card = traitSlot.getCard();
            List<Trait> traits = card.traits();

            traits.forEach(thisTrait -> this.traits.remove(thisTrait.getTrait()));
            logger.info("(Plague {}) Discarding card {} from slot {}", playerId, card.name(), slot);
        }
        catch(IllegalStateException e){
            try{
                logger.info("(Plague {}) Activating ability {}", playerId, traitSlot.getAbility().getName());
            }
            catch(IllegalStateException e2){
                logger.warn("(Plague {}) Tried activating slot {} which has no contents", playerId, slot);
                return;
            }
        }
        finally{
            traitSlot.activate();
        }
        
    }

    public void evolveTrait(int cardIndex, int traitSlotIndex){
        evolveTrait(cardIndex, traitSlotIndex, 0);
    }

    public void evolveTrait(int cardIndex, int traitSlotIndex, int dnaDiscount){
        if(cardIndex >= hand.size() || cardIndex < 0){
            throw new IllegalArgumentException("Cannot evolve a card that is out of bounds (provided index=" + cardIndex + ", size of hand=" + hand.size() + ")");
        }
        evolveTrait(hand.get(cardIndex), traitSlotIndex, dnaDiscount);
    }

    public void evolveTrait(TraitCard card, int traitSlotIndex, int dnaDiscount){
        if(traitSlotIndex >= traitSlots.size()){
            throw new IllegalArgumentException("Cannot evolve a card into a slot with invalid index (provided index=" + traitSlotIndex + ", size of hand=" + traitSlots.size() + ")");
        }
        
        if(dnaPoints < card.cost() - dnaDiscount){
            throw new IllegalStateException("Plague " + playerId + " does not have enough DNA to evolve " + card.name() + " (DNA=" + dnaPoints + ", required DNA =" + (card.cost() - dnaDiscount));
        }

        TraitSlot traitSlot = traitSlots.get(traitSlotIndex);

        if(traitSlot.hasCard()){
            throw new IllegalStateException("Cannot evolve a card into a slot that already has a card");
        }

        traitSlot.setCard(card);
        hand.remove(card);
        card.traits().forEach(trait -> traits.add(trait.getTrait()));
        dnaPoints -= card.cost() - dnaDiscount;        
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
