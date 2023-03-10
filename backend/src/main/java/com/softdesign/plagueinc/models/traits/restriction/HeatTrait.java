package com.softdesign.plagueinc.models.traits.restriction;

import com.softdesign.plagueinc.models.traits.TraitType;

public class HeatTrait extends RestrictionTrait {

    public HeatTrait() {
        super(TraitType.HEAT_RESISTANCE);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof HeatTrait;
    }
    
}
