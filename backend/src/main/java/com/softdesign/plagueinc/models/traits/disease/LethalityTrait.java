package com.softdesign.plagueinc.models.traits.disease;

import com.softdesign.plagueinc.models.traits.TraitType;

public class LethalityTrait extends DiseaseTrait {

    public LethalityTrait() {
        super(TraitType.LETHALITY);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof LethalityTrait;
    }
    
}
