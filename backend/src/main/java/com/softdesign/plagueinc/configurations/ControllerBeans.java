package com.softdesign.plagueinc.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.softdesign.plagueinc.managers.CountryManager;
import com.softdesign.plagueinc.managers.GameStateManager;
import com.softdesign.plagueinc.managers.PlagueManager;

@Configuration
public class ControllerBeans {
    
    @Bean
    public GameStateManager gameStateManager(){
        return new GameStateManager();
    }

    @Bean
    public PlagueManager plagueManager(){
        return new PlagueManager();
    }

    @Bean
    public CountryManager countryManager(){
        return new CountryManager();
    }
}
