package com.softdesign.plagueinc.models.gamestate.selection_objects;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

@JsonSubTypes({
    @Type(value = CitySelection.class, name = "city"),
    @Type(value = CountrySelection.class, name = "country"),
    @Type(value = TraitSlotSelection.class, name = "traitSlot"),
    @Type(value = ContinentSelection.class, name = "continent"),
    @Type(value = TraitCardSelection.class, name = "traitCard")
})
public abstract class SelectionObject {
    
}
