package com.softdesign.plagueinc.rest_controllers.DTOs.selection_objects;

import com.softdesign.plagueinc.models.countries.Continent;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ContinentSelection extends SelectionObject {
    private Continent continent;
}
