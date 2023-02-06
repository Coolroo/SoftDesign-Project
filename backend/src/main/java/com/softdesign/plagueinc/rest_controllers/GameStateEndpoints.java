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

/**
 * The @PostMapping(&quot;/createGame&quot;)
    public function accomplishes:
 * 
 * 
 *
 * @param esponseEntity&lt;String Return a string to the client
 *
 * @return A responseentity&lt;string&gt;
 *
 * @docauthor Trelent
 */
    @PostMapping("/createGame")
    public ResponseEntity<String> createGame(){
        try{
            return ResponseEntity.ok().body(gameStateManager.createGame());
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

/**
 * The @PostMapping(&quot;/joinGame&quot;)
    public function accomplishes:
 * 
 * 
 *
 * @param esponseEntity&lt;UUID Return the uuid of the game state
 *
 * @return A responseentity&lt;uuid&gt; which is a type of httpstatus
 *
 * @docauthor Trelent
 */
    @PostMapping("/joinGame")
    public ResponseEntity<UUID> joinGame(@RequestParam("gameStateId") String gameStateId, @RequestBody JoinGameDTO joinGameDTO){
        try{
            return ResponseEntity.ok().body(gameStateManager.joinGame(gameStateId, joinGameDTO.plagueColor()));
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
        }
    }

/**
 * The @PostMapping(&quot;/voteToStart&quot;)
    public function accomplishes:
 * 
 * 
 *
 * @param esponseEntity&lt;Void Return no data
 *
 * @return ?
 *
 * @docauthor Trelent
 */    
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

/**
 * The @PostMapping(&quot;/proceedState&quot;)
    public function accomplishes:
 * 
 * 
 *
 * @param esponseEntity&lt;Void Return no data
 *
 * @return A responseentity&lt;void&gt;?
 *
 * @docauthor Trelent
 */
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

/**
 * The @PostMapping(&quot;/drawCountry&quot;)
    public function accomplishes:
 *  - Draws a country from the GameState's deck of countries.
 *  - Returns the drawn Country object to the caller.
 
 *
 * @param esponseEntity&lt;Country Return the country that was drawn
 *
 * @return A responseentity&lt;country&gt; object
 *
 * @docauthor Trelent
 */
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

/**
 * The @PostMapping(&quot;/takeCountry&quot;)
    public function accomplishes:
 * 
 * 
 *
 * @param esponseEntity&lt;Country Return the country object to the caller
 *
 * @return A responseentity&lt;country&gt;?
 *
 * @docauthor Trelent
 */
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

/**
 * The @PostMapping(&quot;/playCountry&quot;)
    public function accomplishes:
 * 
 * 
 *
 * @param esponseEntity&lt;Void Return a 204 no content response
 *
 * @return A responseentity&lt;void&gt; object
 *
 * @docauthor Trelent
 */
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

/**
 * The @PostMapping(&quot;/discardCountry&quot;)
    public function accomplishes:
 * 
 * 
 *
 * @param esponseEntity&lt;Void Return a response with no body
 *
 * @return ?
 *
 * @docauthor Trelent
 */
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

/**
 * The @PostMapping(&quot;/evolve&quot;)
    public function accomplishes:
 * 
 * 
 *
 * @param esponseEntity&lt;Void Return an empty response
 *
 * @return ?
 *
 * @docauthor Trelent
 */
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

/**
 * The @PostMapping(&quot;/skipEvolution&quot;)
    public function accomplishes:
 * 
 * 
 *
 * @param esponseEntity&lt;Void Return no content
 *
 * @return ?
 *
 * @docauthor Trelent
 */
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

/**
 * The @PostMapping(&quot;/infect&quot;)
    public function accomplishes:
 * 
 * 
 *
 * @param esponseEntity&lt;Void Return a 204 no content response
 *
 * @return A responseentity&lt;void&gt;?
 *
 * @docauthor Trelent
 */
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

/**
 * The @PostMapping(&quot;/rollDeathDice&quot;)
    public function accomplishes:
 * 
 * 
 *
 * @param esponseEntity&lt;Integer Return the number of death dice rolled
 *
 * @return A responseentity&lt;integer&gt;?
 *
 * @docauthor Trelent
 */
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


/**
 * The @GetMapping(&quot;/gameState&quot;)
    public function accomplishes:
 * 
 * 
 *
 * @param esponseEntity&lt;GameState Return a gamestate object
 *
 * @return A responseentity&lt;gamestate&gt;
 *
 * @docauthor Trelent
 */    //GET Mappings
    @GetMapping("/gameState")
    public ResponseEntity<GameState> getGameState(@RequestParam("gameStateId") String gameStateId){
        return ResponseEntity.ok().body(gameStateManager.getGameState(gameStateId));
    }

/**
 * The @GetMapping(&quot;/getHand&quot;)
    public function accomplishes:
 * 
 * 
 *
 * @param esponseEntity&lt;List&lt;String&gt; Return the list of cards in hand
 *
 * @return A list of strings
 *
 * @docauthor Trelent
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
            return ResponseEntity.badRequest().eTag("Invalid player ID").build();
        }
        
        
        
    }
    
}