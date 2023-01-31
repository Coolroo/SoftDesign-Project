package com.softdesign.plagueinc.rest_controllers.DTOs;

import java.util.UUID;
import com.softdesign.plagueinc.models.plague.DiseaseType;

public record JoinGameDTO(UUID playerID, DiseaseType diseaseType) {
    
}
