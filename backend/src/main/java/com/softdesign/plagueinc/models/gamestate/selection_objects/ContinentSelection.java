package com.softdesign.plagueinc.models.gamestate.selection_objects;

import com.softdesign.plagueinc.models.countries.Continent;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ContinentSelection extends SelectionObject {
    private Continent continent;
}
