package com.softdesign.plagueinc.rest_controllers.DTOs.selection_objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CitySelection extends SelectionObject {
    private int cityIndex;
    private String countryName;
}
