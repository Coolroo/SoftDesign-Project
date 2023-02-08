package com.softdesign.plagueinc.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import com.softdesign.plagueinc.models.events.EventCard;
import com.softdesign.plagueinc.models.events.EventCard.GameStateAction;
import com.softdesign.plagueinc.models.gamestate.PlayState;

public class EventReference {

    public static List<EventCard> getDefaultEventDeck(){
        //TODO: Implement default event deck
        List<EventCard> list = new ArrayList<>();
        IntStream.range(0,10).forEach(val -> {
            list.add(dnaFlow());
            list.add(geneticSurge());
        });
        return list;
    }

    public static EventCard dnaFlow(){
        GameStateAction condition = (plague, gameState) -> {
            if(gameState.getPlayState() != PlayState.START_OF_TURN){
                throw new IllegalStateException("PlayState is not START_OF_TURN");
            }
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }
        };

        GameStateAction event = (plague, gameState) -> {
            plague.addDnaPoints(4);
            return;
        };
        return new EventCard("dna_flow", condition, event);
    }

    public static EventCard geneticSurge(){

        GameStateAction condition = (plague, gameState) -> {
            if(gameState.getPlayState() != PlayState.END_OF_TURN){
                throw new IllegalStateException("PlayState is not END_OF_TURN");
            }
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }
        };

        GameStateAction event = (plague, gameState) -> {
            gameState.drawTraitCards(4).forEach(card -> plague.drawTraitCard(card));
        };
        return new EventCard("genetic_surge", condition, event);
    }

    public static EventCard birdMigration(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState) -> {};
        GameStateAction event = (plague, gameState) -> {};
        return new EventCard("bird_migration", condition, event);
    }
    
}
