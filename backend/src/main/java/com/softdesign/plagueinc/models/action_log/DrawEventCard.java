package com.softdesign.plagueinc.models.action_log;

import com.softdesign.plagueinc.models.events.EventCard;
import com.softdesign.plagueinc.models.gamestate.PlayState;
import com.softdesign.plagueinc.models.plague.Plague;

import lombok.Getter;

@Getter
public class DrawEventCard extends ActionLog {

    private Plague plague;
    private EventCard card;

    public DrawEventCard(PlayState state, Plague plague, EventCard card) {
        super(state);
        this.plague = plague;
        this.card = card;
    }
    
}
