package com.softdesign.plagueinc.models.gamestate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.softdesign.plagueinc.models.countries.Continent;
import com.softdesign.plagueinc.models.countries.Country;
import com.softdesign.plagueinc.models.plague.Plague;
import com.softdesign.plagueinc.models.plague.PlagueColor;

@SpringBootTest
public class GameStateTest {

    @BeforeAll
    static void setUp() {
        MockedStatic<ThreadLocalRandom> randomMock = Mockito.mockStatic(ThreadLocalRandom.class);
        randomMock.when(() -> ThreadLocalRandom.current()).thenReturn(Mockito.mock(ThreadLocalRandom.class));

        Mockito.when(ThreadLocalRandom.current().nextInt(Mockito.anyInt(), Mockito.anyInt())).thenReturn(0);
        
    }

    @Test
    void testStartGame() {
        GameState gameState = new GameState();

        // Make sure gameState starts in the initialization state
        Assertions.assertThat(gameState.getPlayState()).isEqualTo(PlayState.INITIALIZATION);

        // Make sure we can't start the game using an invalid UUID
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> gameState.startGame(null));

        // Add some plagues
        UUID playerId1 = gameState.joinGame(PlagueColor.BLUE);
        UUID playerId2 = gameState.joinGame(PlagueColor.RED);
        UUID playerId3 = gameState.joinGame(PlagueColor.ORANGE);
        UUID playerId4 = gameState.joinGame(PlagueColor.PURPLE);

        // Make sure we can't start the game unless all players have voted to start //

        gameState.startGame(playerId1);
        Assertions.assertThat(gameState.getPlayState()).isEqualTo(PlayState.INITIALIZATION);

        gameState.startGame(playerId2);
        Assertions.assertThat(gameState.getPlayState()).isEqualTo(PlayState.INITIALIZATION);
        
        gameState.startGame(playerId3);
        Assertions.assertThat(gameState.getPlayState()).isEqualTo(PlayState.INITIALIZATION);

