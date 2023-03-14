package com.softdesign.plagueinc.rest_controllers.DTOs;

import java.util.List;
import java.util.UUID;

import com.softdesign.plagueinc.rest_controllers.DTOs.selection_objects.SelectionObject;

public record DoActionDTO(UUID playerId, List<SelectionObject> inputs) {
    
}
