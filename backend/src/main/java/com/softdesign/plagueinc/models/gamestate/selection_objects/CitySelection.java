package com.softdesign.plagueinc.models.gamestate.selection_objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CitySelection extends SelectionObject {
    private int cityIndex;
    private String countryName;
}
