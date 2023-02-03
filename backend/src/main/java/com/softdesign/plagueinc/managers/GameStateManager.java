package com.softdesign.plagueinc.managers;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.softdesign.plagueinc.managers.futures.input_types.CountryChoice;
import com.softdesign.plagueinc.models.countries.Country;
import com.softdesign.plagueinc.models.gamestate.GameState;
import com.softdesign.plagueinc.models.plague.PlagueColor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class GameStateManager {

    @JsonIgnore
    Logger logger = LoggerFactory.getLogger(GameStateManager.class);

    private GameState gameState;

    public GameStateManager(){
        this.gameState = new GameState();
    }

    public UUID joinGame(PlagueColor plagueColor){
        if(plagueColor == null){
            throw new IllegalArgumentException("Provided plague must have a color");
        }
        return gameState.joinGame(plagueColor);
    }

    public void voteToStart(UUID plagueId){
        gameState.startGame(plagueId);
    }

    public void proceedState(UUID plagueId){
        gameState.verifyTurn(plagueId);
        gameState.proceedState();
    }

    public Country drawCountryAction(UUID plagueId){
        gameState.verifyTurn(plagueId);
        return gameState.drawCountryAction();
    }

    public Country selectCountryFromRevealed(UUID plagueId, int index){
        gameState.verifyTurn(plagueId);
        return gameState.selectCountryFromRevealed(index);
    }

    public void playCountry(UUID plagueId){
        gameState.verifyTurn(plagueId);
        gameState.makeCountryChoice(CountryChoice.PLAY);
    }

    public void discardCountry(UUID plagueId){
        gameState.verifyTurn(plagueId);
        gameState.makeCountryChoice(CountryChoice.DISCARD);
    }

    public void evolveTrait(UUID plagueId, int traitSlot, int traitIndex){
        gameState.verifyTurn(plagueId);
        gameState.evolveTrait(traitSlot, traitIndex);
    }

    public void skipEvolve(UUID plagueId){
        gameState.verifyTurn(plagueId);
        gameState.skipEvolve();
    }

    public void attemptInfect(UUID plagueId, String countryName){
        gameState.verifyTurn(plagueId);
        gameState.attemptInfect(countryName);
    }

    public void rollDeathDice(UUID plagueId){
        gameState.verifyTurn(plagueId);
        gameState.rollDeathDice();
    }

    
}
