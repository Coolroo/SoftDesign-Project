package com.softdesign.plagueinc.exceptions;

public class ContinentFullException extends IllegalArgumentException{
    public ContinentFullException(String message){
        super(message);
    }

    public ContinentFullException(){
        super();
    }
}