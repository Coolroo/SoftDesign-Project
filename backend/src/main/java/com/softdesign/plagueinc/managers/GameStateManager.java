package com.softdesign.plagueinc.managers;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.softdesign.plagueinc.managers.futures.input_types.CountryChoice;
import com.softdesign.plagueinc.models.countries.Country;
import com.softdesign.plagueinc.models.gamestate.GameState;
import com.softdesign.plagueinc.models.plague.Plague;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Component
public class GameStateManager {

    Logger logger = LoggerFactory.getLogger(GameStateManager.class);

    private GameState gameState;

    public Plague joinGame(){
        return gameState.joinGame();
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
