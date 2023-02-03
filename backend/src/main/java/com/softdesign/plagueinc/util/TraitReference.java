package com.softdesign.plagueinc.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import com.softdesign.plagueinc.models.traits.TraitCard;
import com.softdesign.plagueinc.models.traits.disease.InfectivityTrait;
import com.softdesign.plagueinc.models.traits.restriction.ColdTrait;
import com.softdesign.plagueinc.models.traits.restriction.HeatTrait;
import com.softdesign.plagueinc.models.traits.travel.AirborneTrait;
import com.softdesign.plagueinc.models.traits.travel.WaterborneTrait;

public class TraitReference {

    public static List<TraitCard> getDefaultTraitDeck(){
        //TODO: Implement default trait deck
        ArrayList<TraitCard> deck = new ArrayList<>();
        IntStream.range(0,10).forEach(val -> {
            deck.add(paranoia());
            deck.add(pustules());
            deck.add(coldResistance());
            deck.add(diarrhoea());
            deck.add(waterTransmission());
        });
        return deck;
    }

    public static TraitCard paranoia(){
        return new TraitCard("paranoia", 12, List.of(new InfectivityTrait(), new InfectivityTrait()));
    }

    public static TraitCard pustules(){
        return new TraitCard("pustules", 12, List.of(new AirborneTrait(), new HeatTrait()));
    }

    public static TraitCard coldResistance(){
        return new TraitCard("cold_resistance", 4, List.of(new ColdTrait()));
    }

    public static TraitCard diarrhoea(){
        return new TraitCard("diarrhoea", 13, List.of(new HeatTrait(), new InfectivityTrait()));
    }

    public static TraitCard waterTransmission(){
        return new TraitCard("water_transmission", 2, List.of(new WaterborneTrait()));
    }
}
