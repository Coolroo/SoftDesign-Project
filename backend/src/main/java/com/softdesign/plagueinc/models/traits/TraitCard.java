package com.softdesign.plagueinc.models.traits;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record TraitCard(String name, @JsonIgnore int cost, @JsonIgnore List<Trait> traits){}
