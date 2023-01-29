package com.softdesign.plagueinc.rest_controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.softdesign.plagueinc.managers.GameStateManager;
import com.softdesign.plagueinc.models.countries.Country;
import com.softdesign.plagueinc.rest_controllers.DTOs.PlayerId;
import com.softdesign.plagueinc.rest_controllers.DTOs.TakeCountryDTO;

@RestController
@CrossOrigin
public class GameStateEndpoints {

    Logger logger = LoggerFactory.getLogger(GameStateEndpoints.class);

    @Autowired
    private GameStateManager gameStateManager;
    
    @PostMapping("/voteToStart")
    public ResponseEntity<Void> voteToStart(@RequestBody PlayerId playerId){
        try{
            gameStateManager.voteToStart(playerId.playerId());
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
        
    }

    @PostMapping("/takeCountry")
    public ResponseEntity<Country> takeCountry(@RequestBody TakeCountryDTO takeCountryDTO){
        //First ensure that the player calling is the right one
        try{
            Country country = gameStateManager.selectCountryFromRevealed(takeCountryDTO.playerId(), takeCountryDTO.cardIndex());
            return ResponseEntity.ok().body(country);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
