package pl.edu.agh.gethere.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import pl.edu.agh.gethere.database.RepositoryManager;

import java.util.ArrayList;

/**
 * Created by SG0222581 on 1/2/2017.
 */

@Component
public class SesameAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    RepositoryManager repositoryManager;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        if (repositoryManager.isUserAuthenticated(name, password)) {
            return new UsernamePasswordAuthenticationToken(name, password, new ArrayList<>());
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
