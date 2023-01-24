package com.softdesign.plagueinc.util;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.softdesign.plagueinc.models.countries.Continent;
import com.softdesign.plagueinc.models.countries.Country;
import com.softdesign.plagueinc.models.plague.Plague;
import com.softdesign.plagueinc.models.traits.restriction.ColdTrait;
import com.softdesign.plagueinc.models.traits.travel.AirborneTrait;
import com.softdesign.plagueinc.models.traits.travel.WaterborneTrait;

public class CountryReference {
    public static Country germany(){
        return new Country("germany", 
        Continent.EUROPE, 
        Optional.empty(), 
        List.of(new AirborneTrait(), new WaterborneTrait()), 
        citiesToMap(List.of("berlin",  "hamburg",  "munich",  "cologne",  "frankfurt")));
    }

    public static Country greenland(){
        return new Country("greenland", Continent.NORTH_AMERICA, Optional.of(new ColdTrait()), List.of(), citiesToMap(List.of("nuuk", "sisimiut")));
    }

    public static Country poland(){
        return new Country("poland",
        Continent.EUROPE,
        Optional.empty(),
        List.of(),
        citiesToMap(List.of("warsaw", "krakow", "lodz", "wroclaw")));
    }

    private static Map<String, Optional<Plague>> citiesToMap(List<String> cities){
        return cities.stream().collect(Collectors.toMap(Function.identity(), city -> Optional.empty()));
    }
}
