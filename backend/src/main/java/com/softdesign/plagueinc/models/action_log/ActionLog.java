package com.softdesign.plagueinc.models.action_log;

import com.softdesign.plagueinc.models.gamestate.PlayState;

public abstract class ActionLog {
    
    PlayState state;

    ActionLog(PlayState state){
        this.state = state;
    }

    public PlayState getState(){
        return state;
    }
}
