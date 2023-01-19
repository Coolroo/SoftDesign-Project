package com.softdesign.plagueinc.models.plague.trait_slot;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.softdesign.plagueinc.exceptions.TraitSlotFullException;
import com.softdesign.plagueinc.models.plague.Ability;
import com.softdesign.plagueinc.models.traits.TraitCard;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TraitSlot {

    Logger logger = LoggerFactory.getLogger(TraitSlot.class);

    private Optional<TraitCard> card;

    private Optional<Ability> ability;

    public TraitSlot(Optional<Ability> ability){
        this.ability = ability;
        this.card = Optional.empty();
    }

    public void setCard(TraitCard card){
        if(this.card.isPresent()){
            throw new TraitSlotFullException();
        }
        this.card = Optional.of(card);
    }

    public boolean hasCard(){
        return this.card.isPresent();
    }

    public boolean hasAbility(){
        return this.ability.isPresent();
    }

    public TraitCard getCard(){
        if(this.card.isEmpty()){
            logger.warn("Cannot get card from an empty slot");
            return null;
        }
        return this.card.get();
    }

    public Ability getAbility(){
        if(this.ability.isEmpty()){
            logger.warn("No ability to get!");
            return null;
        }
        return this.ability.get();
    }

    public void activate(){
        if(this.card.isEmpty()){
            if(this.ability.isEmpty()){
                throw new IllegalStateException("Cannot activate a slot that has nothing in it");
            }
            this.ability.get().resolveAbility();
        }
        else{
            this.card = Optional.empty();
        }
    }

    public int refundCard(){
        if(this.card.isEmpty()){
            throw new IllegalStateException("Cannot refund a card that does not exist");
        }
        int cost = this.card.get().cost();
        this.card = Optional.empty();
        return cost;
    }
}
