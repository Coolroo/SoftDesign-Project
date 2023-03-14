package com.softdesign.plagueinc.models.gamestate;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.softdesign.plagueinc.models.plague.Plague;
import com.softdesign.plagueinc.rest_controllers.DTOs.selection_objects.CitySelection;
import com.softdesign.plagueinc.rest_controllers.DTOs.selection_objects.ContinentSelection;
import com.softdesign.plagueinc.rest_controllers.DTOs.selection_objects.CountrySelection;
import com.softdesign.plagueinc.rest_controllers.DTOs.selection_objects.SelectionObject;
import com.softdesign.plagueinc.rest_controllers.DTOs.selection_objects.TraitCardSelection;
import com.softdesign.plagueinc.rest_controllers.DTOs.selection_objects.TraitSlotSelection;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ConditionalAction {
    
    private Logger logger = LoggerFactory.getLogger(ConditionalAction.class);

    @NonNull
    private GameStateAction condition;
    @NonNull
    private GameStateAction action;
    @NonNull
    private List<InputSelection> requiredInputs;
    @NonNull
    private GameStateAction handleFail;


    public void condition(Plague plague, GameState gameState){
        getCondition().op(plague, gameState, List.of());
    }

    public void resolveEffect(Plague plague, GameState gameState, List<SelectionObject> inputs){
        try{
            validateInput(inputs);
            action.op(plague, gameState, inputs);
        }
        catch(Exception e){
            handleFail.op(plague, gameState, List.of());
            throw e;
        }
        
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
