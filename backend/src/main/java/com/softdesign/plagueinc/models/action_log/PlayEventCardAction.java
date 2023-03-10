package com.softdesign.plagueinc.models.action_log;

import com.softdesign.plagueinc.models.events.EventCard;
import com.softdesign.plagueinc.models.gamestate.PlayState;
import com.softdesign.plagueinc.models.plague.Plague;

import lombok.Getter;

@Getter
public class PlayEventCardAction extends ActionLog {
    private Plague plague;

    private EventCard event;

    public PlayEventCardAction(PlayState playState, Plague plague, EventCard event){
        super(playState);
        this.plague = plague;
        this.event = event;
    }
}
