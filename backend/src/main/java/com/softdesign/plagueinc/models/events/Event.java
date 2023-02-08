package com.softdesign.plagueinc.models.events;

import com.softdesign.plagueinc.models.gamestate.GameState;
import com.softdesign.plagueinc.models.plague.Plague;

public interface Event {

    public void op(Plague player, GameState gameState);

}
