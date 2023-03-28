package com.softdesign.plagueinc.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.concurrent.CompletableFuture;

import com.softdesign.plagueinc.managers.futures.input_types.CountryChoice;
import com.softdesign.plagueinc.models.action_log.CountryAction;
import com.softdesign.plagueinc.models.action_log.InfectCountryAction;
import com.softdesign.plagueinc.models.action_log.KillCountryAction;
import com.softdesign.plagueinc.models.countries.Country;
import com.softdesign.plagueinc.models.events.EventCard;
import com.softdesign.plagueinc.models.gamestate.GameStateAction;
import com.softdesign.plagueinc.models.gamestate.InputSelection;
import com.softdesign.plagueinc.models.gamestate.PlayState;
import com.softdesign.plagueinc.models.gamestate.selection_objects.CitySelection;
import com.softdesign.plagueinc.models.gamestate.selection_objects.CountrySelection;
import com.softdesign.plagueinc.models.gamestate.selection_objects.SelectionObject;
import com.softdesign.plagueinc.models.plague.Plague;

public class EventReference {

    public static List<EventCard> getDefaultEventDeck(){
        //TODO: Implement default event deck
        List<EventCard> list = new ArrayList<>();
        IntStream.range(0,10).forEach(val -> {
            //list.add(absorb());
            list.add(birdMigration());
            /*
            list.add(bombedInfectedCities());
            list.add(cdcAlert());
            list.add(corspeTransmission());
            list.add(divineIntervention());
            list.add(dnaFlow());
            list.add(emergencyCare());
            list.add(executeInfected());
            list.add(geneticSurge());
            list.add(governmentCollapse());
            list.add(hereOnBusiness());
            list.add(immuneReaction());
            list.add(infectedRefugees());
            list.add(lethalBoost());
            list.add(lethalRelapse());
            list.add(neutralise());
            list.add(newTradeRoute());
            list.add(nuclearStrike());
            list.add(olympics());
            list.add(opportunisticBreakdown());
            list.add(pandemicAlert());
            list.add(pilgrimage());
            list.add(rioting());
            list.add(soapShortage());
            list.add(summerVacation());
            list.add(temporaryMutation());
            list.add(winterVacation());
            */
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
    }// end of absorb; DONE



    // When another player places a country card, place one of your tokens in that country
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
                // If they don't have enough tokens, but no need to code anything for this as it is taken care of elsewhere
            }   

        };
        return new EventCard("bird_migration", condition, event, List.of());
    }//end of birdMigration; DONE

    // At the end of your turn, if you killed any Countries, choose 1 country. Remove up to three tokens from that country
    public static EventCard bombedInfectedCities(){
        //that it is the end of the player's turn and that it is currently their turn
        GameStateAction condition = (plague, gameState, list) -> {
            //check for end of turn
            if(gameState.getPlayState()!= PlayState.END_OF_TURN){
                throw new IllegalStateException("PlayState is not END_OF_TERM");
            }
            //check that it is the players turn
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }

            //check that a country was killed
            KillCountryAction action = (KillCountryAction) gameState.getActions().peek();
            Country attacked = action.getCountry();
            if(!(action.isSuccess())){
                throw new IllegalStateException("Can't play this card at the moment");
            }

            
        };

        GameStateAction event = (plague, gameState, list) -> {
            //choose a country and remove 3 tokens
            CitySelection one =(CitySelection) list.get(0);
            CitySelection two =(CitySelection) list.get(1);
            CitySelection three =(CitySelection) list.get(2);

            String name1 = one.getCountryName();
            String name2 = two.getCountryName();
            String name3 = three.getCountryName();

            //check all are from the same country
            if(name1.equals(name2) && name2.equals(name3)){
                //remove 3 tokens
                //Potential problem if they select a city without a token

                Country toRemoveTokens = gameState.getCountry(name3);

                Plague toRemove1 = toRemoveTokens.getCities().get(one.getCityIndex()).get();
                Plague toRemove2 = toRemoveTokens.getCities().get(two.getCityIndex()).get();
                Plague toRemove3 = toRemoveTokens.getCities().get(three.getCityIndex()).get();

                toRemoveTokens.removePlague(toRemove1);
                toRemoveTokens.removePlague(toRemove2);
                toRemoveTokens.removePlague(toRemove3);

            }

        };
        return new EventCard("bombed_infected_cities", condition, event, List.of(InputSelection.CITY, InputSelection.CITY,InputSelection.CITY));
    }//end of bombedInfectedCities; DONE


    // If a player tries to place a Token in a Country, prevent it. No one can place
    // Tokens in that country until your next turn
    public static EventCard cdcAlert(){
        //When a player attempts to place a Token in a Country
        GameStateAction condition = (plague, gameState, list) -> {
            if (!(gameState.getActions().peek() instanceof InfectCountryAction)){
                throw new IllegalStateException("Can't play this card at the moment");
            }
        };

        //Prevent placement, no one can place in the specified country until your next turn

        GameStateAction event = (plague, gameState, list) -> {

            InfectCountryAction action = (InfectCountryAction) gameState.getActions().peek();
            Country attacked = action.getCountry();
            Plague currPlayer = gameState.getCurrTurn();
            attacked.removePlague(currPlayer);//remove the players token from the country (this is the same as preventing placement)

            //TODO: prevent anyone from placing in that country until card player's next turn
            
        };
        return new EventCard("cdc_alert", condition, event, List.of());
    }//end of cdcAlert


    /*
     * At the end of your turn, if you killed any Countries, choose 1 Country you are Connected to.
     * Place up to 2 of your Tokens in that Country (ignore climate)
     */
    public static EventCard corspeTransmission(){
        GameStateAction condition = (plague, gameState, list) -> {

            //check for end of turn
            if(gameState.getPlayState()!= PlayState.END_OF_TURN){
                throw new IllegalStateException("PlayState is not END_OF_TERM");
            }

            //Check if a country was killed
            if(!(gameState.getActions().peek() instanceof KillCountryAction)){
                throw new IllegalStateException("Can't play this card at the moment");
            } 
            KillCountryAction action = (KillCountryAction) gameState.getActions().peek();
            if(!(action.isSuccess())){
                throw new IllegalStateException("Can't play this card at the moment");
            }

        };


        GameStateAction event = (plague, gameState, list) -> {

            // TODO: Make sure the selection country is connected to one of the users 


        };

        return new EventCard("corpse_transmission", condition, event, List.of(InputSelection.COUNTRY));
    }//end of corpseTransmission



    /*
     * At the start of your turn, choose up to 2 Countries that you infect. Remove 1 token from each
     * country
     */
    public static EventCard divineIntervention(){

        GameStateAction condition = (plague, gameState, list) -> {

            // Check it is start of of players turn and that it is their turn
            if(gameState.getPlayState() != PlayState.START_OF_TURN){
                throw new IllegalStateException("PlayState is not START_OF_TURN");
            }
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }
        };
        GameStateAction event = (plague, gameState, list) -> {
            //TODO: implement remove token from counrty that you play in

            CountrySelection one = (CountrySelection) list.get(0);
            CountrySelection two = (CountrySelection) list.get(1);

            Country toInfectOne = (Country) gameState.getCountry(one.getCountryName());
            Country toInfectTwo = (Country) gameState.getCountry(two.getCountryName());

            toInfectOne.infectCountry(plague);
            toInfectTwo.infectCountry(plague);

            //remove a token from each country give city selections check they are in the countries and remove tokens?
        };
        return new EventCard("divine_intervention", condition, event, List.of(InputSelection.COUNTRY, InputSelection.COUNTRY));
    }//end of divineIntervention


    /*
     * At the start of your turn score +4 extra DNA points
     */
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
    }//end of dnaFlow


    /*
     * If any player successfully rolls the Death Dice, prevent that Country from dying and remove 1
     * of that player's Tokens from it.
     */
    public static EventCard emergencyCare(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState, list) -> {};
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("emergency_care", condition, event, List.of());
    }//end of emergency_care


    /*
     * At the end of your turn, if you killed any Countries, choose up to 4 countries.
     * Remove 1 Token from each Country.
     */
    public static EventCard executeInfected(){
        //TODO: Implement condition & event, & List.of()
        GameStateAction condition = (plague, gameState, list) -> {
            if(gameState.getPlayState() != PlayState.END_OF_TURN){
                throw new IllegalStateException("PlayState is not END_OF_TURN");
            }
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }

            
        };
        GameStateAction event = (plague, gameState, list) -> {
            //TODO: remove 1 token from each of up to 4 countries

            //Questions what if there is not 4 countries that can be chose?

        };
        return new EventCard("execute_infected", condition, event, List.of(InputSelection.COUNTRY,InputSelection.COUNTRY,InputSelection.COUNTRY,InputSelection.COUNTRY));
    }//end of executeInfected


    /*
     * At the end of your turn, draw 4 new Trait Cards
     */
    public static EventCard geneticSurge(){

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
    }// end of geneticSurge


    /*
     * At the end of your turn, if you killed any Countries, choose 1 Country that you infect. Place
     * up to 3 tokens in that Country (ignore climate)
     */
    public static EventCard governmentCollapse(){

        GameStateAction condition = (plague, gameState, list) -> {
            if(gameState.getPlayState() != PlayState.END_OF_TURN){
                throw new IllegalStateException("PlayState is not END_OF_TURN");
            }
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }

            //Check that a country was killed
            if(!(gameState.getActions().peek() instanceof KillCountryAction)){
                throw new IllegalStateException("Can't play this card at the moment");
            } 
            KillCountryAction action = (KillCountryAction) gameState.getActions().peek();
            if(!(action.isSuccess())){
                throw new IllegalStateException("Can't play this card at the moment");
            }

            
        };


        GameStateAction event = (plague, gameState, list) -> {

            //Get the country selection
            CountrySelection one = (CountrySelection) list.get(0);

            Country toInfectOne = (Country) gameState.getCountry(one.getCountryName());

            //infect country three times
            toInfectOne.infectCountry(plague);
            toInfectOne.infectCountry(plague);
            toInfectOne.infectCountry(plague);
            

        };
        return new EventCard("government_collapse", condition, event, List.of(InputSelection.COUNTRY));
    }//end of governmentCollapse


    /*
     * At the start of your turn, choose 1 Country. Move 1 of your tokens to that Country from any
     * other Country (ignore climate/connected)
     */
    public static EventCard hereOnBusiness(){

        GameStateAction condition = (plague, gameState, list) -> {
            if(gameState.getPlayState() != PlayState.START_OF_TURN){
                throw new IllegalStateException("PlayState is not START_OF_TURN");
            }
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }

            
        };
        GameStateAction event = (plague, gameState, list) -> {

            CountrySelection one = (CountrySelection) list.get(0);
            CountrySelection two = (CountrySelection) list.get(1);

            //Get the two countries
            Country toInfectOne = (Country) gameState.getCountry(one.getCountryName());
            Country toRemoveFrom = (Country) gameState.getCountry(two.getCountryName());

            //remove one token and add one token
            toRemoveFrom.removePlague(plague);
            toInfectOne.infectCountry(plague);
        };
        return new EventCard("here_on_business", condition, event, List.of(InputSelection.COUNTRY, InputSelection.COUNTRY));
    }//end of hereOnBusiness; DONE



    /*
     * At the start of your turn, choose 1 Country that you infect. Remove up to 2 tokens
     * from that Country.
     */
    public static EventCard immuneReaction(){
        
        GameStateAction condition = (plague, gameState, list) -> {

            if(gameState.getPlayState() != PlayState.START_OF_TURN){
                throw new IllegalStateException("PlayState is not START_OF_TURN");
            }
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }

            
        };
        GameStateAction event = (plague, gameState, list) -> {
            
            CountrySelection one = (CountrySelection) list.get(0);
            
            //Get the country
            Country toInfectOne = (Country) gameState.getCountry(one.getCountryName());
            
            // add one token
            toInfectOne.infectCountry(plague);

            // remove two tokens
            //check that the city selection are in the same country as the one infected then remove tokens

               //choose a country and remove 3 tokens
               CitySelection cityOne =(CitySelection) list.get(1);
               CitySelection cityTwo =(CitySelection) list.get(2);
               
   
               String name1 = cityOne.getCountryName();
               String name2 = cityTwo.getCountryName();
              
   
               //check all are from the same country
               if(name1.equals(name2) && name1.equals(toInfectOne.getCountryName())){

                   //remove 2 tokens
                   //Potential problem if they select a city without a token
   
                   Country toRemoveTokens = gameState.getCountry(name2);
   
                   Plague toRemove1 = toRemoveTokens.getCities().get(cityOne.getCityIndex()).get();
                   Plague toRemove2 = toRemoveTokens.getCities().get(cityTwo.getCityIndex()).get();
                   
   
                   toRemoveTokens.removePlague(toRemove1);
                   toRemoveTokens.removePlague(toRemove2);
                   
   
               }

        };
        return new EventCard("immune_reaction", condition, event, List.of(InputSelection.COUNTRY, InputSelection.CITY,InputSelection.CITY));
    }//end of immuneReaction --DONE


    /*
     * At the end of your turn, if you killed any Countries, choose 1 Country.
     * Move up to 3 tokens from it to any other Countries. (Ignore climate/connected)
     */
    public static EventCard infectedRefugees(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState, list) -> {
            if(gameState.getPlayState() != PlayState.END_OF_TURN){
                throw new IllegalStateException("PlayState is not END_OF_TURN");
            }
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }

            //TODO: add aditional conditions
        };
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("infected_refugees", condition, event, List.of());
    }//end of infectedRefugees


    /*
     * Before you roll the Death Dice add +3 to your Lethality for that roll only. (Announce in advance)
     */
    public static EventCard lethalBoost(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState, list) -> {};
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("lethal_boost", condition, event, List.of());
    }//end of divineIntervention


    /*
     * If you fail to kill a Country, re-roll the Death Dice for that Country. Add +1 to your lethality
     * for that roll only.
     */
    public static EventCard lethalRelapse(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState, list) -> {};
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("divine_intervention", condition, event, List.of());
    }//end of lethalRelapse


    /*
     * If any player tries to play an Event Card, prevent its action and discard it.
     */
    public static EventCard neutralise(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState, list) -> {};
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("neutralise", condition, event, List.of());
    }//end of neutralise


    /*
     * At the start of your turn, swap 1 token in any Country with 1 token from any other Country.
     * (Ignore climate/connected)
     */
    public static EventCard newTradeRoute(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState, list) -> {
            if(gameState.getPlayState() != PlayState.START_OF_TURN){
                throw new IllegalStateException("PlayState is not START_OF_TURN");
            }
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }

            //TODO: add aditional conditions
        };
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("new_trade_route", condition, event, List.of());
    }//end of newTradeRoute


