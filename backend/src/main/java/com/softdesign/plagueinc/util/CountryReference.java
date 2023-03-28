package com.softdesign.plagueinc.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import com.softdesign.plagueinc.models.countries.Continent;
import com.softdesign.plagueinc.models.countries.Country;
import com.softdesign.plagueinc.models.plague.Plague;
import com.softdesign.plagueinc.models.traits.restriction.ColdTrait;
import com.softdesign.plagueinc.models.traits.restriction.HeatTrait;
import com.softdesign.plagueinc.models.traits.travel.AirborneTrait;
import com.softdesign.plagueinc.models.traits.travel.WaterborneTrait;

public class CountryReference {

    public static List<Country> getStartingCountries(){ 
        return List.of(argentina(),
        france(),
        japan(),
        southAfrica(),
        southKorea(),
        usa()); 
    }

    public static List<Country> getDefaultCountryDeck(){
        return List.of(turkey(),
        india(),
        germany(),
        uk(),
        iceland(),
        ukraine(),
        spain(),
        iran(),
        iraq(),
        russia(),
        china(),
        australia(),
        philippines(),
        libya(),
        colombia(),
        greenland(),
        canada(),
        poland(),
        brazil(),
        chile(),
        saudiArabia(),
        sweden(),
        sudan(),
        pakistan(),
        mongolia(),
        kazakhstan(),
        norway(),
        newZealand(),
        pNewGuinea(),
        peru(),
        bolivia(),
        ethiopia(),
        morocco(),
        egypt(),
        cuba(),
        mexico(),
        indonesia(),
        panama(),
        nigeria(),
        kenya(),
        madagascar(),
        venezuela());
    }

    //Starting Countries

    public static Country argentina(){
        return new Country("argentina",
        Continent.SOUTH_AMERICA,
        Optional.empty(),
        List.of(new WaterborneTrait()),
        citiesToMap(4));
    }

    public static Country france(){
        return new Country("france",
        Continent.EUROPE,
        Optional.empty(),
        List.of(new AirborneTrait()),
        citiesToMap(5));
    }

    public static Country japan(){
        return new Country("japan",
        Continent.ASIA,
        Optional.empty(),
        List.of(new WaterborneTrait(), new AirborneTrait()),
        citiesToMap(6));
    }

    public static Country southAfrica(){
        return new Country("south_africa",
        Continent.AFRICA,
        Optional.empty(),
        List.of(new AirborneTrait()),
        citiesToMap(5));
    }

    public static Country southKorea(){
        return new Country("south_korea",
        Continent.ASIA,
        Optional.empty(),
        List.of(new WaterborneTrait()),
        citiesToMap(5));
    }

    public static Country usa(){
        return new Country("usa",
        Continent.NORTH_AMERICA,
        Optional.empty(),
        List.of(new WaterborneTrait(), new AirborneTrait()),
        citiesToMap(7));
    }


    public static Country turkey(){
        return new Country("turkey",
        Continent.EUROPE,
        Optional.of(new HeatTrait()),
        List.of(),
        citiesToMap(5));
    }

    public static Country india(){
        return new Country("india",
        Continent.ASIA,
        Optional.of(new HeatTrait()),
        List.of(new WaterborneTrait()), 
        citiesToMap(8));
    }

    public static Country germany(){
        return new Country("germany", 
        Continent.EUROPE, 
        Optional.empty(), 
        List.of(new AirborneTrait(), new WaterborneTrait()), 
        citiesToMap(5));
    }

    public static Country uk(){
        return new Country("uk",
        Continent.EUROPE,
        Optional.empty(),
        List.of(new WaterborneTrait(), new AirborneTrait()),
        citiesToMap(4));
    }

    public static Country iceland(){
        return new Country("iceland",
        Continent.EUROPE,
        Optional.of(new ColdTrait()),
        List.of(new WaterborneTrait()),
        citiesToMap(2));
    }


    public static Country ukraine(){
        return new Country("ukraine",
        Continent.EUROPE,
        Optional.of(new ColdTrait()),
        List.of(),
        citiesToMap(4));
    }

    public static Country spain(){
        return new Country("spain",
        Continent.EUROPE,
        Optional.of(new HeatTrait()),
        List.of(),
        citiesToMap(4));
    }

    public static Country iran(){
        return new Country("iran",
        Continent.ASIA,
        Optional.empty(),
        List.of(),
        citiesToMap(4));
    }

    public static Country iraq(){
        return new Country("iraq",
        Continent.ASIA,
        Optional.of(new HeatTrait()),
        List.of(),
        citiesToMap(4));
    }

    public static Country russia(){
        return new Country("russia",
        Continent.ASIA,
        Optional.of(new ColdTrait()),
        List.of(new AirborneTrait()),
        citiesToMap(6));
    }

    public static Country china(){
        return new Country("china",
        Continent.ASIA,
        Optional.empty(),
        List.of(new AirborneTrait(), new WaterborneTrait()),
        citiesToMap(8));
    }

    public static Country australia(){
        return new Country("australia",
        Continent.OCEANIA,
        Optional.of(new HeatTrait()),
        List.of(new AirborneTrait()),
        citiesToMap(3));
    }

    public static Country philippines(){
        return new Country("philippines",
        Continent.OCEANIA,
        Optional.empty(),
        List.of(new WaterborneTrait()),
        citiesToMap(5));
    }

    public static Country libya(){
        return new Country("libya",
        Continent.AFRICA,
        Optional.empty(),
        List.of(),
        citiesToMap(2));
    }

