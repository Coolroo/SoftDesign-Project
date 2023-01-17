package com.softdesign.plagueinc.models.events;

import com.softdesign.plagueinc.models.plague.Plague;

public interface Event {

    public void resolveEffect(Plague player);

}
