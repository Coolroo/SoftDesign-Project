package com.softdesign.plagueinc.models.traits.restriction;

import com.softdesign.plagueinc.models.traits.TraitType;

public class ColdTrait extends RestrictionTrait {

    public ColdTrait() {
        super(TraitType.COLD_RESISTANCE);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ColdTrait;
    }
    
}
