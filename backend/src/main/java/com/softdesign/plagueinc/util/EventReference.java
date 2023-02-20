package com.softdesign.plagueinc.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import com.softdesign.plagueinc.models.events.EventCard;
import com.softdesign.plagueinc.models.gamestate.GameStateAction;
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

    //In alphabetical order

    public static EventCard absorb(){
        //TODO: Implement condition, event, and inputList
        GameStateAction condition = (plague, gameState, list) -> {};
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("absorb", condition, event, null);
    }// end of absorb

    public static EventCard birdMigration(){
        //TODO: Implement condition, event, and inputList
        GameStateAction condition = (plague, gameState, list) -> {};
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("bird_migration", condition, event, null);
    }//end of birdMigration

    public static EventCard bombedInfectedCities(){
        //TODO: Implement condition, event, and inputList
        GameStateAction condition = (plague, gameState, list) -> {};
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("bombed_infected_cities", condition, event, null);
    }//end of bombedInfectedCities

    public static EventCard cdcAlert(){
        //TODO: Implement condition, event, and inputList
        GameStateAction condition = (plague, gameState, list) -> {};
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("cdc_alert", condition, event, null);
    }//end of cdcAlert

    public static EventCard corspeTransmission(){
        //TODO: Implement condition, event, and inputList
        GameStateAction condition = (plague, gameState, list) -> {};
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("corpse_transmission", condition, event, null);
    }//end of corpseTransmission

    public static EventCard divineIntervention(){
        //TODO: Implement condition, event, and inputList
        GameStateAction condition = (plague, gameState, list) -> {};
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("divine_intervention", condition, event, null);
    }//end of divineIntervention

    public static EventCard dnaFlow(){
        GameStateAction condition = (plague, gameState, list) -> {
            if(gameState.getPlayState() != PlayState.START_OF_TURN){
                throw new IllegalStateException("PlayState is not START_OF_TURN");
            }
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }
        };

        GameStateAction event = (plague, gameState, list) -> {
            plague.addDnaPoints(4);
        };
        return new EventCard("dna_flow", condition, event, List.of());
    }

    public static EventCard emergencyCare(){
        //TODO: Implement condition, event, and inputList
        GameStateAction condition = (plague, gameState, list) -> {};
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("emergency_care", condition, event, null);
    }//end of emergency_care

    public static EventCard executeInfected(){
        //TODO: Implement condition, event, and inputList
        GameStateAction condition = (plague, gameState, list) -> {};
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("execute_infected", condition, event, null);
    }//end of executeInfected

    public static EventCard geneticSurge(){
        //TODO: Implement inputList
        GameStateAction condition = (plague, gameState, list) -> {
            if(gameState.getPlayState() != PlayState.END_OF_TURN){
                throw new IllegalStateException("PlayState is not END_OF_TURN");
            }
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }
        };

        GameStateAction event = (plague, gameState, list) -> {
            gameState.drawTraitCards(4).forEach(card -> plague.drawTraitCard(card));
        };
        return new EventCard("genetic_surge", condition, event, List.of());
    }

    public static EventCard governmentCollapse(){
        //TODO: Implement condition, event, and inputList
        GameStateAction condition = (plague, gameState, list) -> {};
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("government_collapse", condition, event, null);
    }//end of governmentCollapse

    public static EventCard hereOnBusiness(){
        //TODO: Implement condition, event, and inputList
        GameStateAction condition = (plague, gameState, list) -> {};
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("here_on_business", condition, event, null);
    }//end of hereOnBusiness

    public static EventCard immuneReaction(){
        //TODO: Implement condition, event, and inputList
        GameStateAction condition = (plague, gameState, list) -> {};
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("immune_reaction", condition, event, null);
    }//end of immuneReaction

    public static EventCard infectedRefugees(){
        //TODO: Implement condition, event, and inputList
        GameStateAction condition = (plague, gameState, list) -> {};
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("infected_refugees", condition, event, null);
    }//end of infectedRefugees

    public static EventCard lethalBoost(){
        //TODO: Implement condition, event, and inputList
        GameStateAction condition = (plague, gameState, list) -> {};
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("lethal_boost", condition, event, null);
    }//end of divineIntervention

    public static EventCard lethalRelapse(){
        //TODO: Implement condition, event, and inputList
        GameStateAction condition = (plague, gameState, list) -> {};
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("divine_intervention", condition, event, null);
    }//end of lethalRelapse

    public static EventCard neutralise(){
        //TODO: Implement condition, event, and inputList
        GameStateAction condition = (plague, gameState, list) -> {};
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("neutralise", condition, event, null);
    }//end of neutralise

    public static EventCard newTradeRoute(){
        //TODO: Implement condition, event, and inputList
        GameStateAction condition = (plague, gameState, list) -> {};
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("new_trade_route", condition, event, null);
    }//end of newTradeRoute

    public static EventCard nuclearStrike(){
        //TODO: Implement condition, event, and inputList
        GameStateAction condition = (plague, gameState, list) -> {};
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("nuclear_strike", condition, event, null);
    }//end of nuclearStrike

    public static EventCard olympics(){
        //TODO: Implement condition, event, and inputList
        GameStateAction condition = (plague, gameState, list) -> {};
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("olympics", condition, event, null);
    }//end of olympics

    public static EventCard opportunisticBreakdown(){
        //TODO: Implement condition, event, and inputList
        GameStateAction condition = (plague, gameState, list) -> {};
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("opportunitstic_breakdown", condition, event, null);
    }//end of opportunisticBreakDown

    public static EventCard pandemicAlert(){
        //TODO: Implement condition, event, and inputList
        GameStateAction condition = (plague, gameState, list) -> {};
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("pandemic_alert", condition, event, null);
    }//end of pandemicAlert

    public static EventCard pilgrimage(){
        //TODO: Implement condition, event, and inputList
        GameStateAction condition = (plague, gameState, list) -> {};
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("pilgrimage", condition, event, null);
    }//end of pilgrimage

    public static EventCard rioting(){
        //TODO: Implement condition, event, and inputList
        GameStateAction condition = (plague, gameState, list) -> {};
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("rioting", condition, event, null);
    }//end of rioting

    public static EventCard soapShortage(){
        //TODO: Implement condition, event, and inputList
        GameStateAction condition = (plague, gameState, list) -> {};
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("soap_shortage", condition, event, null);
    }//end of soap_shortage

    public static EventCard summerVacation(){
        //TODO: Implement condition, event, and inputList
        GameStateAction condition = (plague, gameState, list) -> {};
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("summerVacation", condition, event, null);
    }//end of summerVacation

    public static EventCard temporaryMutation(){
        //TODO: Implement condition, event, and inputList
        GameStateAction condition = (plague, gameState, list) -> {};
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("temporary_mutation", condition, event, null);
    }//end of temporaryMutation

    public static EventCard winterVacation(){
        //TODO: Implement condition, event, and inputList
        GameStateAction condition = (plague, gameState, list) -> {};
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("winter_vacation", condition, event, null);
    }//end of winterVacation



    


    
}//end of event reference
