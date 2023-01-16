package com.softdesign.plagueinc.models.traits;

public abstract class Trait {

    private TraitType traitType;

    protected Trait(TraitType traitType){
        this.traitType = traitType;
    }
    
    public TraitType getTrait(){
        return traitType;
    }
}
