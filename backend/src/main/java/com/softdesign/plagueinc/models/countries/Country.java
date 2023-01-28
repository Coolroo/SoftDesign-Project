package com.softdesign.plagueinc.models.countries;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.softdesign.plagueinc.models.plague.Plague;
import com.softdesign.plagueinc.models.traits.restriction.RestrictionTrait;
import com.softdesign.plagueinc.models.traits.travel.TravelTrait;

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

    public String getCountryName(){ return countryName; }

    public Continent getContinent(){ return continent; }

    public List<TravelTrait> getTravelTypes(){
        return travelTypes;
    }

    public boolean hasRestriction(){
        return this.restriction.isPresent();
    }

    public Optional<RestrictionTrait> getRestriction(){ return restriction; }

    public Map<String, Optional<Plague>> getCities(){ return cities; }

    public boolean isFull(){
        return cities.values().stream().allMatch(optional -> optional.isPresent());
    }

    public Map<Plague, Long> getInfectionByPlayer(){
        return getCities().values().stream().filter(Optional::isPresent).map(Optional::get).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }
}
