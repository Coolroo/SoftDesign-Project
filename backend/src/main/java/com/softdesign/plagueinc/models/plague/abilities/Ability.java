package com.softdesign.plagueinc.models.plague.abilities;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.softdesign.plagueinc.models.gamestate.ConditionalAction;
import com.softdesign.plagueinc.models.gamestate.GameState;
import com.softdesign.plagueinc.models.gamestate.GameStateAction;
import com.softdesign.plagueinc.models.gamestate.InputSelection;
import com.softdesign.plagueinc.models.gamestate.selection_objects.SelectionObject;
import com.softdesign.plagueinc.models.plague.Plague;

import lombok.Getter;

@Getter
public abstract class Ability extends ConditionalAction {

    @JsonIgnore
    protected Logger logger = LoggerFactory.getLogger(Ability.class);

    protected boolean activated;

    protected String name;

    protected Ability(String name, GameStateAction condition, GameStateAction action, List<InputSelection> requiredInputs){
        super(condition, action, requiredInputs);
        this.activated = false;
        this.name = name;
    }

    @Override
    public void condition(Plague plague, GameState gameState){
        if(this.activated){
            logger.warn("Attempted to activate an already activated ability");
            throw new IllegalAccessError();
        }
        super.condition(plague, gameState);
    }

    @Override
    public void resolveEffect(Plague plague, GameState gameState, List<SelectionObject> inputs){
        super.resolveEffect(plague, gameState, inputs);
        this.activated = true;
        
    }

    public Ability create(){
        return null;
    }

    public void resetAbility(){
        this.activated = false;
    }
}
