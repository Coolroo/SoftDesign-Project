package com.softdesign.plagueinc.rest_controllers.DTOs;

import java.util.UUID;

public record ChooseCityDTO(UUID playerId, String countryName, int cityIndex) {
    
}
