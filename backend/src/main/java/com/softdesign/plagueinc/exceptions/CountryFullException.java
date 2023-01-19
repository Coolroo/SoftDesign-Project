package com.softdesign.plagueinc.exceptions;

public class CountryFullException extends IllegalArgumentException{
    public CountryFullException(String message){
        super(message);
    }

    public CountryFullException(){
        super();
    }
}
