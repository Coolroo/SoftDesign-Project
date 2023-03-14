package com.softdesign.plagueinc.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.softdesign.plagueinc.managers.futures.input_types.CountryChoice;
import com.softdesign.plagueinc.models.gamestate.GameState;
import com.softdesign.plagueinc.models.plague.DiseaseType;
import com.softdesign.plagueinc.models.plague.PlagueColor;

@Component
public class GameStateManager {

    @JsonIgnore
    Logger logger = LoggerFactory.getLogger(GameStateManager.class);
    
    private Map<String, GameState> games;

    public GameStateManager(){
        this.games = new HashMap<>();
    }

    public String createGame(){
        String randomId;
        while(games.containsKey(randomId = generateRandomId())){}
        games.put(randomId, new GameState());
        logger.info("[UTIL] Created game lobby with ID ({})", randomId);
        return randomId;
    }

    public UUID joinGame(String gameStateId, PlagueColor plagueColor){
        if(plagueColor == null){
            throw new IllegalArgumentException("Provided plague must have a color");
        }
        if(!games.containsKey(gameStateId)){
            throw new IllegalArgumentException("Provided game state does not exist");
        }
        return games.get(gameStateId).joinGame(plagueColor);
    }

    public void exitGame(String gameStateId, UUID playerId){
        if(playerId == null){
            throw new IllegalArgumentException("Provided playerID does not exist");
        }
        if(!games.containsKey(gameStateId)){
            throw new IllegalArgumentException("Provided game state does not exist");
        }
        
        //Only player in this lobby
        if(games.get(gameStateId).getPlagues().size() == 1){

        }
        else{
            games.get(gameStateId).
        }
    }

    public void voteToStart(String gameStateId, UUID plagueId){
        if(plagueId == null){
            throw new IllegalArgumentException("must provide plague ID");
        }
        if(gameStateId == null){
            throw new IllegalArgumentException("Must provide gameStateId");
        }
        games.get(gameStateId).startGame(plagueId);
    }

    public void changePlagueType(String gameStateId, UUID plagueId, DiseaseType diseaseType){
        if(plagueId == null){
            throw new IllegalArgumentException("must provide plague ID");
        }
        if(gameStateId == null){
            throw new IllegalArgumentException("Must provide gameStateId");
        }
        games.get(gameStateId).changePlagueType(plagueId, diseaseType);
    }

    public void proceedState(String gameStateId, UUID plagueId){
        if(plagueId == null){
            throw new IllegalArgumentException("must provide plague ID");
        }
        if(gameStateId == null){
            throw new IllegalArgumentException("Must provide gameStateId");
        }
        games.get(gameStateId).verifyTurn(plagueId);
        games.get(gameStateId).proceedState();
    }

    public void makeCountryChoice(String gameStateId, UUID playerId, String countryName, CountryChoice choice){
        if(playerId == null){
            throw new IllegalArgumentException("must provide plague ID");
        }
        if(countryName == null){
            throw new IllegalArgumentException("must provide Country ID");
        }
        if(choice == null){
            throw new IllegalArgumentException("must provide Country choice");
        }
        if(gameStateId == null){
            throw new IllegalArgumentException("Must provide gameStateId");
        }
        games.get(gameStateId).verifyTurn(playerId);
        games.get(gameStateId).makeCountryChoice(countryName, choice);
    }

    public void evolveTrait(String gameStateId, UUID plagueId, int traitSlot, int traitIndex){
        if(plagueId == null){
            throw new IllegalArgumentException("must provide plague ID");
        }
        if(gameStateId == null){
            throw new IllegalArgumentException("Must provide gameStateId");
        }
        games.get(gameStateId).verifyTurn(plagueId);
        games.get(gameStateId).evolveTrait(traitSlot, traitIndex);
    }

    public void skipEvolve(String gameStateId, UUID plagueId){
        if(plagueId == null){
            throw new IllegalArgumentException("must provide plague ID");
        }
        if(gameStateId == null){
            throw new IllegalArgumentException("Must provide gameStateId");
        }
        games.get(gameStateId).verifyTurn(plagueId);
        games.get(gameStateId).skipEvolve();
    }

    public void attemptInfect(String gameStateId, UUID plagueId, String countryName){
        if(plagueId == null){
            throw new IllegalArgumentException("must provide plague ID");
        }
        if(gameStateId == null){
            throw new IllegalArgumentException("Must provide gameStateId");
        }
        if(countryName == null){
            throw new IllegalArgumentException("Must provide countryName");
        }
        games.get(gameStateId).verifyTurn(plagueId);
        games.get(gameStateId).attemptInfect(countryName);
    }

    public int rollDeathDice(String gameStateId, UUID plagueId, String countryName){
        if(plagueId == null){
            throw new IllegalArgumentException("must provide plague ID");
        }
        if(gameStateId == null){
            throw new IllegalArgumentException("Must provide gameStateId");
        }
        games.get(gameStateId).verifyTurn(plagueId);
        return games.get(gameStateId).rollDeathDice(countryName);
    }

    public void playEventCard(String gameStateId, UUID plagueId, int eventCardIndex){
        if(plagueId == null){
            throw new IllegalArgumentException("must provide plague ID");
        }
        if(gameStateId == null){
            throw new IllegalArgumentException("Must provide gameStateId");
        }
        games.get(gameStateId).playEventCard(eventCardIndex, plagueId);
    }

    public GameState getGameState(String gameStateId){
        if(gameStateId == null){
            throw new IllegalArgumentException("Must provide gameStateId");
        }
        return games.get(gameStateId);
    }

    private static String generateRandomId(){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 4;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
        .limit(targetStringLength)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();
        return generatedString.toUpperCase();
    }

    
}
