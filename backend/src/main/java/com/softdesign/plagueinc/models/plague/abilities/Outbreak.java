package com.softdesign.plagueinc.models.plague.abilities;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.softdesign.plagueinc.models.action_log.InfectCountryAction;
import com.softdesign.plagueinc.models.countries.Country;
import com.softdesign.plagueinc.models.gamestate.GameState;
import com.softdesign.plagueinc.models.gamestate.PlayState;
import com.softdesign.plagueinc.models.gamestate.selection_objects.CitySelection;
import com.softdesign.plagueinc.models.plague.Plague;

public class Outbreak extends Ability {

    public Outbreak() {
        super("outbreak");
    }

    //TODO: Implement checks to ensure player does not become soft-locked

    @Override
    public void resolveAbility(GameState gameState){
        //Ensure we are in an appropriate state
        if(gameState.getPlayState() != PlayState.INFECT || gameState.isReadyToProceed()){
            logger.warn("Player attempted to play outbreak in an invalid state, (PlayState:{}), (readyToProceed:{})", gameState.getPlayState(), gameState.isReadyToProceed());
            return;
        }
        //Initialize the ability checker
        gameState.setPlayState(PlayState.ABILITY_ACTIVATION);
        gameState.setCitySelectionFuture(Optional.of(selectPlayerToken(gameState)));
    }

    private CompletableFuture<CitySelection> selectPlayerToken(GameState gameState){
        CompletableFuture<CitySelection> future = new CompletableFuture<>();
        future.whenComplete((result, ex) -> {
            if(ex != null){
                logger.warn("Outbreak future failed for EX: {}", ex.getMessage());
                gameState.setCitySelectionFuture(Optional.of(selectPlayerToken(gameState)));
            }
            else{
                try{
                    //Get the queried city and ensure that the player controls it
                    Plague city = gameState.getBoard()
                    .values()
                    .stream()
                    .flatMap(continent -> continent.stream())
                    .filter(country -> country.equals(result.country()))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new)
                    .getCities()
                    .get(result.city())
                    .orElseThrow(IllegalStateException::new);

                    if(city.equals(gameState.getCurrTurn())){
                        //Move on to the next step of outbreak;
                        gameState.setCitySelectionFuture(Optional.of(selectEmptyCity(gameState, result)));
                        return;
                    }

                }
                catch(IllegalArgumentException e){
                    logger.error("Outbreak attempted to infect country that does not exist ", result.country().getCountryName());
                }
                catch(IllegalStateException e){
                    logger.error("Outbreak attempted to select a city that the player does not control");
                }
                catch(Exception e){
                    logger.error(e.getMessage());
                }
                finally{
                    gameState.setCitySelectionFuture(Optional.of(selectPlayerToken(gameState)));
                }
                
            }
        });
        return future;
    }

    private CompletableFuture<CitySelection> selectEmptyCity(GameState gameState, CitySelection citySelection){
        CompletableFuture<CitySelection> emptyCityFuture = new CompletableFuture<>();

        emptyCityFuture.whenComplete((result, ex) -> {
            if(ex != null){
                logger.error("Error with empty city selection EX: {}", ex.getMessage());
                gameState.setCitySelectionFuture(Optional.of(selectEmptyCity(gameState, citySelection)));
            }
            else{
                try{
                    //Ensure the city is empty
                    Country country = gameState.getBoard()
                    .values()
                    .stream()
                    .flatMap(continent -> continent.stream())
                    .filter(thisCountry -> thisCountry.equals(result.country()))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);

                    if(country.getCities().get(result.city()).isPresent()){
                        throw new IllegalStateException();
                    }
                    Country takingCountry = gameState.getBoard()
                    .values()
                    .stream()
                    .flatMap(continent -> continent.stream())
                    .filter(thisCountry -> thisCountry.equals(citySelection.country()))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);

                    //Remove token from old city, and place one in new city
                    takingCountry.getCities().set(citySelection.city(), Optional.empty());
                    country.getCities().set(result.city(), Optional.of(gameState.getCurrTurn()));

                    //Clear future, and update game state
                    gameState.setCitySelectionFuture(Optional.empty());
                    gameState.setReadyToProceed(true);
                    gameState.logAction(new InfectCountryAction(country));
                    gameState.setPlayState(PlayState.INFECT);
                    return;
                }
                catch(IllegalArgumentException e){
                    logger.error("Outbreak attempted to select a country that doesn't exist {}", result.country().getCountryName());
                }
                catch(IllegalStateException e){
                    logger.error("Outbreak attempted to infect a city that is already infected (Country: {}, City: {})", result.country().getCountryName(), result.city());
                }
                catch(Exception e){
                    logger.error(e.getMessage());
                }
                finally{
                    gameState.setCitySelectionFuture(Optional.of(selectEmptyCity(gameState, citySelection)));
                }
            }
        });

        return emptyCityFuture;
    }
}
