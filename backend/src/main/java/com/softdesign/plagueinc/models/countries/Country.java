package com.softdesign.plagueinc.models.countries;

import java.util.Map;
import java.util.Optional;

import com.softdesign.plagueinc.models.plague.Plague;
import com.softdesign.plagueinc.models.traits.restriction.RestrictionTrait;

public class Country {

    private String countryName;

    private Continent continent;

    private Optional<RestrictionTrait> restriction;

    private Map<String, Optional<Plague>> cities;

    public Country(String countryName, Continent continent, Optional<RestrictionTrait> restrictionTrait, Map<String, Optional<Plague>> cities){
        this.countryName = countryName;
        this.continent = continent;
        this.restriction = restrictionTrait;
        this.cities = cities;
    }

    public String getCountryName(){ return countryName; }

    public Continent getContinent(){ return continent; }

    public boolean hasRestriction(){
        return this.restriction.isPresent();
    }

    public Optional<RestrictionTrait> getRestriction(){ return restriction; }

    public Map<String, Optional<Plague>> getCities(){ return cities; }
}
