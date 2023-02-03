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
import com.softdesign.plagueinc.rest_controllers.DTOs.JoinGameDTO;
import com.softdesign.plagueinc.rest_controllers.DTOs.PlayerId;
import com.softdesign.plagueinc.rest_controllers.DTOs.TakeCountryDTO;

@RestController
@CrossOrigin
public class GameStateEndpoints {

    Logger logger = LoggerFactory.getLogger(GameStateEndpoints.class);

    @Autowired
    private GameStateManager gameStateManager;

    //POST Endpoints

    @PostMapping("/joinGame")
    public ResponseEntity<Plague> joinGame(@RequestBody JoinGameDTO joinGameDTO){
        try{
            Plague plague = gameStateManager.joinGame(joinGameDTO.diseaseType());
            return ResponseEntity.ok().body(plague);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
        }
    }
    
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

    @PostMapping("/proceedState")
    public ResponseEntity<Void> proceedState(@RequestBody PlayerId playerId){
        try{
            gameStateManager.proceedState(playerId.playerId());
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/drawCountry")
    public ResponseEntity<Country> drawCountry(@RequestBody PlayerId playerId){
        try{
            Country country = gameStateManager.drawCountryAction(playerId.playerId());
            return ResponseEntity.ok().body(country);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

    @PostMapping("/playCountry")
    public ResponseEntity<Void> playCountry(@RequestBody PlayerId playerId){
        try{
            gameStateManager.playCountry(playerId.playerId());
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/discardCountry")
    public ResponseEntity<Void> discardCountry(@RequestBody PlayerId playerId){
        try{
            gameStateManager.discardCountry(playerId.playerId());
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/evolve")
    public ResponseEntity<Void> evolve(@RequestBody EvolveDTO evolveDTO){
        try{
            gameStateManager.evolveTrait(evolveDTO.playerId(),evolveDTO.traitSlot(), evolveDTO.traitIndex());
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/skipEvolution")
    public ResponseEntity<Void> skipEvolution(@RequestBody PlayerId playerId){
        try{
            gameStateManager.skipEvolve(playerId.playerId());
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/infect")
    public ResponseEntity<Void> infect(@RequestBody InfectDTO infectDTO){
        try{
            gameStateManager.attemptInfect(infectDTO.playerId(),infectDTO.countryName());
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/rollDeathDice")
    public ResponseEntity<Integer> rollDeathDice(@RequestBody PlayerId playerId){
        try{
            gameStateManager.rollDeathDice(playerId.playerId());
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
}