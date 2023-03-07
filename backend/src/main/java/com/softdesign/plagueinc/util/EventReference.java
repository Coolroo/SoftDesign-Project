package com.softdesign.plagueinc.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.concurrent.CompletableFuture;

import com.softdesign.plagueinc.managers.futures.input_types.CountryChoice;
import com.softdesign.plagueinc.models.action_log.CountryAction;
import com.softdesign.plagueinc.models.action_log.KillCountryAction;
import com.softdesign.plagueinc.models.countries.Country;
import com.softdesign.plagueinc.models.events.EventCard;
import com.softdesign.plagueinc.models.gamestate.GameStateAction;
import com.softdesign.plagueinc.models.gamestate.InputSelection;
import com.softdesign.plagueinc.models.gamestate.PlayState;
import com.softdesign.plagueinc.models.plague.Plague;

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

    //Cards In alphabetical order

    //If another player fails to kill a Country you infect, replace 1 of their Tokens in that Country with your own
    // ignoring climate
    public static EventCard absorb(){

        //Check whether a kill action was succesful , if unsuccessful proceed
        GameStateAction condition = (plague, gameState, list) -> {
            if(!(gameState.getActions().peek() instanceof KillCountryAction)){
                throw new IllegalStateException("Can't play this card at the moment");
            } 
            KillCountryAction action = (KillCountryAction) gameState.getActions().peek();
            Country attacked = action.getCountry();
            if(action.isSuccess()){
                throw new IllegalStateException("Can't play this card at the moment");
            }
        };
        
        GameStateAction event = (plague, gameState, list) -> {
            KillCountryAction action = (KillCountryAction) gameState.getActions().peek();
            Country attacked = action.getCountry();
            Plague currPlayer = gameState.getCurrTurn();
                    attacked.removePlague(currPlayer);//remove the players token from the country
                    attacked.infectCountry(plague);// add the card players token to the city
        };
        return new EventCard("absorb", condition, event, List.of());
    }// end of absorb

    //When another player places a country card, place one of your tokens in that country
    public static EventCard birdMigration(){
        
        GameStateAction condition = (plague, gameState, list) -> {

            // Check if the card is a CountryAction
            if(!(gameState.getActions().peek() instanceof CountryAction)){
                throw new IllegalStateException("Can't play this card");     
            }
            
            CountryAction playerAction = (CountryAction) gameState.getActions().peek();

            //Check if the player played the card
            if(playerAction.getCountryChoice() == CountryChoice.DISCARD){
                throw new IllegalStateException("Can't play this card");
            }
        };
        GameStateAction event = (plague, gameState, list) -> {

            try{
                CountryAction cardPlaced = (CountryAction) gameState.getActions().peek();
                cardPlaced.getCountry().infectCountry(plague);
            } catch(Exception ex){
                // problem if they don't have enough tokens
            }

            

        };
        return new EventCard("bird_migration", condition, event, List.of());
    }//end of birdMigration

    public static EventCard bombedInfectedCities(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState) -> {
            if(gameState.getPlayState()!= PlayState.END_OF_TURN){
                throw new IllegalStateException("PlayState is not END_OF_TERM");
            }
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }
        };
        GameStateAction event = (plague, gameState) -> {};
        return new EventCard("bombed_infected_cities", condition, event);
    }//end of bombedInfectedCities

    public static EventCard cdcAlert(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState) -> {};
        GameStateAction event = (plague, gameState) -> {};
        return new EventCard("cdc_alert", condition, event);
    }//end of cdcAlert

    public static EventCard corspeTransmission(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState) -> {};
        GameStateAction event = (plague, gameState) -> {};
        return new EventCard("corpse_transmission", condition, event);
    }//end of corpseTransmission

    public static EventCard divineIntervention(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState) -> {};
        GameStateAction event = (plague, gameState) -> {};
        return new EventCard("divine_intervention", condition, event);
    }//end of divineIntervention

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
        };
        return new EventCard("dna_flow", condition, event);
    }//end of dnaFlow

    public static EventCard emergencyCare(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState) -> {};
        GameStateAction event = (plague, gameState) -> {};
        return new EventCard("emergency_care", condition, event);
    }//end of emergency_care

    public static EventCard executeInfected(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState) -> {
            if(gameState.getPlayState() != PlayState.END_OF_TURN){
                throw new IllegalStateException("PlayState is not END_OF_TURN");
            }
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }

            //TODO: Add logic for other conditions on card (if killed a country)
        };
        GameStateAction event = (plague, gameState) -> {};
        return new EventCard("execute_infected", condition, event);
    }//end of executeInfected

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
    }// end of geneticSurge

    public static EventCard governmentCollapse(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState) -> {
            if(gameState.getPlayState() != PlayState.END_OF_TURN){
                throw new IllegalStateException("PlayState is not END_OF_TURN");
            }
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }

            //TODO: add additional conditions for card
        };
        GameStateAction event = (plague, gameState) -> {};
        return new EventCard("government_collapse", condition, event);
    }//end of governmentCollapse

    public static EventCard hereOnBusiness(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState) -> {
            if(gameState.getPlayState() != PlayState.START_OF_TURN){
                throw new IllegalStateException("PlayState is not START_OF_TURN");
            }
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }

            //TODO: add aditional conditions
        };
        GameStateAction event = (plague, gameState) -> {};
        return new EventCard("here_on_business", condition, event);
    }//end of hereOnBusiness

    public static EventCard immuneReaction(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState) -> {
            if(gameState.getPlayState() != PlayState.START_OF_TURN){
                throw new IllegalStateException("PlayState is not START_OF_TURN");
            }
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }

            //TODO: add aditional conditions
        };
        GameStateAction event = (plague, gameState) -> {};
        return new EventCard("immune_reaction", condition, event);
    }//end of immuneReaction

    public static EventCard infectedRefugees(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState) -> {
            if(gameState.getPlayState() != PlayState.END_OF_TURN){
                throw new IllegalStateException("PlayState is not END_OF_TURN");
            }
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }

            //TODO: add aditional conditions
        };
        GameStateAction event = (plague, gameState) -> {};
        return new EventCard("infected_refugees", condition, event);
    }//end of infectedRefugees

    public static EventCard lethalBoost(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState) -> {};
        GameStateAction event = (plague, gameState) -> {};
        return new EventCard("lethal_boost", condition, event);
    }//end of divineIntervention

    public static EventCard lethalRelapse(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState) -> {};
        GameStateAction event = (plague, gameState) -> {};
        return new EventCard("divine_intervention", condition, event);
    }//end of lethalRelapse

    public static EventCard neutralise(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState) -> {};
        GameStateAction event = (plague, gameState) -> {};
        return new EventCard("neutralise", condition, event);
    }//end of neutralise

    public static EventCard newTradeRoute(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState) -> {
            if(gameState.getPlayState() != PlayState.START_OF_TURN){
                throw new IllegalStateException("PlayState is not START_OF_TURN");
            }
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }

            //TODO: add aditional conditions
        };
        GameStateAction event = (plague, gameState) -> {};
        return new EventCard("new_trade_route", condition, event);
    }//end of newTradeRoute

    public static EventCard nuclearStrike(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState) -> {
            if(gameState.getPlayState() != PlayState.END_OF_TURN){
                throw new IllegalStateException("PlayState is not END_OF_TURN");
            }
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }

            //TODO: add aditional conditions
        };
        GameStateAction event = (plague, gameState) -> {};
        return new EventCard("nuclear_strike", condition, event);
    }//end of nuclearStrike

    public static EventCard olympics(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState) -> {
            if(gameState.getPlayState() != PlayState.START_OF_TURN){
                throw new IllegalStateException("PlayState is not START_OF_TURN");
            }
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }

            //TODO: add aditional conditions
        };
        GameStateAction event = (plague, gameState) -> {};
        return new EventCard("olympics", condition, event);
    }//end of olympics

    public static EventCard opportunisticBreakdown(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState) -> {
            if(gameState.getPlayState() != PlayState.END_OF_TURN){
                throw new IllegalStateException("PlayState is not END_OF_TURN");
            }
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }

            //TODO: add aditional conditions
        };
        GameStateAction event = (plague, gameState) -> {};
        return new EventCard("opportunitstic_breakdown", condition, event);
    }//end of opportunisticBreakDown

    public static EventCard pandemicAlert(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState) -> {
            if(gameState.getPlayState() != PlayState.END_OF_TURN){
                throw new IllegalStateException("PlayState is not END_OF_TURN");
            }
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }

            //TODO: add aditional conditions
        };
        GameStateAction event = (plague, gameState) -> {};
        return new EventCard("pandemic_alert", condition, event);
    }//end of pandemicAlert

    public static EventCard pilgrimage(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState) -> {
            if(gameState.getPlayState() != PlayState.START_OF_TURN){
                throw new IllegalStateException("PlayState is not START_OF_TURN");
            }
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }

            //TODO: add aditional conditions
        };
        GameStateAction event = (plague, gameState) -> {};
        return new EventCard("pilgrimage", condition, event);
    }//end of pilgrimage

    public static EventCard rioting(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState) -> {
            if(!gameState.getPlayState.equals(PlayState.END_OF_TURN)){
                throw new IllegalStateException("Game is not at end of turn");
            }
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }
            
        };
        FutureInterface<CompletableFuture<Country>> createFuture = (plague) -> {
            return new CompletableFuture<>().whenComplete((country, ex) -> {
                if(ex != null){
                    //TODO implement error checking
                }
                else{
                    if(country.getInfectionByPlayer().keySet().contains(plague)){
                        country.infectCountry(plague);
                        country.infectCountry(plague);
                    }
                    else{
                        //TODO implement error checking
                    }
                }
            });
        };
        GameStateAction event = (plague, gameState) -> {
            gameState.setCountrySelectionFuture(createFuture.op(plague));
        };
        

        return new EventCard("rioting", condition, event);
    }//end of rioting

    public static EventCard soapShortage(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState) -> {
            if(gameState.getPlayState() != PlayState.START_OF_TURN){
                throw new IllegalStateException("PlayState is not START_OF_TURN");
            }
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }

            //TODO: add aditional conditions
        };
        GameStateAction event = (plague, gameState) -> {};
        return new EventCard("soap_shortage", condition, event);
    }//end of soap_shortage

    public static EventCard summerVacation(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState) -> {
            if(gameState.getPlayState() != PlayState.START_OF_TURN){
                throw new IllegalStateException("PlayState is not START_OF_TURN");
            }
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }

            //TODO: add aditional conditions
        };
        GameStateAction event = (plague, gameState) -> {};
        return new EventCard("summerVacation", condition, event);
    }//end of summerVacation

    public static EventCard temporaryMutation(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState) -> {
            if(gameState.getPlayState() != PlayState.START_OF_TURN){
                throw new IllegalStateException("PlayState is not START_OF_TURN");
            }
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }

            //TODO: add aditional conditions
        };
        GameStateAction event = (plague, gameState) -> {};
        return new EventCard("temporary_mutation", condition, event);
    }//end of temporaryMutation

    public static EventCard winterVacation(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState) -> {
            if(gameState.getPlayState() != PlayState.START_OF_TURN){
                throw new IllegalStateException("PlayState is not START_OF_TURN");
            }
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }

            //TODO: add aditional conditions
        };
        GameStateAction event = (plague, gameState) -> {};
        return new EventCard("winter_vacation", condition, event);
    }//end of winterVacation



    private static interface FutureInterface<T>{
        public CompletableFuture<T> op();
    }


    
}//end of event reference
