package com.softdesign.plagueinc.models.plague.abilities;
import java.util.List;

import com.softdesign.plagueinc.models.countries.Country;
import com.softdesign.plagueinc.models.gamestate.GameStateAction;
import com.softdesign.plagueinc.models.gamestate.InputSelection;
import com.softdesign.plagueinc.models.gamestate.PlayState;
import com.softdesign.plagueinc.models.gamestate.selection_objects.CountrySelection;

public class Outbreak extends Ability {

    public Outbreak(GameStateAction condition, GameStateAction action) {
        super("outbreak", condition, action, List.of(InputSelection.COUNTRY, InputSelection.COUNTRY));
    }

    public static Ability create(){
        GameStateAction condition = (plague, gameState, list) -> {
            if(gameState.getPlayState() != PlayState.INFECT){
                throw new IllegalAccessError();
            }
        };

        GameStateAction action = (plague, gameState, list) -> {
            CountrySelection sourceSelection = ((CountrySelection)list.get(0));
            CountrySelection targetSelection = ((CountrySelection)list.get(1));

            Country sourceCountry = gameState.getCountry(sourceSelection.getCountryName());
            Country targetCountry = gameState.getCountry(targetSelection.getCountryName());

            if(targetCountry.isFull()){
                throw new IllegalArgumentException();
            }

            sourceCountry.removePlague(plague);
            targetCountry.infectCountry(plague);
        };
        return new Outbreak(condition, action);
    }
}