        // Make sure we can't start the game using an invalid UUID with existing players
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> gameState.startGame(null));

        // Make sure the first turn has started
        gameState.startGame(playerId4);
        Assertions.assertThat(gameState.getPlayState()).isEqualTo(PlayState.START_OF_TURN);

        // Make sure players have been given the right amount of DNA points
        Assertions.assertThat(gameState.getPlagues().get(0).getDnaPoints()).isEqualTo(0);
        Assertions.assertThat(gameState.getPlagues().get(1).getDnaPoints()).isEqualTo(1);
        Assertions.assertThat(gameState.getPlagues().get(2).getDnaPoints()).isEqualTo(2);
        Assertions.assertThat(gameState.getPlagues().get(3).getDnaPoints()).isEqualTo(3);

        // Make sure the game starts with a single infection per player

        Long infections = 0L;
        for (List<Country> countries : gameState.getBoard().values()) 
            for (Country country : countries) 
                for (Long infectCount : country.getInfectionByPlayer().values()) 
                    infections += infectCount;

        Assertions.assertThat(infections).isEqualTo(4);

        // Make sure we can't start the game after it has already been started
        Assertions.assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() -> gameState.startGame(playerId1));

        // Make sure the game state is ready to proceed
        Assertions.assertThat(gameState.isReadyToProceed()).isTrue();
    }

    @Test
    void testJoinGame() {
        GameState gameState = new GameState();

        // Make sure gameState starts in the initialization state
        Assertions.assertThat(gameState.getPlayState()).isEqualTo(PlayState.INITIALIZATION);

        // Add the first plague
        gameState.joinGame(PlagueColor.BLUE);

        // Make sure a single plague now exists
        Assertions.assertThat(gameState.getPlagues().size()).isEqualTo(1);

        // Make sure we can't add another plague with the same colour
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> gameState.joinGame(PlagueColor.BLUE));

        // Repeat for more plagues

        gameState.joinGame(PlagueColor.ORANGE);
        gameState.joinGame(PlagueColor.PURPLE);
        gameState.joinGame(PlagueColor.RED);

        Assertions.assertThat(gameState.getPlagues().size()).isEqualTo(4);

        // Make sure we can't exceed the plague count limit
        Assertions.assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() -> gameState.joinGame(PlagueColor.YELLOW));


        // Make sure we can't add plagues outside of the intialization state
        
        GameState gameState2 = new GameState();

        gameState2.setPlayState(PlayState.START_OF_TURN);

        Assertions.assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() -> gameState2.joinGame(PlagueColor.ORANGE));
    }

    @Test
    void testScoreDNAPoints() {
        // Declarations //

        GameState gameState = new GameState();
        Plague plague1, plague2, plague3;
        List<Optional<Plague>> cities;
        Country country;
        HashMap<Continent, List<Country>> map;

        gameState.setPlayState(PlayState.DNA);

        // Init Case 1 //
        
        plague1 = new Plague(PlagueColor.RED);
        gameState.setCurrTurn(plague1);

        cities =  new ArrayList<>(List.of(
            Optional.of(plague1), 
            Optional.empty(), 
            Optional.empty(),
            Optional.empty()
        ));

        country = new Country("null", Continent.AFRICA, Optional.empty(), List.of(), cities);
        map = new HashMap<>(Map.of(Continent.AFRICA, new ArrayList<>(List.of(country))));
        gameState.setBoard(map);

        // Run Case 1 //

        gameState.scoreDNAPoints();
        Assertions.assertThat(plague1.getDnaPoints()).isEqualTo(1);

        // Make sure we can't score DNA points more than once
        Assertions.assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() -> gameState.scoreDNAPoints());

        // Init Case 2 //

        gameState.setReadyToProceed(false);

        plague1 = new Plague(PlagueColor.RED);
        plague2 = new Plague(PlagueColor.BLUE);

        cities =  new ArrayList<>(List.of(
            Optional.of(plague1), 
            Optional.of(plague2), 
            Optional.of(plague2),
            Optional.empty()
        ));

        country = new Country("null", Continent.AFRICA, Optional.empty(), List.of(), cities);
        map = new HashMap<>(Map.of(Continent.AFRICA, new ArrayList<>(List.of(country))));
        gameState.setBoard(map);

        // Run Case 2 //

        // Score for first plague
        gameState.setCurrTurn(plague1);
        gameState.scoreDNAPoints();

        // Reset turn
        gameState.setReadyToProceed(false);

        // Score for second plague
        gameState.setCurrTurn(plague2);
        gameState.scoreDNAPoints();

        // Check scores
        Assertions.assertThat(plague1.getDnaPoints()).isEqualTo(0);
        Assertions.assertThat(plague2.getDnaPoints()).isEqualTo(1);

        // Init Case 3 //

        gameState.setReadyToProceed(false);

        plague1 = new Plague(PlagueColor.RED);
        plague2 = new Plague(PlagueColor.BLUE);
        plague3 = new Plague(PlagueColor.YELLOW);

        cities =  new ArrayList<>(List.of(
            Optional.of(plague1), 
            Optional.of(plague2), 
            Optional.of(plague3),
            Optional.empty()
        ));

        country = new Country("null", Continent.AFRICA, Optional.empty(), List.of(), cities);
        map = new HashMap<>(Map.of(Continent.AFRICA, new ArrayList<>(List.of(country))));
        gameState.setBoard(map);

        // Run Case 3 //

        // Score for first plague
        gameState.setCurrTurn(plague1);
        gameState.scoreDNAPoints();

        // Reset turn
        gameState.setReadyToProceed(false);

        // Score for second plague
        gameState.setCurrTurn(plague2);
        gameState.scoreDNAPoints();

        // Reset turn
        gameState.setReadyToProceed(false);

        // Score for third plague
        gameState.setCurrTurn(plague3);
        gameState.scoreDNAPoints();

        // Check scores
        Assertions.assertThat(plague1.getDnaPoints()).isEqualTo(1);
        Assertions.assertThat(plague2.getDnaPoints()).isEqualTo(1);
        Assertions.assertThat(plague3.getDnaPoints()).isEqualTo(1);

        // Init Case 4 //
        // Multi-Country Case

        gameState.setReadyToProceed(false);

        plague1 = new Plague(PlagueColor.RED);
        plague2 = new Plague(PlagueColor.BLUE);
        plague3 = new Plague(PlagueColor.YELLOW);

        // First Country
        cities =  new ArrayList<>(List.of(
            Optional.of(plague1), 
            Optional.of(plague1), 
            Optional.of(plague2),
            Optional.of(plague2)
        ));

        country = new Country("null1", Continent.AFRICA, Optional.empty(), List.of(), cities);

        // Second Country
        cities =  new ArrayList<>(List.of(
            Optional.of(plague2), 
            Optional.of(plague2), 
            Optional.of(plague3),
            Optional.of(plague3)
        ));

        Country country2 = new Country("null2", Continent.ASIA, Optional.empty(), List.of(), cities);

        map = new HashMap<>(Map.of(Continent.AFRICA, new ArrayList<>(List.of(country, country2))));
        gameState.setBoard(map);

        // Run Case 4 //

        // Score for first plague
        gameState.setCurrTurn(plague1);
        gameState.scoreDNAPoints();

        // Reset turn
        gameState.setReadyToProceed(false);

        // Score for second plague
        gameState.setCurrTurn(plague2);
        gameState.scoreDNAPoints();

        // Reset turn
        gameState.setReadyToProceed(false);

        // Score for third plague
        gameState.setCurrTurn(plague3);
        gameState.scoreDNAPoints();

        // Check scores
        Assertions.assertThat(plague1.getDnaPoints()).isEqualTo(1);
        Assertions.assertThat(plague2.getDnaPoints()).isEqualTo(2);
        Assertions.assertThat(plague3.getDnaPoints()).isEqualTo(1);
    }

    @Test
    void testAttemptInfect(){

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
    void testKillingCountry() {
        // Declarations //

        GameState gameState;
        UUID playerId;
        Plague plague1, plague2;
        List<Optional<Plague>> cities;
        Country country;
        HashMap<Continent, List<Country>> map;
        int oldDNA1, oldDNA2;

        // Init Case 1 //

        gameState = new GameState();
        
        playerId = gameState.joinGame(PlagueColor.RED);
        plague1 = gameState.getPlagues().get(0);
        gameState.startGame(playerId);

        playerId = gameState.joinGame(PlagueColor.BLUE);
        plague2 = gameState.getPlagues().get(1);
        gameState.startGame(playerId);

        gameState.setCurrTurn(plague1);

        // Refund initial token placement
        plague1.returnPlagueTokens(1);
        plague2.returnPlagueTokens(1);

        // checks if plague starts with extra DNA because of turn order
        oldDNA1 = plague1.getDnaPoints();

        cities =  new ArrayList<>(List.of(
            Optional.of(plague1), 
            Optional.of(plague1), 
            Optional.of(plague1),
            Optional.of(plague1)
        ));

        // Decrement token count accordingly
        plague1.placePlagueToken();
        plague1.placePlagueToken();
        plague1.placePlagueToken();
        plague1.placePlagueToken();

        country = new Country("name", Continent.AFRICA, Optional.empty(), List.of(), cities);
        map = new HashMap<>(Map.of(Continent.AFRICA, new ArrayList<>(List.of(country))));
        gameState.setBoard(map);

        // Advance to death state to implicitly call initDeathPhase
        gameState.setPlayState(PlayState.INFECT);
        gameState.setReadyToProceed(true);
        gameState.proceedState();

        // Run Case 1 //

        Assertions.assertThat(plague1.getPlagueTokens()).isEqualTo(12);
        Assertions.assertThat(gameState.getBoard().get(Continent.AFRICA).size()).isEqualTo(1);

        // Call the future directly to avoid the random dice roll
        gameState.rollDeathDice("name");

        Assertions.assertThat(plague1.getDnaPoints()).isEqualTo(4 + oldDNA1);
        Assertions.assertThat(plague1.getPlagueTokens()).isEqualTo(16);
        Assertions.assertThat(plague1.getKilledCountries().size()).isEqualTo(1);

        Assertions.assertThat(gameState.getBoard().get(Continent.AFRICA).size()).isEqualTo(0);

        // Make sure we can't try to kill if the game is ready to proceed
        GameState tmp1 = gameState;
        Assertions.assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() -> tmp1.rollDeathDice("name"));


        // Init Case 2 //

        gameState = new GameState();
        
        playerId = gameState.joinGame(PlagueColor.RED);
        plague1 = gameState.getPlagues().get(0);
        gameState.startGame(playerId);

        playerId = gameState.joinGame(PlagueColor.BLUE);
        plague2 = gameState.getPlagues().get(1);
        gameState.startGame(playerId);

        gameState.setCurrTurn(plague1);

        // Refund initial token placement
        plague1.returnPlagueTokens(1);
        plague2.returnPlagueTokens(1);

        // checks if plague starts with extra DNA because of turn order
        oldDNA1 = plague1.getDnaPoints();
        oldDNA2 = plague2.getDnaPoints();

        cities =  new ArrayList<>(List.of(
            Optional.of(plague1), 
            Optional.of(plague1), 
            Optional.of(plague1),
            Optional.of(plague2)
        ));

        // Decrement token count accordingly
        plague1.placePlagueToken();
        plague1.placePlagueToken();
        plague1.placePlagueToken();
        
        plague2.placePlagueToken();

        country = new Country("name", Continent.AFRICA, Optional.empty(), List.of(), cities);
        map = new HashMap<>(Map.of(Continent.AFRICA, new ArrayList<>(List.of(country))));
        gameState.setBoard(map);

        // Advance to death state to implicitly call initDeathPhase
        gameState.setPlayState(PlayState.INFECT);
        gameState.setReadyToProceed(true);
        gameState.proceedState();

        // Run Case 2 //

        Assertions.assertThat(plague1.getPlagueTokens()).isEqualTo(13);
        Assertions.assertThat(plague2.getPlagueTokens()).isEqualTo(15);
        Assertions.assertThat(gameState.getBoard().get(Continent.AFRICA).size()).isEqualTo(1);

        // Call the future directly to avoid the random dice roll
        gameState.rollDeathDice("name");

        Assertions.assertThat(plague1.getDnaPoints()).isEqualTo(3 + oldDNA1);
        Assertions.assertThat(plague1.getPlagueTokens()).isEqualTo(16);
        Assertions.assertThat(plague1.getKilledCountries().size()).isEqualTo(1);

        Assertions.assertThat(plague2.getDnaPoints()).isEqualTo(1 + oldDNA2);
        Assertions.assertThat(plague2.getPlagueTokens()).isEqualTo(16);
        Assertions.assertThat(plague2.getKilledCountries().size()).isEqualTo(0);

        Assertions.assertThat(gameState.getBoard().get(Continent.AFRICA).size()).isEqualTo(0);

        // Make sure we can't try to kill if the game is ready to proceed
        GameState tmp2 = gameState;
        Assertions.assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() -> tmp2.rollDeathDice("name"));
    }
}
