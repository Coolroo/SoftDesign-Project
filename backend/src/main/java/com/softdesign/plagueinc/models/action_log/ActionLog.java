package com.softdesign.plagueinc.models.action_log;

import com.softdesign.plagueinc.models.gamestate.PlayState;

import lombok.Getter;

@Getter
public abstract class ActionLog {
    
    private PlayState state;

    protected ActionLog(PlayState state){
        this.state = state;
    }
}
