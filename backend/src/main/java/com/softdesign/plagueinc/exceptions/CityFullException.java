package com.softdesign.plagueinc.exceptions;

public class CityFullException extends IllegalArgumentException{
    public CityFullException(String message){
        super(message);
    }

    public CityFullException(){
        super();
    }
}
