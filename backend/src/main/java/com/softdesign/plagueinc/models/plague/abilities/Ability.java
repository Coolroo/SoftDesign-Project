package com.softdesign.plagueinc.models.plague.abilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.softdesign.plagueinc.models.gamestate.GameState;

public abstract class Ability {

    @JsonIgnore
    protected Logger logger = LoggerFactory.getLogger(Ability.class);

    protected boolean activated;

    protected String name;

    protected Ability(String name){
        this.activated = false;
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void resolveAbility(GameState gameState){}

    public void resetAbility(){
        this.activated = false;
    }
}
