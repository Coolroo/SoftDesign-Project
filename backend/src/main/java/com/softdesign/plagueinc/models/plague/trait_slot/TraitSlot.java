package com.softdesign.plagueinc.models.plague.trait_slot;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.softdesign.plagueinc.exceptions.TraitSlotFullException;
import com.softdesign.plagueinc.models.gamestate.GameState;
import com.softdesign.plagueinc.models.plague.abilities.Ability;
import com.softdesign.plagueinc.models.traits.TraitCard;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TraitSlot {

    @JsonIgnore
    Logger logger = LoggerFactory.getLogger(TraitSlot.class);

    @JsonSerialize(using = CardSerializer.class)
    private Optional<TraitCard> card;

    @JsonSerialize(using = AbilitySerializer.class)
    private Optional<Ability> ability;


    private static class CardSerializer extends JsonSerializer<TraitCard>{

        @Override
        public void serialize(TraitCard value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
                        gen.writeString(value.name());
        }
        
    }

    private static class AbilitySerializer extends JsonSerializer<Ability>{

        @Override
        public void serialize(Ability value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            // TODO Auto-generated method stub
            gen.writeString(value.getName());
            
        }
        
    }

    public TraitSlot(){
        this.ability = Optional.empty();
        this.card = Optional.empty();
    }

    public TraitSlot(Ability ability){
        this.ability = Optional.of(ability);
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
            return null;
        }
        return this.card.get();
    }

    public Ability getAbility(){
        if(this.ability.isEmpty()){
            return null;
        }
        return this.ability.get();
    }

    public void activate(GameState gameState){
        if(this.card.isEmpty()){
            if(this.ability.isEmpty()){
                throw new IllegalStateException("Cannot activate a slot that has nothing in it");
            }
            this.ability.get().resolveAbility(gameState);
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
