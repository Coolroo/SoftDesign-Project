package com.softdesign.plagueinc.rest_controllers;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.softdesign.plagueinc.managers.GameStateManager;
import com.softdesign.plagueinc.models.countries.Country;
import com.softdesign.plagueinc.models.gamestate.GameState;
import com.softdesign.plagueinc.models.traits.TraitCard;
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
    public ResponseEntity<UUID> joinGame(@RequestBody JoinGameDTO joinGameDTO){
        try{
            return ResponseEntity.ok().body(gameStateManager.joinGame(joinGameDTO.plagueColor()));
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
            throw e;
            //logger.error(e.toString());
            //return ResponseEntity.badRequest().eTag(e.getMessage()).build();
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
            Country country = gameStateManager.selectCountryFromRevealed(takeCountryDTO.playerId(), takeCountryDTO.countryName());
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

    //GET Mappings
    @GetMapping("/gameState")
    public ResponseEntity<GameState> getGameState(){
        return ResponseEntity.ok().body(gameStateManager.getGameState());
    }

    @GetMapping("/getHand")
    public ResponseEntity<List<String>> getHand(@RequestParam("playerId") UUID playerId){
        try{
            List<String> hand = gameStateManager.getGameState()
            .getPlagues()
            .stream()
            .filter(plague -> plague.getPlayerId().equals(playerId))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new)
            .getHand()
            .stream()
            .map(card -> card.name())
            .toList();
            return ResponseEntity.ok().body(hand);
        }
        catch(Exception e){
            return ResponseEntity.badRequest().eTag("Invalid player ID").build();
        }
        
        
        
    }
    
}