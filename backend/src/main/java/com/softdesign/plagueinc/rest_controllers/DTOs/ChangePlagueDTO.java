package com.softdesign.plagueinc.rest_controllers.DTOs;

import java.util.UUID;

import com.softdesign.plagueinc.models.plague.DiseaseType;

public record ChangePlagueDTO(UUID playerId, DiseaseType diseaseType) {
    
}
