package com.softdesign.plagueinc.controllers.managers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.softdesign.plagueinc.models.countries.Country;
import com.softdesign.plagueinc.models.events.Event;
import com.softdesign.plagueinc.models.plague.Plague;
import com.softdesign.plagueinc.models.plague.trait_slot.TraitSlot;
import com.softdesign.plagueinc.models.traits.Trait;
import com.softdesign.plagueinc.models.traits.TraitCard;

public class PlagueManager {
    
    Logger logger = LoggerFactory.getLogger(PlagueManager.class);

    @Autowired
    public CountryManager countryManager;
    
    public void initializePlague(Plague plague, Country startingCountry, List<TraitCard> hand){
        countryManager.infectCountry(startingCountry, plague);
        hand.forEach(card -> plague.drawTraitCard(card));
    }

    public void activateTraitSlot(Plague plague, int slot){
        if(slot >= plague.getTraitSlots().size()){
            throw new IndexOutOfBoundsException("Index is greater than the number of slots that exist");
        }
        TraitSlot traitSlot = plague.getTraitSlots().get(slot);
        try{
            TraitCard card = traitSlot.getCard();
            List<Trait> traits = card.traits();

            traits.forEach(thisTrait -> plague.getTraits().remove(thisTrait.getTrait()));
            logger.info("(Plague {}) Discarding card {} from slot {}", plague.getPlayerId(), card.name(), slot);
        }
        catch(IllegalStateException e){
            try{
                logger.info("(Plague {}) Activating ability {}", plague.getPlayerId(), traitSlot.getAbility().getName());
            }
            catch(IllegalStateException e2){
                logger.warn("(Plague {}) Tried activating slot {} which has no contents", plague.getPlayerId(), slot);
                return;
            }
        }
        finally{
            traitSlot.activate();
        }
        
    }

    public void evolveTrait(Plague plague, int cardIndex, int traitSlotIndex){
        evolveTrait(plague, cardIndex, traitSlotIndex, 0);
    }

    public void evolveTrait(Plague plague, int cardIndex, int traitSlotIndex, int dnaDiscount){
        if(cardIndex >= plague.getHand().size() || cardIndex < 0){
            throw new IllegalArgumentException("Cannot evolve a card that is out of bounds (provided index=" + cardIndex + ", size of hand=" + plague.getHand().size() + ")");
        }
        evolveTrait(plague, plague.getHand().get(cardIndex), traitSlotIndex, dnaDiscount);
    }

    public void evolveTrait(Plague plague, TraitCard card, int traitSlotIndex, int dnaDiscount){
        if(traitSlotIndex >= plague.getTraitSlots().size()){
            throw new IllegalArgumentException("Cannot evolve a card into a slot with invalid index (provided index=" + traitSlotIndex + ", size of hand=" + plague.getTraitSlots().size() + ")");
        }
        
        if(plague.getDnaPoints() < card.cost() - dnaDiscount){
            throw new IllegalStateException("Plague " + plague.getPlayerId() + " does not have enough DNA to evolve " + card.name() + " (DNA=" + plague.getDnaPoints() + ", required DNA =" + (card.cost() - dnaDiscount));
        }

        TraitSlot traitSlot = plague.getTraitSlots().get(traitSlotIndex);

        if(traitSlot.hasCard()){
            throw new IllegalStateException("Cannot evolve a card into a slot that already has a card");
        }

        traitSlot.setCard(card);
        plague.getHand().remove(card);
        card.traits().forEach(trait -> plague.getTraits().add(trait.getTrait()));
        plague.spendDnaPoints(Math.max(0, card.cost() - dnaDiscount));        
    }

    public void useEventCard(Plague plague, int eventCardIndex){
        if(eventCardIndex < 0 || eventCardIndex >= plague.getEventCards().size()){
            throw new IllegalArgumentException("Cannot play event card which has an index which is out of bounds (eventCardIndex=" + eventCardIndex + ", eventCard Size=" + plague.getEventCards().size() + ")");
        }
        Event event = plague.getEventCards().get(eventCardIndex);
        
        try{
            event.resolveEffect(plague);
            plague.getEventCards().remove(eventCardIndex);
        }
        //TODO: Implement error checking
        catch(Exception e){

        }
        

    }

}
