package com.softdesign.plagueinc.models.action_log;

import com.softdesign.plagueinc.models.countries.Country;
import com.softdesign.plagueinc.models.gamestate.PlayState;

import lombok.Getter;

@Getter
public class KillCountryAction extends ActionLog {

    private Country country;

    private int roll;

    private boolean success;

    public KillCountryAction(Country country, int roll, boolean success){
        super(PlayState.DEATH);
        this.country = country;
        this.roll = roll;
        this.success = success;
    }
}
