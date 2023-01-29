package com.softdesign.plagueinc.models.plague.abilities;

public abstract class Ability {

    private boolean activated;

    private String name;

    protected Ability(String name){
        this.activated = false;
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void resolveAbility(){}

    public void resetAbility(){
        this.activated = false;
    }
}
