package com.softdesign.plagueinc.rest_controllers.DTOs;

import java.util.UUID;

import com.softdesign.plagueinc.managers.futures.input_types.CountryChoice;

public record CountryChoiceDTO(UUID playerId, String countryName, CountryChoice choice) {
    
}
