package com.softdesign.plagueinc.rest_controllers.DTOs;

import java.util.UUID;

public record EvolveDTO(UUID playerId, int traitIndex, int traitSlot) {
    
}
