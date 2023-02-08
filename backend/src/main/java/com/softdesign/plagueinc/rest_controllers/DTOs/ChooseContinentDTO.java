package com.softdesign.plagueinc.rest_controllers.DTOs;

import java.util.UUID;

import com.softdesign.plagueinc.models.countries.Continent;

public record ChooseContinentDTO(UUID playerId, Continent continent) {
    
}
