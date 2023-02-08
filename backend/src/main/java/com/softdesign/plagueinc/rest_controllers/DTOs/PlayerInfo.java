package com.softdesign.plagueinc.rest_controllers.DTOs;

import java.util.List;

import com.softdesign.plagueinc.models.plague.Plague;

public record PlayerInfo(List<String> hand, List<String> eventCards, Plague plague) {
    
}