/*
 * At the end of your turn, if you killed any Countries, choose a single Country. At the start of your
 * next turn, if that Country is still alive, immediately discard it.
 * Tokens are returned, but no points are awarded
 */
    public static EventCard nuclearStrike(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState, list) -> {
            if(gameState.getPlayState() != PlayState.END_OF_TURN){
                throw new IllegalStateException("PlayState is not END_OF_TURN");
            }
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }

            //TODO: add aditional conditions
        };
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("nuclear_strike", condition, event, List.of());
    }//end of nuclearStrike


    /*
     * At the start of your turn, choose 1 Country.
     * Move 1 token from every player into that Country. If not enough space for all Tokens, you 
     * choose who to move (ignore climate/ connected)
     */
    public static EventCard olympics(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState, list) -> {
            if(gameState.getPlayState() != PlayState.START_OF_TURN){
                throw new IllegalStateException("PlayState is not START_OF_TURN");
            }
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }

            //TODO: add aditional conditions
        };
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("olympics", condition, event, List.of());
    }//end of olympics


    /*
     * At the end of your turn, desolve a Trait, add its DNA cost back onto you DNA score and 
     * draw 2 new trait cards.
     */
    public static EventCard opportunisticBreakdown(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState, list) -> {
            if(gameState.getPlayState() != PlayState.END_OF_TURN){
                throw new IllegalStateException("PlayState is not END_OF_TURN");
            }
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }

            //TODO: add aditional conditions
        };
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("opportunitstic_breakdown", condition, event, List.of());
    }//end of opportunisticBreakDown


    /*
     * At the end of your turn, choose a Continent. No one can place Tokens in that Continent until
     * your next turn.
     */
    public static EventCard pandemicAlert(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState, list) -> {
            if(gameState.getPlayState() != PlayState.END_OF_TURN){
                throw new IllegalStateException("PlayState is not END_OF_TURN");
            }
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }

            //TODO: add aditional conditions
        };
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("pandemic_alert", condition, event, List.of());
    }//end of pandemicAlert


    /*
     * At the start of your turn choose a Neutral Climate Country.
     * Move 2 Tokens from anywhere into it. (Ignore connected)
     */
    public static EventCard pilgrimage(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState, list) -> {
            if(gameState.getPlayState() != PlayState.START_OF_TURN){
                throw new IllegalStateException("PlayState is not START_OF_TURN");
            }
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }

            //TODO: add aditional conditions
        };
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("pilgrimage", condition, event, List.of());
    }//end of pilgrimage


    /*
     * At the end of your turn, choose 1 climate suitable Country that you infect. Place up to 2 
     * Tokens in that Country
     */
    public static EventCard rioting(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState, list) -> {
            if(!gameState.getPlayState().equals(PlayState.END_OF_TURN)){
                throw new IllegalStateException("Game is not at end of turn");
            }
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }
            
        };
      
        GameStateAction event = (plague, gameState, list) -> {
            
        };
        

        return new EventCard("rioting", condition, event, List.of());
    }//end of rioting

    /*
     * At the start of your turn, choose 1 climate suitable Country that you infect.
     * Place up to 2 Tokens in that Country
     */
    public static EventCard soapShortage(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState, list) -> {
            if(gameState.getPlayState() != PlayState.START_OF_TURN){
                throw new IllegalStateException("PlayState is not START_OF_TURN");
            }
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }

            //TODO: add aditional conditions
        };
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("soap_shortage", condition, event, List.of());
    }//end of soap_shortage


    /*
     * At the start of your turn, choose 1 hot country you are connected to.
     * Place uo to 2 tokens in it. (Ignore climate)
     */
    public static EventCard summerVacation(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState, list) -> {
            if(gameState.getPlayState() != PlayState.START_OF_TURN){
                throw new IllegalStateException("PlayState is not START_OF_TURN");
            }
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }

            //TODO: add aditional conditions
        };
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("summerVacation", condition, event, List.of());
    }//end of summerVacation


    /*
     * At the start of your turn, temporarily evolve a Trait Card without paying DNA or using an
     * Evolution Spave. Dicard it before you end your turn.
     */
    public static EventCard temporaryMutation(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState, list) -> {
            if(gameState.getPlayState() != PlayState.START_OF_TURN){
                throw new IllegalStateException("PlayState is not START_OF_TURN");
            }
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }

            //TODO: add aditional conditions
        };
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("temporary_mutation", condition, event, List.of());
    }//end of temporaryMutation


    /*
     * At the start of your turn, choose 1 Cold Country you are connected to. Place up to 2
     * Tokens in it. (Ignore Climate)
     */
    public static EventCard winterVacation(){
        //TODO: Implement condition & event
        GameStateAction condition = (plague, gameState, list) -> {
            if(gameState.getPlayState() != PlayState.START_OF_TURN){
                throw new IllegalStateException("PlayState is not START_OF_TURN");
            }
            if(!gameState.getCurrTurn().equals(plague)){
                throw new IllegalStateException("It is not this players turn");
            }

            //TODO: add aditional conditions
        };
        GameStateAction event = (plague, gameState, list) -> {};
        return new EventCard("winter_vacation", condition, event, List.of());
    }//end of winterVacation

    
}//end of event reference
