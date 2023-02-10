package com.softdesign.plagueinc.models.countries;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.softdesign.plagueinc.exceptions.CityFullException;
import com.softdesign.plagueinc.exceptions.CountryFullException;
import com.softdesign.plagueinc.models.plague.Plague;
import com.softdesign.plagueinc.models.serializers.CountrySerializers.CitySerializer;
import com.softdesign.plagueinc.models.traits.restriction.RestrictionTrait;
import com.softdesign.plagueinc.models.traits.travel.TravelTrait;

import lombok.Getter;

@Getter
public class Country {

    @JsonIgnore
    Logger logger = LoggerFactory.getLogger(Country.class);
    
    private String countryName;

    @JsonIgnore
    private Continent continent;

    @JsonIgnore
    private Optional<RestrictionTrait> restriction;

    @JsonIgnore
    private List<TravelTrait> travelTypes;

    @JsonSerialize(using = CitySerializer.class)
    private List<Optional<Plague>> cities;

    public Country(String countryName, Continent continent, Optional<RestrictionTrait> restrictionTrait, List<TravelTrait> travelTypes, List<Optional<Plague>> cities){
        this.countryName = countryName;
        this.continent = continent;
        this.restriction = restrictionTrait;
        this.travelTypes = travelTypes;
        this.cities = cities;
    }

    public boolean hasRestriction(){
        return this.restriction.isPresent();
    }

    @JsonIgnore
    public boolean isFull(){
        return cities.stream().allMatch(optional -> optional.isPresent());
    }

    @JsonIgnore
    public Map<Plague, Long> getInfectionByPlayer(){
        return getCities().stream().filter(Optional::isPresent).map(Optional::get).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    public void infectCountry(Plague plague){
        int city = getCities().indexOf(Optional.empty());

        if(city == -1){
            throw new CountryFullException();
        }
        infectCity(plague, city);
    }

    public void infectCity(Plague plague, int city){

        if(getCities().get(city).isPresent()){
            throw new CityFullException();
        }

        if(plague.getPlagueTokens() == 0){
            logger.warn("(Plague {}) cannot infect this city as they have no tokens left!", plague.getColor());
            throw new IllegalStateException("Plague " + plague.getPlayerId() + " cannot infect this city as they don't have any plague tokens left!");
        }

        getCities().set(city, Optional.of(plague));
        plague.placePlagueToken();
    }

    @JsonIgnore
    public List<Plague> getControllers(){
        Map<Plague, Long> infectionCount = getInfectionByPlayer();
        long max = infectionCount.values().stream().mapToLong(val -> val).max().getAsLong();
        return infectionCount.keySet().stream().filter(plague -> infectionCount.get(plague).longValue() == max).toList();
    }
}
