package com.softdesign.plagueinc.models.action_log;

import com.softdesign.plagueinc.models.countries.Country;
import com.softdesign.plagueinc.models.gamestate.PlayState;

import lombok.Getter;

@Getter
public class InfectCountryAction extends ActionLog {
    
    private Country country;

    public InfectCountryAction(Country country){
        super(PlayState.INFECT);
        this.country = country;
    }
}
