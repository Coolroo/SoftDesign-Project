package com.softdesign.plagueinc.util;

import java.util.ArrayList;
import java.util.List;

import com.softdesign.plagueinc.models.plague.abilities.BonusDNA;
import com.softdesign.plagueinc.models.plague.abilities.GeneticSwitch;
import com.softdesign.plagueinc.models.plague.abilities.Outbreak;
import com.softdesign.plagueinc.models.plague.abilities.RandomMutation;
import com.softdesign.plagueinc.models.plague.trait_slot.TraitSlot;

public class PlagueReference {
    
    public static List<TraitSlot> bacteriaSlots(){
        return new ArrayList<>(List.of(new TraitSlot(), 
        new TraitSlot(), 
        new TraitSlot(), 
        new TraitSlot(new Outbreak()),
        new TraitSlot(new BonusDNA())));
    }

    public static List<TraitSlot> virusSlots(){
        return new ArrayList<>(List.of(new TraitSlot(),
        new TraitSlot(),
        new TraitSlot(new GeneticSwitch()),
        new TraitSlot(new Outbreak()),
        new TraitSlot(new RandomMutation())));
    }
}
