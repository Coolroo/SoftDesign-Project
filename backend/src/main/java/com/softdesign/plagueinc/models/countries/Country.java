package com.softdesign.plagueinc.models.countries;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.softdesign.plagueinc.exceptions.CityFullException;
import com.softdesign.plagueinc.exceptions.CountryFullException;
import com.softdesign.plagueinc.models.plague.Plague;
import com.softdesign.plagueinc.models.traits.restriction.RestrictionTrait;
import com.softdesign.plagueinc.models.traits.travel.TravelTrait;

import lombok.Getter;

@Getter
public class Country {

    private String countryName;

    private Continent continent;

    private Optional<RestrictionTrait> restriction;

    private List<TravelTrait> travelTypes;

    private Map<String, Optional<Plague>> cities;

    public Country(String countryName, Continent continent, Optional<RestrictionTrait> restrictionTrait, List<TravelTrait> travelTypes, Map<String, Optional<Plague>> cities){
        this.countryName = countryName;
        this.continent = continent;
        this.restriction = restrictionTrait;
        this.travelTypes = travelTypes;
        this.cities = cities;
    }

    public boolean hasRestriction(){
        return this.restriction.isPresent();
    }

    public boolean isFull(){
        return cities.values().stream().allMatch(optional -> optional.isPresent());
    }

    public Map<Plague, Long> getInfectionByPlayer(){
        return getCities().values().stream().filter(Optional::isPresent).map(Optional::get).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    public void infectCountry(Plague plague){
        Optional<String> city = getCities().keySet().stream().filter(cityName -> getCities().get(cityName).isEmpty()).findFirst();

        if(city.isEmpty()){
            throw new CountryFullException();
        }
        infectCity(plague, city.get());
    }

    public void infectCity(Plague plague, String city){
        if(!getCities().containsKey(city)){
            throw new IllegalArgumentException("Input was an invalid city");
        }

        if(getCities().get(city).isPresent()){
            throw new CityFullException();
        }

        if(plague.getPlagueTokens() == 0){
            throw new IllegalStateException("Plague " + plague.getPlayerId() + " cannot infect this city as they don't have any plague tokens left!");
        }

        getCities().put(city, Optional.of(plague));
        plague.placePlagueToken();
    }

    public List<Plague> getControllers(){
        Map<Plague, Long> infectionCount = getInfectionByPlayer();
        long max = infectionCount.values().stream().mapToLong(val -> val).max().getAsLong();
        return infectionCount.keySet().stream().filter(plague -> infectionCount.get(plague).longValue() == max).toList();
    }
}
