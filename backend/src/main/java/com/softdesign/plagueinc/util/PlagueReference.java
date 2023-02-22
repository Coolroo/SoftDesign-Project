package com.softdesign.plagueinc.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.softdesign.plagueinc.models.plague.DiseaseType;
import com.softdesign.plagueinc.models.plague.abilities.Ability;
import com.softdesign.plagueinc.models.plague.trait_slot.TraitSlot;

public class PlagueReference {

    public static List<TraitSlot> getTraitSlots(DiseaseType diseaseType){
        return Map.of(DiseaseType.BACTERIA, bacteriaSlots(), DiseaseType.VIRUS, virusSlots()).get(diseaseType);
    }
    
    private static List<TraitSlot> bacteriaSlots(){
        return new ArrayList<>(List.of(new TraitSlot(), 
        new TraitSlot(), 
        new TraitSlot(), 
        new TraitSlot(Ability.create("Outbreak")),
        new TraitSlot(Ability.create("BonusDNA"))));
    }

    private static List<TraitSlot> virusSlots(){
        return new ArrayList<>(List.of(new TraitSlot(),
        new TraitSlot(),
        new TraitSlot(Ability.create("GeneticSwitch")),
        new TraitSlot(Ability.create("Outbreak")),
        new TraitSlot(Ability.create("RandomMutation"))));
    }
}