    public static Country colombia(){
        return new Country("colombia",
        Continent.SOUTH_AMERICA,
        Optional.empty(),
        List.of(new AirborneTrait()),
        citiesToMap(4));
    }

    public static Country greenland(){
        return new Country("greenland", 
        Continent.NORTH_AMERICA, 
        Optional.of(new ColdTrait()), 
        List.of(), 
        citiesToMap(2));
    }

    public static Country canada(){
        return new Country("canada",
        Continent.NORTH_AMERICA,
        Optional.of(new ColdTrait()),
        List.of(new AirborneTrait()),
        citiesToMap(3));
    }

    public static Country poland(){
        return new Country("poland",
        Continent.EUROPE,
        Optional.empty(),
        List.of(),
        citiesToMap(4));
    }

    public static Country brazil(){
        return new Country("brazil",
        Continent.SOUTH_AMERICA,
        Optional.of(new HeatTrait()),
        List.of(new AirborneTrait(), new WaterborneTrait()),
        citiesToMap(7));
    }

    public static Country chile(){
        return new Country("chile",
        Continent.SOUTH_AMERICA,
        Optional.of(new ColdTrait()),
        List.of(new AirborneTrait()),
        citiesToMap(4));
    }

    public static Country saudiArabia(){
        return new Country("saudi_arabia",
        Continent.ASIA,
        Optional.of(new HeatTrait()),
        List.of(new AirborneTrait()),
        citiesToMap(4));
    }

    public static Country sweden(){
        return new Country("sweden",
        Continent.EUROPE,
        Optional.of(new ColdTrait()),
        List.of(),
        citiesToMap(3));
    }

    public static Country sudan(){
        return new Country("sudan",
        Continent.AFRICA,
        Optional.of(new HeatTrait()),
        List.of(),
        citiesToMap(4));
    }

    public static Country pakistan(){
        return new Country("pakistan",
        Continent.ASIA,
        Optional.empty(),
        List.of(),
        citiesToMap(6));
    }

    public static Country mongolia(){
        return new Country("mongolia",
        Continent.ASIA,
        Optional.empty(),
        List.of(),
        citiesToMap(3));
    }

    public static Country kazakhstan(){
        return new Country("kazakhstan",
        Continent.ASIA,
        Optional.of(new ColdTrait()),
        List.of(),
        citiesToMap(3));
    }

    public static Country norway(){
        return new Country("norway",
        Continent.EUROPE,
        Optional.of(new ColdTrait()),
        List.of(new WaterborneTrait()),
        citiesToMap(3));
    }

    public static Country newZealand(){
        return new Country("new_zealand",
        Continent.OCEANIA,
        Optional.empty(),
        List.of(new AirborneTrait()),
        citiesToMap(3));
    }

    public static Country pNewGuinea(){
        return new Country("p_new_guinea",
        Continent.OCEANIA,
        Optional.empty(),
        List.of(),
        citiesToMap(3));
    }

    public static Country peru(){
        return new Country("peru",
        Continent.SOUTH_AMERICA,
        Optional.empty(),
        List.of(new AirborneTrait()),
        citiesToMap(3));
    }

    public static Country bolivia(){
        return new Country("bolivia",
        Continent.SOUTH_AMERICA,
        Optional.empty(),
        List.of(),
        citiesToMap(3));
    }

    public static Country ethiopia(){
        return new Country("ethiopia",
        Continent.AFRICA,
        Optional.empty(),
        List.of(),
        citiesToMap(6));
    }

    public static Country morocco(){
        return new Country("morocco",
        Continent.AFRICA,
        Optional.empty(),
        List.of(new WaterborneTrait()),
        citiesToMap(3));
    }

    public static Country egypt(){
        return new Country("egypt",
        Continent.AFRICA,
        Optional.of(new HeatTrait()),
        List.of(new WaterborneTrait()),
        citiesToMap(5));
    }

    public static Country cuba(){
        return new Country("cuba",
        Continent.NORTH_AMERICA,
        Optional.empty(),
        List.of(new AirborneTrait()),
        citiesToMap(3));
    }

    public static Country mexico(){
        return new Country("mexico",
        Continent.NORTH_AMERICA,
        Optional.of(new HeatTrait()),
        List.of(new WaterborneTrait()),
        citiesToMap(6));
    }

    public static Country indonesia(){
        return new Country("indonesia",
        Continent.OCEANIA,
        Optional.empty(),
        List.of(new WaterborneTrait()),
        citiesToMap(7));
    }

    public static Country panama(){
        return new Country("panama",
        Continent.NORTH_AMERICA,
        Optional.empty(),
        List.of(new WaterborneTrait()),
        citiesToMap(2));
    }

    public static Country nigeria(){
        return new Country("nigeria",
        Continent.AFRICA,
        Optional.of(new HeatTrait()),
        List.of(),
        citiesToMap(6));
    }

    public static Country kenya(){
        return new Country("kenya",
        Continent.AFRICA,
        Optional.of(new HeatTrait()),
        List.of(),
        citiesToMap(5));
    }

    public static Country madagascar(){
        return new Country("madagascar",
        Continent.AFRICA,
        Optional.of(new HeatTrait()),
        List.of(),
        citiesToMap(3));
    }

    public static Country venezuela(){
        return new Country("venezuela",
        Continent.SOUTH_AMERICA,
        Optional.empty(),
        List.of(),
        citiesToMap(4));
    }


    private static List<Optional<Plague>> citiesToMap(int numCities){
        List<Optional<Plague>> cities = new ArrayList<>();
        IntStream.range(0, numCities).forEach(num -> cities.add(Optional.empty()));
        return cities;
    }
}
