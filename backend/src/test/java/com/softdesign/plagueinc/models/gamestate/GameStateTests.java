package com.softdesign.plagueinc.models.gamestate;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.softdesign.plagueinc.models.countries.Country;
import com.softdesign.plagueinc.util.CountryReference;

public class GameStateTests {
    
    static GameState gameState;

    @BeforeEach
    void setup(){
        gameState = new GameState();
    }

    @Test
    void testCountryInit(){


        MockedStatic<CountryReference> mockedReference = Mockito.mockStatic(CountryReference.class);
        
        mockedReference.when(CountryReference::getDefaultCountryDeck)
        .thenReturn(IntStream.range(0, 32)
        .mapToObj(val -> new Country("" + val, null, java.util.Optional.empty(), null, null)).toList());
        
        mockedReference.when(CountryReference::getCountriesByPlayer)
        .thenReturn(Map.of(0, 32));


        gameState.initCountryDeck(List.of());

        Assertions.assertThat(gameState.getCountryDeck()).isNotEqualTo(null);
        Assertions.assertThat(gameState.getCountryDeck().size()).isGreaterThan(0);
        Assertions.assertThat(gameState.getRevealedCountries().size()).isEqualTo(3);

        Assertions.assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() -> 
        {
            gameState.initCountryDeck(List.of());
        });
    }
}
