package com.softdesign.plagueinc.models.plague.trait_slot;

public class TraitSlotFullException extends IllegalArgumentException {
    public TraitSlotFullException(String message){
        super(message);
    }

    public TraitSlotFullException(){
        super();
    }
}
