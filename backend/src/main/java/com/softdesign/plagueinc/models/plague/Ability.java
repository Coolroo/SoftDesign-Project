package com.softdesign.plagueinc.models.plague;

public abstract class Ability {

    private String name;

    public Ability(String name){ this.name = name; }

    public String getName(){ return name; }

    public void resolveAbility(){}
}
