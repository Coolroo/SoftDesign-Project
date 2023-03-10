package com.softdesign.plagueinc.managers;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.softdesign.plagueinc.managers.futures.input_types.CountryChoice;
import com.softdesign.plagueinc.models.countries.Continent;
import com.softdesign.plagueinc.models.countries.Country;
import com.softdesign.plagueinc.models.gamestate.GameState;
import com.softdesign.plagueinc.models.gamestate.PlayState;
import com.softdesign.plagueinc.models.plague.Plague;
import com.softdesign.plagueinc.models.plague.PlagueColor;

@SpringBootTest
public class GameStateManagerTest {

    @Autowired
    GameStateManager gameStateManager;

    @Test
    void testInfectFuture(){

        //init
        GameState gameState = new GameState();
        Plague plague = new Plague(PlagueColor.RED);
        gameState.setCurrTurn(plague);

        List<Optional<Plague>> cities =  new ArrayList<>(List.of(Optional.of(plague), 
        Optional.empty(), 
        Optional.empty(),
        Optional.empty()));

        Country country = new Country("null", Continent.AFRICA, Optional.empty(), List.of(), cities);
        HashMap<Continent, List<Country>> map = new HashMap<>(Map.of(Continent.AFRICA, new ArrayList<>(List.of(country))));
        gameState.setBoard(map);
        gameState.setPlayState(PlayState.EVOLVE);

        gameState.setReadyToProceed(true);
        
        //Testing

        gameState.proceedState();

        Assertions.assertThat(gameState.getPlayState()).isEqualTo(PlayState.INFECT);

        gameState.attemptInfect("null");

        //Make sure that country was infected, and that the game is not ready to proceed

        Assertions.assertThat(country.getInfectionByPlayer().get(plague)).isEqualTo(2);

        Assertions.assertThat(gameState.isReadyToProceed()).isFalse();

        gameState.attemptInfect("null");

        //After 2 infections, the player cannot place any more, and the game should be marked as ready to proceed

        Assertions.assertThat(country.getInfectionByPlayer().get(plague)).isEqualTo(3);

        Assertions.assertThat(gameState.isReadyToProceed()).isTrue();

        Assertions.assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() -> gameState.attemptInfect("null"));

    }

    @Test
    void testPlaceCountry(){
        String gameStateId = gameStateManager.createGame();
        GameState gameState = gameStateManager.getGameState(gameStateId);
        Plague plague = new Plague(PlagueColor.RED);
        gameState.setCurrTurn(plague);
        Country country = new Country("gobble", Continent.AFRICA, java.util.Optional.empty(), List.of(), List.of());
        gameState.setCountryDeck(new ArrayDeque<>(List.of(country)));

        Map<Continent, List<Country>> board = Stream.of(Continent.values()).collect(Collectors.toMap(Function.identity(), continent -> new ArrayList<>()));
        gameState.setBoard(board);

        gameState.setPlayState(PlayState.COUNTRY);

        //Now play the country
        gameState.makeCountryChoice("countrycardback", CountryChoice.PLAY);

        Assertions.assertThat(gameState.getBoard().get(Continent.AFRICA).size()).isGreaterThan(0);
    }
}
