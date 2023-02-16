package com.softdesign.plagueinc.models.traits.disease;

import com.softdesign.plagueinc.models.traits.TraitType;

public class InfectivityTrait extends DiseaseTrait {

    public InfectivityTrait() {
        super(TraitType.INFECTIVITY);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof InfectivityTrait;
    }
    
}
