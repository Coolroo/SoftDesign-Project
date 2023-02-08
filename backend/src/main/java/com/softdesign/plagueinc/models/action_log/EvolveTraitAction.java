package com.softdesign.plagueinc.models.action_log;

import com.softdesign.plagueinc.models.gamestate.PlayState;
import com.softdesign.plagueinc.models.traits.TraitCard;

import lombok.Getter;

@Getter
public class EvolveTraitAction extends ActionLog {

    private TraitCard card;

    public EvolveTraitAction(TraitCard card){
        super(PlayState.EVOLVE);
        this.card = card;
    }

}
