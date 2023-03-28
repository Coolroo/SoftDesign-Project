package com.softdesign.plagueinc.models.gamestate;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.softdesign.plagueinc.models.gamestate.selection_objects.CitySelection;
import com.softdesign.plagueinc.models.gamestate.selection_objects.ContinentSelection;
import com.softdesign.plagueinc.models.gamestate.selection_objects.CountrySelection;
import com.softdesign.plagueinc.models.gamestate.selection_objects.SelectionObject;
import com.softdesign.plagueinc.models.gamestate.selection_objects.TraitCardSelection;
import com.softdesign.plagueinc.models.gamestate.selection_objects.TraitSlotSelection;
import com.softdesign.plagueinc.models.plague.Plague;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ConditionalAction {

    @JsonIgnore
    private Logger logger = LoggerFactory.getLogger(ConditionalAction.class);

    @JsonIgnore
    @NonNull
    private GameStateAction condition;
    @JsonIgnore
    @NonNull
    private GameStateAction action;
    @NonNull
    private List<InputSelection> requiredInputs;


    public void condition(Plague plague, GameState gameState){
        getCondition().op(plague, gameState, List.of());
    }

    public void resolveEffect(Plague plague, GameState gameState, List<SelectionObject> inputs){
        validateInput(inputs);
        action.op(plague, gameState, inputs);
    }

    protected void validateInput(List<SelectionObject> inputSelection){
        final Map<InputSelection, Class<?>> classMap = Map.of(InputSelection.CITY, CitySelection.class,
        InputSelection.COUNTRY, CountrySelection.class, 
        InputSelection.CONTINENT, ContinentSelection.class, 
        InputSelection.TRAIT_CARD, TraitCardSelection.class,
        InputSelection.TRAIT_SLOT, TraitSlotSelection.class);

        if(inputSelection.size() != this.requiredInputs.size()){
            logger.warn("Input selection ({}) does not match the expected size ({})", inputSelection.size(), this.requiredInputs.size());
            throw new IllegalArgumentException("Input selection does not match the expected size");
        }
        for(int i = 0; i < this.requiredInputs.size(); i++){
            if(!classMap.get(this.requiredInputs.get(i)).isInstance(inputSelection.get(i))){
                logger.warn("Input selection ({}) does not match the expected class ({})", inputSelection.get(i).getClass(), classMap.get(this.requiredInputs.get(i)));
                throw new IllegalArgumentException("Input selection does not match the expected class");
            }
        }
    }
}
