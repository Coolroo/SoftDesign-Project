package com.softdesign.plagueinc.models.traits.travel;

import com.softdesign.plagueinc.models.traits.TraitType;

public class AirborneTrait extends TravelTrait {

    public AirborneTrait() {
        super(TraitType.AIRBORNE);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof AirborneTrait;
    }
    
}
