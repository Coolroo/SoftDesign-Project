package com.softdesign.plagueinc.models.plague;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.softdesign.plagueinc.models.countries.Continent;
import com.softdesign.plagueinc.models.countries.Country;
import com.softdesign.plagueinc.models.events.Event;
import com.softdesign.plagueinc.models.gamestate.GameState;
import com.softdesign.plagueinc.models.plague.trait_slot.TraitSlot;
import com.softdesign.plagueinc.models.traits.Trait;
import com.softdesign.plagueinc.models.traits.TraitCard;
import com.softdesign.plagueinc.models.traits.TraitType;
import com.softdesign.plagueinc.util.PlagueReference;

import lombok.Getter;

@Getter
public class Plague {

    @JsonIgnore
    Logger logger = LoggerFactory.getLogger(Plague.class);
    
    @JsonIgnore
    private UUID playerId;

    private PlagueColor color;

    private DiseaseType diseaseType;

    private int dnaPoints;

    private int plagueTokens;

    private List<TraitSlot> traitSlots;

    @JsonIgnore
    private List<TraitType> traits;

    @JsonIgnore
    private List<TraitCard> hand;

    @JsonIgnore
    private List<Event> eventCards;

    private Set<Country> killedCountries;

    private static final int INITIAL_PLAGUE_TOKENS = 16;

    private static final List<TraitType> DEFAULT_TRAITS = List.of(TraitType.INFECTIVITY, TraitType.INFECTIVITY, TraitType.LETHALITY);

    public Plague(PlagueColor plagueColor){
        this.playerId = UUID.randomUUID();
        this.dnaPoints = 0;
        this.color = plagueColor;
        this.plagueTokens = INITIAL_PLAGUE_TOKENS;
        this.traits = new ArrayList<>();
        this.traits.addAll(DEFAULT_TRAITS);
        this.hand = new ArrayList<>();
        this.eventCards = new ArrayList<>();
        this.traitSlots = new ArrayList<>();
        this.killedCountries = new HashSet<>();
    }

    public void setDiseaseType(DiseaseType diseaseType){
        this.diseaseType = diseaseType;
        this.traitSlots = PlagueReference.getTraitSlots(diseaseType);
    }

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

    public boolean hasTrait(TraitType trait){
        return traits.contains(trait);
    }

    public void initializePlague(Country startingCountry, List<TraitCard> hand){
        startingCountry.infectCountry(this);
        hand.forEach(card -> drawTraitCard(card));
    }

    public void activateTraitSlot(int slot, GameState gameState){
        if(slot >= getTraitSlots().size()){
            throw new IndexOutOfBoundsException("Index is greater than the number of slots that exist");
        }
        TraitSlot traitSlot = getTraitSlots().get(slot);
        try{
            TraitCard card = traitSlot.getCard();
            List<Trait> traits = card.traits();

            traits.forEach(thisTrait -> getTraits().remove(thisTrait.getTrait()));
            logger.info("(Plague {}) Discarding card {} from slot {}", getPlayerId(), card.name(), slot);
        }
        catch(IllegalStateException e){
            try{
                logger.info("(Plague {}) Activating ability {}", getPlayerId(), traitSlot.getAbility().getName());
            }
            catch(IllegalStateException e2){
                logger.warn("(Plague {}) Tried activating slot {} which has no contents", getPlayerId(), slot);
                return;
            }
        }
        finally{
            traitSlot.activate(gameState);
        }
        
    }

    public TraitCard evolveTrait(int cardIndex, int traitSlotIndex){
        return evolveTrait(cardIndex, traitSlotIndex, 0);
    }

    public TraitCard evolveTrait(int cardIndex, int traitSlotIndex, int dnaDiscount){
        if(cardIndex >= getHand().size() || cardIndex < 0){
            throw new IllegalArgumentException("Cannot evolve a card that is out of bounds (provided index=" + cardIndex + ", size of hand=" + getHand().size() + ")");
        }
        return evolveTrait(getHand().get(cardIndex), traitSlotIndex, dnaDiscount);
    }

    public TraitCard evolveTrait(TraitCard card, int traitSlotIndex, int dnaDiscount){
        if(traitSlotIndex >= getTraitSlots().size()){
            throw new IllegalArgumentException("Cannot evolve a card into a slot with invalid index (provided index=" + traitSlotIndex + ", size of hand=" + getTraitSlots().size() + ")");
        }
        
        if(getDnaPoints() < card.cost() - dnaDiscount){
            throw new IllegalStateException("Plague " + getPlayerId() + " does not have enough DNA to evolve " + card.name() + " (DNA=" + getDnaPoints() + ", required DNA =" + (card.cost() - dnaDiscount));
        }

        TraitSlot traitSlot = getTraitSlots().get(traitSlotIndex);

        if(traitSlot.hasCard()){
            throw new IllegalStateException("Cannot evolve a card into a slot that already has a card");
        }

        traitSlot.setCard(card);
        getHand().remove(card);
        card.traits().forEach(trait -> getTraits().add(trait.getTrait()));
        spendDnaPoints(Math.max(0, card.cost() - dnaDiscount));
        return card;        
    }

    public void useEventCard(int eventCardIndex){
        if(eventCardIndex < 0 || eventCardIndex >= getEventCards().size()){
            throw new IllegalArgumentException("Cannot play event card which has an index which is out of bounds (eventCardIndex=" + eventCardIndex + ", eventCard Size=" + getEventCards().size() + ")");
        }
        Event event = getEventCards().get(eventCardIndex);
        
        try{
            event.resolveEffect(this);
            getEventCards().remove(eventCardIndex);
        }
        //TODO: Implement error checking
        catch(Exception e){

        }
        
    }

}
