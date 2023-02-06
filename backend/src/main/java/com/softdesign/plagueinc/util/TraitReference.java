package com.softdesign.plagueinc.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import com.softdesign.plagueinc.models.traits.TraitCard;
import com.softdesign.plagueinc.models.traits.disease.InfectivityTrait;
import com.softdesign.plagueinc.models.traits.disease.LethalityTrait;
import com.softdesign.plagueinc.models.traits.restriction.ColdTrait;
import com.softdesign.plagueinc.models.traits.restriction.HeatTrait;
import com.softdesign.plagueinc.models.traits.travel.AirborneTrait;
import com.softdesign.plagueinc.models.traits.travel.WaterborneTrait;

public class TraitReference {

    public static List<TraitCard> getDefaultTraitDeck(){
        //TODO: Implement default trait deck
        ArrayList<TraitCard> deck = new ArrayList<>();
            deck.add(coldResistance());
            deck.add(psychosis());
            deck.add(paranoia());
            deck.add(heatResistance());
            deck.add(waterTransmission());
            deck.add(delirium());
            deck.add(insanity());
            deck.add(necrosis());
            deck.add(airTransmission());
            deck.add(rash());
            deck.add(coma());
            deck.add(meningitis());
            deck.add(skinLesions());
            deck.add(diarrhoea());
            deck.add(abcesses());
            deck.add(haemorrhagicShock());
            deck.add(pustules());
            deck.add(noseBleed());
            deck.add(cannibalism());
            deck.add(soreThroat());
            deck.add(gastricUlceration());
            deck.add(hallucinations());
            deck.add(headache());
            deck.add(paralysis());
            deck.add(cysts());
            deck.add(hypothermia());
            deck.add(fever());
            deck.add(insomnia());
            deck.add(tumours());
            deck.add(vomiting());
            deck.add(dysentery());
            deck.add(coughing());
            deck.add(internalHaemorrhaging());
            deck.add(totalOrganFailure());
            deck.add(respitoryFailure());
            deck.add(buboes());
            deck.add(sneezing());
            deck.add(systemicInfection());
            deck.add(seizures());
            deck.add(sweating());
            deck.add(pneumonia());
            deck.add(confusion());
        return deck;
    }

    public static TraitCard coldResistance(){
        return new TraitCard("cold_resistance", 4, List.of(new ColdTrait()));
    }

    public static TraitCard psychosis(){
        return new TraitCard("psychosis",2,List.of(new LethalityTrait())); 
    }

    public static TraitCard paranoia(){
        return new TraitCard("paranoia", 12, List.of(new InfectivityTrait(), new InfectivityTrait()));
    }

    public static TraitCard heatResistance(){
        return new TraitCard("heat_resistance", 5, List.of(new HeatTrait()));
    }

    public static TraitCard waterTransmission(){
        return new TraitCard("water_transmission", 2, List.of(new WaterborneTrait()));
    }

    public static TraitCard delirium(){
        return new TraitCard("delirium", 12, List.of(new WaterborneTrait(), new HeatTrait()));
    }

    public static TraitCard insanity(){
        return new TraitCard("insanity", 19, List.of(new InfectivityTrait(), new InfectivityTrait(), new LethalityTrait()));
    }

    public static TraitCard necrosis(){
        return new TraitCard("necrosis", 20, List.of(new InfectivityTrait(), new LethalityTrait(), new LethalityTrait()));
    }

    public static TraitCard airTransmission(){
        return new TraitCard("air_transmission", 2, List.of(new AirborneTrait()));
    }

    public static TraitCard rash(){
        return new TraitCard("rash", 2, List.of(new InfectivityTrait()));
    }

    public static TraitCard coma(){
        return new TraitCard("coma", 13, List.of(new LethalityTrait(), new LethalityTrait()));
    }

    public static TraitCard meningitis(){
        return new TraitCard("meningitis",11, List.of(new InfectivityTrait(), new LethalityTrait()));
    }

    public static TraitCard skinLesions(){
        return new TraitCard("skin_legions", 12, List.of(new InfectivityTrait(), new InfectivityTrait()));
    }

