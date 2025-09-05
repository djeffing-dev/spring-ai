package com.djeffing.spring_ai.configs.rateLimit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)       // L'annotation peut être placée sur une méthode (ex: un endpoint @GetMapping)
@Retention(RetentionPolicy.RUNTIME) // L'annotation est disponible à l'exécution (runtime)
public @interface RateLimit {
    int limit();      // nombre max de requêtes
    long duration();  // durée en secondes (ex: 86400s = 1 jour)
}