package com.softdesign.plagueinc.models.action_log;

import com.softdesign.plagueinc.models.events.Event;
import com.softdesign.plagueinc.models.gamestate.PlayState;
import com.softdesign.plagueinc.models.plague.Plague;

public class PlayEventCardAction extends ActionLog {
    private Plague plague;

    private Event event;

    public PlayEventCardAction(PlayState playState, Plague plague, Event event){
        super(playState);
        this.plague = plague;
        this.event = event;
    }

    public Plague getPlague(){
        return plague;
    }
    
    public Event getEvent(){
        return event;
    }
}