    public static TraitCard diarrhoea(){
        return new TraitCard("diarrhoea", 13, List.of(new HeatTrait(), new InfectivityTrait()));
    }

    public static TraitCard abcesses(){
        return new TraitCard("abcesses", 2, List.of(new LethalityTrait()));
    }

    public static TraitCard haemorrhagicShock(){
        return new TraitCard("haemorrhagic_shock", 20, List.of(new InfectivityTrait(), new LethalityTrait(), new LethalityTrait()));
    }

    public static TraitCard pustules(){
        return new TraitCard("pustules", 12, List.of(new AirborneTrait(), new HeatTrait()));
    }

    public static TraitCard noseBleed(){
        return new TraitCard("nose_bleed",2, List.of(new InfectivityTrait()));
    }

    public static TraitCard cannibalism(){
        return new TraitCard("cannibalism", 2, List.of(new LethalityTrait()));
    }

    public static TraitCard soreThroat(){
        return new TraitCard("sore_throat", 2, List.of(new InfectivityTrait()));
    }

    public static TraitCard gastricUlceration(){
        return new TraitCard("gastric_ulceration", 2, List.of(new LethalityTrait()));
    }

    public static TraitCard hallucinations(){
        return new TraitCard("hallucinations", 2, List.of(new InfectivityTrait()));
    }

    public static TraitCard headache(){
        return new TraitCard("headache", 2, List.of(new InfectivityTrait()));
    }

    public static TraitCard paralysis(){
        return new TraitCard("paralysis", 2, List.of(new LethalityTrait()));
    }

    public static TraitCard cysts(){
        return new TraitCard("cysts", 2, List.of(new InfectivityTrait()));
    }

    public static TraitCard hypothermia(){
        return new TraitCard("hypothermia", 12, List.of(new ColdTrait(), new LethalityTrait()));
    }

    public static TraitCard fever(){
        return new TraitCard("fever", 2, List.of(new LethalityTrait()));
    }

    public static TraitCard insomnia(){
        return new TraitCard("insomnia", 2, List.of(new InfectivityTrait()));
    }

    public static TraitCard tumours(){
        return new TraitCard("tumours", 13, List.of(new LethalityTrait(), new LethalityTrait()));
    }

    public static TraitCard vomiting(){
        return new TraitCard("vomiting", 10, List.of(new WaterborneTrait(), new InfectivityTrait()));
    }

    public static TraitCard dysentery(){
        return new TraitCard("dysentery", 13, List.of(new HeatTrait(), new LethalityTrait()));
    }

    public static TraitCard coughing(){
        return new TraitCard("coughing", 2, List.of(new InfectivityTrait()));
    }

    public static TraitCard internalHaemorrhaging(){
        return new TraitCard("internal_haemorrhaging", 11, List.of(new InfectivityTrait(), new LethalityTrait()));
    }

    public static TraitCard totalOrganFailure(){
        return new TraitCard("total_organ_failure", 21, List.of(new LethalityTrait(), new LethalityTrait(), new LethalityTrait()));
    }

    public static TraitCard respitoryFailure(){
        return new TraitCard("respitory_failure", 2, List.of(new LethalityTrait()));
    }

    public static TraitCard buboes(){
        return new TraitCard("buboes", 11, List.of(new WaterborneTrait(), new ColdTrait()));
    }

    public static TraitCard sneezing(){
        return new TraitCard("sneezing", 10, List.of(new AirborneTrait(), new InfectivityTrait()));
    }

    public static TraitCard systemicInfection(){
        return new TraitCard("systemic_infection", 11, List.of(new InfectivityTrait(), new LethalityTrait()));
    }

    public static TraitCard seizures(){
        return new TraitCard("seizures", 2, List.of(new LethalityTrait()));
    }

    public static TraitCard sweating(){
        return new TraitCard("sweating", 2, List.of(new InfectivityTrait()));
    }

    public static TraitCard pneumonia(){
        return new TraitCard("pneumonia", 12, List.of(new ColdTrait(), new InfectivityTrait()));
    }

    public static TraitCard confusion(){
        return new TraitCard("confusion", 11, List.of(new AirborneTrait(), new ColdTrait()));
    }
 


}
