package com.softdesign.plagueinc.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.softdesign.plagueinc.models.countries.Continent;
import com.softdesign.plagueinc.models.countries.Country;
import com.softdesign.plagueinc.models.gamestate.GameState;
import com.softdesign.plagueinc.models.gamestate.PlayState;
import com.softdesign.plagueinc.models.plague.Plague;

@SpringBootTest
public class GameStateManagerTest {
    
    @Autowired
    GameStateManager gameStateManager;


    @Test
    void testInfectFuture(){
        GameState gameState = new GameState();
        gameStateManager.setGameState(gameState);
        Plague plague = new Plague();
        gameState.setCurrTurn(plague);

        Map<String, Optional<Plague>> cities =  new HashMap<>(Map.of("jeffistan", Optional.of(plague), 
        "sus", Optional.empty(), 
        "tata", Optional.empty(),
        "doda", Optional.empty()));

        Country country = new Country("null", Continent.AFRICA, Optional.empty(), List.of(), cities);
        HashMap<Continent, List<Country>> map = new HashMap<>(Map.of(Continent.AFRICA, new ArrayList<>(List.of(country))));
        gameState.setBoard(map);
        gameState.setPlayState(PlayState.EVOLVE);

        gameState.setReadyToProceed(true);
        System.out.println("BEGIN TESTS");
        System.out.println(gameState);

        gameStateManager.proceedState();

        Assertions.assertThat(gameState.getPlayState()).isEqualTo(PlayState.INFECT);

        gameStateManager.attemptInfect("null");

        Assertions.assertThat(country.getInfectionByPlayer().get(plague)).isEqualTo(2);

        Assertions.assertThat(gameState.getReadyToProceed()).isFalse();

        gameStateManager.attemptInfect("null");

        Assertions.assertThat(country.getInfectionByPlayer().get(plague)).isEqualTo(3);

        Assertions.assertThat(gameState.getReadyToProceed()).isTrue();


        

        


    }
}
