package com.softdesign.plagueinc.exceptions;

public class TraitSlotFullException extends IllegalArgumentException {
    public TraitSlotFullException(String message){
        super(message);
    }

    public TraitSlotFullException(){
        super();
    }
}
