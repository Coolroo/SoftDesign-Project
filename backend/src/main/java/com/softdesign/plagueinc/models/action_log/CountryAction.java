package com.softdesign.plagueinc.models.action_log;

import com.softdesign.plagueinc.managers.futures.input_types.CountryChoice;
import com.softdesign.plagueinc.models.countries.Country;
import com.softdesign.plagueinc.models.gamestate.PlayState;

import lombok.Getter;

@Getter
public class CountryAction extends ActionLog {

    private CountryChoice countryChoice;

    private Country country;

    public CountryAction(CountryChoice countryChoice, Country country){
        super(PlayState.COUNTRY);
        this.countryChoice = countryChoice;
        this.country = country;
    }
    
}
