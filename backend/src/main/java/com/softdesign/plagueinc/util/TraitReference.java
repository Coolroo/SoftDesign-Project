package com.softdesign.plagueinc.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import com.softdesign.plagueinc.models.traits.TraitCard;
import com.softdesign.plagueinc.models.traits.disease.InfectivityTrait;
import com.softdesign.plagueinc.models.traits.disease.LethalityTrait;
import com.softdesign.plagueinc.models.traits.restriction.ColdTrait;
import com.softdesign.plagueinc.models.traits.restriction.HeatTrait;
import com.softdesign.plagueinc.models.traits.travel.AirborneTrait;
import com.softdesign.plagueinc.models.traits.travel.WaterborneTrait;

public class TraitReference {

    private static final Map<String, Integer> idToCount = new HashMap<>(){{
        put("heat_resistance", 6); 
        put("cold_resistance", 5);
        put("sweating", 1);
        put("meningitis", 1); 
        put("confusion", 1); 
        put("dysentery", 1); 
        put("coma", 1); 
        put("insanity", 1); 
        put("pustules", 1); 
        put("delirium", 1); 
        put("psychosis", 1); 
        put("coughing", 1); 
        put("blindness", 1); 
        put("paranoia", 1); 
        put("headache", 1); 
        put("hallucinations", 1); 
        put("nose_bleed", 1); 
        put("cysts", 1); 
        put("insomnia", 1); 
        put("gastric_ulceration", 1); 
        put("hypothermia", 1); 
        put("respiratory_failure", 1); 
        put("rash", 1); 
        put("pneumonia", 1); 
        put("paralysis", 1); 
        put("seizures", 1); 
        put("tumours", 1); 
        put("fever", 1); 
        put("cannibalism", 1); 
        put("skin_lesions", 1); 
        put("necrosis", 1); 
        put("sneezing", 1); 
        put("total_organ_failure", 1); 
        put("sore_throat", 1); 
        put("air_transmission", 6); 
        put("water_transmission", 6); 
        put("vomiting", 1); 
        put("haemorrhagic_shock", 1); 
        put("systemic_infection", 1); 
        put("internal_haemorrhaging", 1); 
        put("buboes", 1); 
        put("abcesses", 1); 
    }};

    public static List<TraitCard> getDefaultTraitDeck(){
        //TODO: Implement default trait deck
        List<TraitCard> deck = new ArrayList<>();
            idToCount.keySet().forEach(id -> {
                IntStream.range(0, idToCount.get(id)).forEach(val -> {
                    switch(id){
                        case "heat_resistance":
                        deck.add(heatResistance());
                        break;
                        case "cold_resistance":
                        deck.add(heatResistance());
                        break;
                        case "sweating":
                        deck.add(sweating());
                        break;
                        case "meningitis":
                        deck.add(meningitis());
                        break;
                        case "confusion":
                        deck.add(confusion());
                        break;
                        case "dysentery":
                        deck.add(dysentery());
                        break;
                        case "coma":
                        deck.add(coma());
                        break;
                        case "insanity":
                        deck.add(insanity());
                        break;
                        case "pustules":
                        deck.add(pustules());
                        break;
                        case "delirium":
                        deck.add(delirium());
                        break;
                        case "psychosis":
                        deck.add(psychosis());
                        break;
                        case "coughing":
                        deck.add(coughing());
                        break; 
                        case "blindness":
                        deck.add(blindness());
                        break;
                        case "paranoia":
                        deck.add(paranoia());
                        break;
                        case "headache":
                        deck.add(headache());
                        break;
                        case "hallucinations":
                        deck.add(hallucinations());
                        break;
                        case "nose_bleed":
                        deck.add(noseBleed());
                        break;
                        case "cysts":
                        deck.add(cysts());
                        break;
                        case "insomnia":
                        deck.add(insomnia());
                        break;
                        case "gastric_ulceration":
                        deck.add(gastricUlceration());
                        break;
                        case "hypothermia":
                        deck.add(hypothermia());
                        break;
                        case "respiratory_failure":
                        deck.add(respitoryFailure());
                        break;
                        case "rash":
                        deck.add(rash());
                        break;
                        case "pneumonia":
                        deck.add(pneumonia());
                        break;
                        case "paralysis":
                        deck.add(paralysis());
                        break;
                        case "seizures":
                        deck.add(seizures());
                        break;
                        case "tumours":
                        deck.add(tumours());
                        break;
                        case "fever":
                        deck.add(fever());
                        break;
                        case "cannibalism":
                        deck.add(cannibalism());
                        break;
                        case "skin_lesions":
                        deck.add(skinLesions());
                        break; 
                        case "necrosis":
                        deck.add(necrosis());
                        break;
                        case "sneezing":
                        deck.add(sneezing());
                        break;
                        case "total_organ_failure":
                        deck.add(totalOrganFailure());
                        break;
                        case "sore_throat":
                        deck.add(soreThroat());
                        break;
                        case "air_transmission":
                        deck.add(airTransmission());
                        break;
                        case "water_transmission":
                        deck.add(waterTransmission());
                        break;
                        case "vomiting":
                        deck.add(vomiting());
                        break;
                        case "haemorrhagic_shock":
                        deck.add(haemorrhagicShock());
                        break;
                        case "systemic_infection":
                        deck.add(systemicInfection());
                        break;
                        case "internal_haemorrhaging":
                        deck.add(internalHaemorrhaging());
                        break;
                        case "buboes":
                        deck.add(buboes());
                        break;
                        case "abcesses":
                        deck.add(abcesses());
                        break;
                    }
                });             
            });
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

    public static TraitCard blindness(){
        return new TraitCard("blindness", 2, List.of(new LethalityTrait()));
    }

 


}
