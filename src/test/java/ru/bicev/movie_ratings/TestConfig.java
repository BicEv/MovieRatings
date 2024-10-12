package ru.bicev.movie_ratings;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import ru.bicev.movie_ratings.controllers.GlobalExceptionHandler;

@Configuration
@EnableWebMvc
public class TestConfig {

    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

}
