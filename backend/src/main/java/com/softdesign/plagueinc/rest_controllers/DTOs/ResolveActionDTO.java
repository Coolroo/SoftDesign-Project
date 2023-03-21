package com.softdesign.plagueinc.rest_controllers.DTOs;

import java.util.List;
import java.util.UUID;

import com.softdesign.plagueinc.models.gamestate.selection_objects.SelectionObject;

public record ResolveActionDTO(UUID playerId, List<SelectionObject> inputSelections) {
    
}
