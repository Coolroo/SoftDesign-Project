package com.softdesign.plagueinc.models.countries;

import java.util.Optional;

import com.softdesign.plagueinc.models.traits.restriction.RestrictionTrait;

public class Country {

    private String countryName;

    private Continent continent;

    private Optional<RestrictionTrait> restriction;
}
