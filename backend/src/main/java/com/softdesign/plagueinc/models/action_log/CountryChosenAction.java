package com.softdesign.plagueinc.models.action_log;

import com.softdesign.plagueinc.models.countries.Country;
import com.softdesign.plagueinc.models.gamestate.PlayState;

public class CountryChosenAction extends ActionLog {
    
    private Country country;

    public CountryChosenAction(Country country){
        super(PlayState.CHOOSECOUNTRY);
        this.country = country;
    }

    public Country getCountry(){
        return country;
    }
}
