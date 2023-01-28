package com.softdesign.plagueinc.managers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.softdesign.plagueinc.exceptions.CityFullException;
import com.softdesign.plagueinc.exceptions.CountryFullException;
import com.softdesign.plagueinc.models.countries.Country;
import com.softdesign.plagueinc.models.plague.Plague;

public class CountryManager {
    
    public void infectCountry(Country country, Plague plague){
        Optional<String> city = country.getCities().keySet().stream().filter(cityName -> country.getCities().get(cityName).isEmpty()).findFirst();

        if(city.isEmpty()){
            throw new CountryFullException();
        }
        infectCity(country, plague, city.get());
    }

    public void infectCity(Country country, Plague plague, String city){
        if(!country.getCities().containsKey(city)){
            throw new IllegalArgumentException("Input was an invalid city");
        }

        if(country.getCities().get(city).isPresent()){
            throw new CityFullException();
        }

        if(plague.getPlagueTokens() == 0){
            throw new IllegalStateException("Plague " + plague.getPlayerId() + " cannot infect this city as they don't have any plague tokens left!");
        }

        country.getCities().put(city, Optional.of(plague));
        plague.placePlagueToken();
    }

    public List<Plague> getControllers(Country country){
        Map<Plague, Long> infectionCount = country.getInfectionByPlayer();
        long max = infectionCount.values().stream().mapToLong(val -> val).max().getAsLong();
        return infectionCount.keySet().stream().filter(plague -> infectionCount.get(plague).longValue() == max).toList();
    }
}
