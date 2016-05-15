package pl.edu.agh.gethere.controller;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Dominik on 14.05.2016.
 */

@Configuration
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(TripleController.class);
    }
}
