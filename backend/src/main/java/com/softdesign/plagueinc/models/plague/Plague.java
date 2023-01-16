package com.softdesign.plagueinc.models.plague;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.softdesign.plagueinc.models.countries.Country;
import com.softdesign.plagueinc.models.events.Event;
import com.softdesign.plagueinc.models.plague.trait_slot.TraitSlot;
import com.softdesign.plagueinc.models.traits.TraitCard;
import com.softdesign.plagueinc.models.traits.TraitType;

public class Plague {
    
    private int playerId;

    private int dnaPoints;

    private int plagueTokens;

    private List<TraitSlot> traitSlots;

    private Set<TraitType> traits;

    private List<TraitCard> hand;

    private List<Event> eventCards;

    private Set<Country> killedCountries;

    private static final int INITIAL_PLAGUE_TOKENS = 16;

    public Plague(int playerId){
        this.playerId = playerId;
        this.dnaPoints = 0;
        this.plagueTokens = INITIAL_PLAGUE_TOKENS;
        this.traits = new HashSet<>();
        this.traits.addAll(List.of(TraitType.INFECTIVITY, TraitType.INFECTIVITY, TraitType.LETHALITY));
        this.hand = new ArrayList<>();
        this.eventCards = new ArrayList<>();
        this.traitSlots = new ArrayList<>();
        for(int i = 0; i<5; i++){
            this.traitSlots.add(new TraitSlot(Optional.empty()));
        }
    }
}
