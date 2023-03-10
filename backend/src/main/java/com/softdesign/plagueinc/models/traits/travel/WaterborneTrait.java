package com.softdesign.plagueinc.models.traits.travel;

import com.softdesign.plagueinc.models.traits.TraitType;

public class WaterborneTrait extends TravelTrait {

    public WaterborneTrait() {
        super(TraitType.WATERBORNE);
    }
    
    @Override
    public boolean equals(Object o) {
        return o instanceof WaterborneTrait;
    }

    
}
