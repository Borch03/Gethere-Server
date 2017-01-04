package pl.edu.agh.gethere.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import pl.edu.agh.gethere.database.UserRepositoryManager;
import pl.edu.agh.gethere.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SG0222581 on 1/2/2017.
 */

@Component
public class SesameAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserRepositoryManager repositoryManager = new UserRepositoryManager();
        if (repositoryManager.isUserAuthenticated(name, password)) {
            List<UserAuthority> authorities = new ArrayList<>();
            User user = repositoryManager.getUserByName(name);
            if (user != null) {
                authorities.add(new UserAuthority(user.getRole()));
            }
            repositoryManager.tearDown();
            return new UsernamePasswordAuthenticationToken(name, password, authorities);
        } else {
            repositoryManager.tearDown();
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
