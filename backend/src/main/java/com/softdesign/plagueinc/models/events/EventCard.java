package com.softdesign.plagueinc.models.events;

import java.util.List;

import com.softdesign.plagueinc.models.gamestate.ConditionalAction;
import com.softdesign.plagueinc.models.gamestate.GameStateAction;
import com.softdesign.plagueinc.models.gamestate.InputSelection;

import lombok.Getter;

@Getter
public class EventCard extends ConditionalAction {

    private String name;

    public EventCard(String name, GameStateAction condition, GameStateAction effect, List<InputSelection> requiredInputs){
        super(effect, effect, requiredInputs, (dummy, dum, dumo) -> {});
        this.name = name;
    }

    public EventCard(String name, GameStateAction condition, GameStateAction effect, List<InputSelection> requiredInputs, GameStateAction handleFail){
        super(effect, effect, requiredInputs, handleFail);
        this.name = name;
    }

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(!(o instanceof EventCard)){
            return false;
        }
        EventCard other = (EventCard) o;
        return name.equals(other.name);
    }
}
