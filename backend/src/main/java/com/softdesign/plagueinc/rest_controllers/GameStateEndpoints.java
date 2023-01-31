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
import com.softdesign.plagueinc.models.plague.Plague;
import com.softdesign.plagueinc.rest_controllers.DTOs.EvolveDTO;
import com.softdesign.plagueinc.rest_controllers.DTOs.InfectDTO;
import com.softdesign.plagueinc.rest_controllers.DTOs.PlayerId;
import com.softdesign.plagueinc.rest_controllers.DTOs.TakeCountryDTO;

@RestController
@CrossOrigin
public class GameStateEndpoints {

    Logger logger = LoggerFactory.getLogger(GameStateEndpoints.class);

    @Autowired
    private GameStateManager gameStateManager;

    @PostMapping("/joinGame")
    public ResponseEntity<Plague> joinGame(@RequestBody PlayerId playerId){
        try{
            Plague plague = gameStateManager.joinGame();
            return ResponseEntity.ok().body(plague);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
        }
    }
    
    @PostMapping("/voteToStart")
    public ResponseEntity<Void> voteToStart(@RequestBody PlayerId playerId){
        try{
            gameStateManager.startGame(playerId.playerId());
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
        
    }

    @PostMapping("/proceedState")
    public ResponseEntity<Void> proceedState(@RequestBody PlayerId playerId){
        try{
            gameStateManager.proceedState();
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/drawCountry")
    public ResponseEntity<Country> drawCountry(@RequestBody PlayerId playerId){
        try{
            Country country = gameStateManager.drawCountry();
            return ResponseEntity.ok().body(country);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/takeCountry")
    public ResponseEntity<Country> takeCountry(@RequestBody TakeCountryDTO takeCountryDTO){
        //First ensure that the player calling is the right one
        if(!gameStateManager.verifyTurn(takeCountryDTO.playerId())){
            logger.warn("Player with ID: {}, attempted to take a country, but it is not their turn", takeCountryDTO.playerId());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try{
            Country country = gameStateManager.selectCountryFromRevealed(takeCountryDTO.cardIndex());
            return ResponseEntity.ok().body(country);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /***@PostMapping("/playCountry")
    public ResponseEntity<Void> playCountry(@RequestBody PlayerId playerId){
        try{
            //uh
        }
        catch(Exception e){

        }
    }

    @PostMapping("/discardCountry")
    public ResponseEntity<Void> discardCountry(@RequestBody PlayerId playerId){
        try{
            gameStateManager.discardCountry();
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }***/

    @PostMapping("/evolve")
    public ResponseEntity<Void> evolve(@RequestBody EvolveDTO evolveDTO){
        try{
            gameStateManager.evolveTrait(evolveDTO.traitSlot(), evolveDTO.traitIndex());
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("skipEvolution")
    public ResponseEntity<Void> skipEvolution(@RequestBody PlayerId playerId){
        try{
            gameStateManager.skipEvolve();
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/infect")
    public ResponseEntity<Void> infect(@RequestBody InfectDTO infectDTO){
        try{
            gameStateManager.attemptInfect(infectDTO.countryName());
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/rollDeathDice")
    public ResponseEntity<Integer> rollDeathDice(@RequestBody PlayerId playerId){
        try{
            int diceNum = gameStateManager.rollDeathDice();
            return ResponseEntity.ok().body(diceNum);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}