package com.softdesign.plagueinc.rest_controllers;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.softdesign.plagueinc.managers.GameStateManager;
import com.softdesign.plagueinc.models.gamestate.GameState;
import com.softdesign.plagueinc.models.plague.Plague;
import com.softdesign.plagueinc.rest_controllers.DTOs.ChangePlagueDTO;
import com.softdesign.plagueinc.rest_controllers.DTOs.ChooseCountryDTO;
import com.softdesign.plagueinc.rest_controllers.DTOs.CountryChoiceDTO;
import com.softdesign.plagueinc.rest_controllers.DTOs.EvolveDTO;
import com.softdesign.plagueinc.rest_controllers.DTOs.JoinGameDTO;
import com.softdesign.plagueinc.rest_controllers.DTOs.PlayEventCardDTO;
import com.softdesign.plagueinc.rest_controllers.DTOs.PlayerId;
import com.softdesign.plagueinc.rest_controllers.DTOs.PlayerInfo;

@RestController
@CrossOrigin
public class GameStateEndpoints {

    
    Logger logger = LoggerFactory.getLogger(GameStateEndpoints.class);

    @Autowired
    private GameStateManager gameStateManager;

    @Autowired
    private SimpMessagingTemplate template;

    private AtomicBoolean deathBool = new AtomicBoolean(false);

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
            UUID plagueId = gameStateManager.joinGame(gameStateId, joinGameDTO.plagueColor());
            broadcastGameState(gameStateId);
            return ResponseEntity.ok().body(plagueId);
            
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, e.getMessage(), e);
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
    @PatchMapping("/exitGame")
    public ResponseEntity<Void> exitGame(@RequestParam("gameStateId") String gameStateId, @RequestBody PlayerId playerId){
        try{
            gameStateManager.exitGame(gameStateId, playerId.playerId());
            broadcastGameState(gameStateId);
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, e.getMessage(), e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
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
            broadcastGameState(gameStateId);

        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
            //logger.error(e.toString());
            //return ResponseEntity.badRequest().eTag(e.getMessage()).build();
        }
        return new ResponseEntity<>(HttpStatus.OK);
        
    }

    @PatchMapping("/changePlagueType")
    public ResponseEntity<Void> changePlagueType(@RequestParam("gameStateId") String gameStateId, @RequestBody ChangePlagueDTO changePlagueDTO){
        try{
            gameStateManager.changePlagueType(gameStateId, changePlagueDTO.playerId(), changePlagueDTO.diseaseType());
            broadcastGameState(gameStateId);
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
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
            broadcastGameState(gameStateId);

        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
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
    @PatchMapping("/countryChoice")
    public ResponseEntity<Void> countryChoice(@RequestParam("gameStateId") String gameStateId, @RequestBody CountryChoiceDTO chooseCountryDTO){
        logger.info("Country Choice: " + chooseCountryDTO.toString());
        try{
            gameStateManager.makeCountryChoice(gameStateId, chooseCountryDTO.playerId(), chooseCountryDTO.countryName(), chooseCountryDTO.choice());
            broadcastGameState(gameStateId);
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
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
            broadcastGameState(gameStateId);
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
            broadcastGameState(gameStateId);
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
 * @param chooseCountryDTO InfectDTO
 *
 * @return void
 *
 * @docauthor Nick Lee
 */
    @PatchMapping("/infect")
    public ResponseEntity<Void> infect(@RequestParam("gameStateId") String gameStateId, @RequestBody ChooseCountryDTO chooseCountryDTO){
        try{
            gameStateManager.attemptInfect(gameStateId, chooseCountryDTO.playerId(),chooseCountryDTO.countryName());
            broadcastGameState(gameStateId);
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
    public ResponseEntity<Integer> rollDeathDice(@RequestParam("gameStateId") String gameStateId, @RequestBody ChooseCountryDTO chooseCountryDTO){
        if(deathBool.compareAndSet(false, true)){
            try{
                int roll = gameStateManager.rollDeathDice(gameStateId, chooseCountryDTO.playerId(), chooseCountryDTO.countryName());
                TimerTask task = new TimerTask() {
                    public void run(){
                        broadcastGameState(gameStateId);
                        deathBool.set(false);
                    }
                };

                Timer timer = new Timer("Timer");
                long delay = 3000L;
                timer.schedule(task, delay);

                return ResponseEntity.ok().body(roll);
                
            }
            catch(Exception e){
                deathBool.set(false);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
            }
        }
        return new ResponseEntity<>(HttpStatus.TOO_EARLY);
        
    }

    @PatchMapping("/playEventCard")
    public ResponseEntity<Void> playEventCard(@RequestParam("gameStateId") String gameStateId, @RequestBody PlayEventCardDTO playEventCardDTO){
        try{
            gameStateManager.playEventCard(gameStateId, playEventCardDTO.playerId(), playEventCardDTO.eventCardIndex());
            broadcastGameState(gameStateId);
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
        GameState game = gameStateManager.getGameState(gameStateId);
        if(game != null){
            return ResponseEntity.ok().body(gameStateManager.getGameState(gameStateId));
        }
        return ResponseEntity.badRequest().build();
        
    }

    
/**
 * The Get Mapping for /getPlayerInfo used to
 * retrieve the non-abstracted player info
 *
 * @param gameStateId String
 * @param playerId PlayerID
 *
 * @return Info about the specified player
 *
 * @docauthor Nick Lee
 */
    @GetMapping("/getPlayerInfo")
    public ResponseEntity<PlayerInfo> getPlayerInfo(@RequestParam("gameStateId") String gameStateId, @RequestParam("playerId") UUID playerId){
        try{
            Plague plague = gameStateManager.getGameState(gameStateId).getPlague(playerId);
            List<String> hand = plague
            .getHand()
            .stream()
            .map(card -> card.name())
            .toList();

            List<String> eventCards = plague.getEventCards().stream().map(card -> card.getName()).toList();
            return ResponseEntity.ok().body(new PlayerInfo(hand, eventCards, plague));
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Player ID", e);
        }
        
        
        
    }

    private void broadcastGameState(String gameStateId){
        logger.info("Websocket sending gamestate to lobby ({})", gameStateId);
        this.template.convertAndSend("/games/gameState/" + gameStateId, gameStateManager.getGameState(gameStateId));
    }
    
}