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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.softdesign.plagueinc.managers.GameStateManager;
import com.softdesign.plagueinc.models.countries.Country;
import com.softdesign.plagueinc.models.gamestate.GameState;
import com.softdesign.plagueinc.rest_controllers.DTOs.EvolveDTO;
import com.softdesign.plagueinc.rest_controllers.DTOs.InfectDTO;
import com.softdesign.plagueinc.rest_controllers.DTOs.JoinGameDTO;
import com.softdesign.plagueinc.rest_controllers.DTOs.PlayEventCardDTO;
import com.softdesign.plagueinc.rest_controllers.DTOs.PlayerId;
import com.softdesign.plagueinc.rest_controllers.DTOs.TakeCountryDTO;

@RestController
@CrossOrigin
public class GameStateEndpoints {

    
    Logger logger = LoggerFactory.getLogger(GameStateEndpoints.class);

    @Autowired
    private GameStateManager gameStateManager;

    //POST Endpoints

/**
 * The Post Mapping for /createGame used to
 * create a new game and return its ID
 *
 * @param 
 *
 * @return a String representing the Game ID;
 *
 * @docauthor Nick Lee
 */
    @PostMapping("/createGame")
    public ResponseEntity<String> createGame(){
        try{
            return ResponseEntity.ok().body(gameStateManager.createGame());
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

/**
 * The Patch Mapping for /joinGame used to
 * join a game using a given game ID
 *
 * @param gameStateId String
 * @param joinGameDTO JoinGameDTO
 *
 * @return UUID
 *
 * @docauthor Nick Lee
 */
    @PatchMapping("/joinGame")
    public ResponseEntity<UUID> joinGame(@RequestParam("gameStateId") String gameStateId, @RequestBody JoinGameDTO joinGameDTO){
        try{
            return ResponseEntity.ok().body(gameStateManager.joinGame(gameStateId, joinGameDTO.plagueColor()));
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, e.getMessage(), e);
        }
    }

/**
 * The Patch Mapping for /voteToStart used to
 * allow users to submit their vote to start the game
 *
 * @param gameStateId String
 * @param playerId PlayerID
 *
 * @return void
 *
 * @docauthor Nick Lee
 */    
    @PatchMapping("/voteToStart")
    public ResponseEntity<Void> voteToStart(@RequestParam("gameStateId") String gameStateId, @RequestBody PlayerId playerId){
        try{
            gameStateManager.voteToStart(gameStateId, playerId.playerId());
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
            //logger.error(e.toString());
            //return ResponseEntity.badRequest().eTag(e.getMessage()).build();
        }
        return new ResponseEntity<>(HttpStatus.OK);
        
    }

/**
 * The Patch Mapping for /proceedState used to
 * proceed the game state
 *
 * @param gameStateId String
 * @param playerId PlayerID
 *
 * @return void
 *
 * @docauthor Nick Lee
 */
    @PatchMapping("/proceedState")
    public ResponseEntity<Void> proceedState(@RequestParam("gameStateId") String gameStateId, @RequestBody PlayerId playerId){
        try{
            gameStateManager.proceedState(gameStateId, playerId.playerId());
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

/**
 * The Patch Mapping for /drawCountry used to:
 *  - Draws a country from the GameState's deck of countries.
 *  - Returns the name of the drawn Country object to the caller.
 *
 * @param gameStateId String
 * @param playerId PlayerID
 *
 * @return Country name
 *
 * @docauthor Nick Lee
 */
    @PatchMapping("/drawCountry")
    public ResponseEntity<String> drawCountry(@RequestParam("gameStateId") String gameStateId, @RequestBody PlayerId playerId){
        try{
            Country country = gameStateManager.drawCountryAction(gameStateId, playerId.playerId());
            return ResponseEntity.ok().body(country.getCountryName());
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

/**
 * The Patch Mapping for /takeCountry used to
 * take a revealed country card
 *
 * @param gameStateId String
 * @param takeCountryDTO TakeCountryDTO
 *
 * @return name of Country
 *
 * @docauthor Nick Lee
 */
    @PatchMapping("/takeCountry")
    public ResponseEntity<String> takeCountry(@RequestParam("gameStateId") String gameStateId, @RequestBody TakeCountryDTO takeCountryDTO){
        //First ensure that the player calling is the right one
        try{
            Country country = gameStateManager.selectCountryFromRevealed(gameStateId, takeCountryDTO.playerId(), takeCountryDTO.countryName());
            return ResponseEntity.ok().body(country.getCountryName());
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

/**
 * The Patch Mapping for /playCountry used to
 * play the given country card
 *
 * @param gameStateId String
 * @param playerId PlayerID
 *
 * @return void
 *
 * @docauthor Trelent
 */
    @PatchMapping("/playCountry")
    public ResponseEntity<Void> playCountry(@RequestParam("gameStateId") String gameStateId, @RequestBody PlayerId playerId){
        try{
            gameStateManager.playCountry(gameStateId, playerId.playerId());
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

/**
 * The Patch Mapping for /discardCountry used to
 * discard the revealed country card
 *
 * @param gameStateId String
 * @param playerId PlayerID
 *
 * @return void
 *
 * @docauthor Nick Lee
 */
    @PatchMapping("/discardCountry")
    public ResponseEntity<Void> discardCountry(@RequestParam("gameStateId") String gameStateId, @RequestBody PlayerId playerId){
        try{
            gameStateManager.discardCountry(gameStateId, playerId.playerId());
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

/**
 * The Patch Mapping for /evolve used when a
 * player decides to evolve a trait in their hand
 *
 * @param gameStateId String
 * @param playerId PlayerID
 * @param evolveDTO EvolveDTO
 *
 * @return void
 *
 * @docauthor Nick Lee
 */
    @PatchMapping("/evolve")
    public ResponseEntity<Void> evolve(@RequestParam("gameStateId") String gameStateId, @RequestBody EvolveDTO evolveDTO){
        try{
            gameStateManager.evolveTrait(gameStateId, evolveDTO.playerId(),evolveDTO.traitSlot(), evolveDTO.traitIndex());
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

/**
 * The Patch Mapping for /skipEvolution used to
 * skip the evolution phase of a players turn
 *
 * @param gameStateId String
 * @param playerId PlayerID
 *
 * @return void
 *
 * @docauthor Nick Lee
 */
    @PatchMapping("/skipEvolution")
    public ResponseEntity<Void> skipEvolution(@RequestParam("gameStateId") String gameStateId, @RequestBody PlayerId playerId){
        try{
            gameStateManager.skipEvolve(gameStateId, playerId.playerId());
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

/**
 * The Patch Mapping for /infect used to
 * infect a country on the board
 *
 * @param gameStateId String
 * @param infectDTO InfectDTO
 *
 * @return void
 *
 * @docauthor Nick Lee
 */
    @PatchMapping("/infect")
    public ResponseEntity<Void> infect(@RequestParam("gameStateId") String gameStateId, @RequestBody InfectDTO infectDTO){
        try{
            gameStateManager.attemptInfect(gameStateId, infectDTO.playerId(),infectDTO.countryName());
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

/**
 * The Patch Mapping for /rollDeathDice used to
 * roll the dice at the end of a turn i necessary
 *
 * @param gameStateId String
 * @param playerId PlayerID
 *
 * @return Integer
 *
 * @docauthor Nick Lee
 */
    @PatchMapping("/rollDeathDice")
    public ResponseEntity<Integer> rollDeathDice(@RequestParam("gameStateId") String gameStateId, @RequestBody PlayerId playerId){
        try{
            gameStateManager.rollDeathDice(gameStateId, playerId.playerId());
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/playEventCard")
    public ResponseEntity<Void> playEventCard(@RequestParam("gameStateId") String gameStateId, @RequestBody PlayEventCardDTO playEventCardDTO){
        try{
            gameStateManager.playEventCard(gameStateId, playEventCardDTO.playerId(), playEventCardDTO.eventCardIndex());
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


/**
 * The Get Mapping for /gameState used to
 * retrieve the current game State as JSON
 *
 * @param gameStateId String
 *
 * @return GameState
 *
 * @docauthor Nick Lee
 */
    @GetMapping("/gameState")
    public ResponseEntity<GameState> getGameState(@RequestParam("gameStateId") String gameStateId){
        return ResponseEntity.ok().body(gameStateManager.getGameState(gameStateId));
    }

    
/**
 * The Get Mapping for /getHand used to
 * retrieve the hand of a given player
 *
 * @param gameStateId String
 * @param playerId PlayerID
 *
 * @return A list of strings
 *
 * @docauthor Nick Lee
 */
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Player ID", e);
        }
        
        
        
    }
    
}