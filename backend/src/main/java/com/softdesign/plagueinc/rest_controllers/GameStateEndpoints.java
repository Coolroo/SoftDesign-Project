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

    @PostMapping("/createGame")
    public ResponseEntity<String> createGame(){
        try{
            return ResponseEntity.ok().body(gameStateManager.createGame());
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/joinGame")
    public ResponseEntity<UUID> joinGame(@RequestParam("gameStateId") String gameStateId, @RequestBody JoinGameDTO joinGameDTO){
        try{
            return ResponseEntity.ok().body(gameStateManager.joinGame(gameStateId, joinGameDTO.plagueColor()));
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
        }
    }
    
    @PostMapping("/voteToStart")
    public ResponseEntity<Void> voteToStart(@RequestParam("gameStateId") String gameStateId, @RequestBody PlayerId playerId){
        try{
            gameStateManager.voteToStart(gameStateId, playerId.playerId());
        }
        catch(Exception e){
            throw e;
            //logger.error(e.toString());
            //return ResponseEntity.badRequest().eTag(e.getMessage()).build();
        }
        return new ResponseEntity<>(HttpStatus.OK);
        
    }

    @PostMapping("/proceedState")
    public ResponseEntity<Void> proceedState(@RequestParam("gameStateId") String gameStateId, @RequestBody PlayerId playerId){
        try{
            gameStateManager.proceedState(gameStateId, playerId.playerId());
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/drawCountry")
    public ResponseEntity<Country> drawCountry(@RequestParam("gameStateId") String gameStateId, @RequestBody PlayerId playerId){
        try{
            Country country = gameStateManager.drawCountryAction(gameStateId, playerId.playerId());
            return ResponseEntity.ok().body(country);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/takeCountry")
    public ResponseEntity<Country> takeCountry(@RequestParam("gameStateId") String gameStateId, @RequestBody TakeCountryDTO takeCountryDTO){
        //First ensure that the player calling is the right one
        try{
            Country country = gameStateManager.selectCountryFromRevealed(gameStateId, takeCountryDTO.playerId(), takeCountryDTO.countryName());
            return ResponseEntity.ok().body(country);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/playCountry")
    public ResponseEntity<Void> playCountry(@RequestParam("gameStateId") String gameStateId, @RequestBody PlayerId playerId){
        try{
            gameStateManager.playCountry(gameStateId, playerId.playerId());
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/discardCountry")
    public ResponseEntity<Void> discardCountry(@RequestParam("gameStateId") String gameStateId, @RequestBody PlayerId playerId){
        try{
            gameStateManager.discardCountry(gameStateId, playerId.playerId());
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/evolve")
    public ResponseEntity<Void> evolve(@RequestParam("gameStateId") String gameStateId, @RequestBody EvolveDTO evolveDTO){
        try{
            gameStateManager.evolveTrait(gameStateId, evolveDTO.playerId(),evolveDTO.traitSlot(), evolveDTO.traitIndex());
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/skipEvolution")
    public ResponseEntity<Void> skipEvolution(@RequestParam("gameStateId") String gameStateId, @RequestBody PlayerId playerId){
        try{
            gameStateManager.skipEvolve(gameStateId, playerId.playerId());
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/infect")
    public ResponseEntity<Void> infect(@RequestParam("gameStateId") String gameStateId, @RequestBody InfectDTO infectDTO){
        try{
            gameStateManager.attemptInfect(gameStateId, infectDTO.playerId(),infectDTO.countryName());
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/rollDeathDice")
    public ResponseEntity<Integer> rollDeathDice(@RequestParam("gameStateId") String gameStateId, @RequestBody PlayerId playerId){
        try{
            gameStateManager.rollDeathDice(gameStateId, playerId.playerId());
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //GET Mappings
    @GetMapping("/gameState")
    public ResponseEntity<GameState> getGameState(@RequestParam("gameStateId") String gameStateId){
        return ResponseEntity.ok().body(gameStateManager.getGameState(gameStateId));
    }

    @GetMapping("/getHand")
    public ResponseEntity<List<String>> getHand(@RequestParam("gameStateId") String gameStateId, @RequestParam("playerId") UUID playerId){
        try{
            List<String> hand = gameStateManager.getGameState(gameStateId)
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