package pl.edu.agh.gethere.security;

import org.springframework.security.core.GrantedAuthority;
import pl.edu.agh.gethere.model.UserRole;

/**
 * Created by SG0222581 on 1/3/2017.
 */
public class UserAuthority implements GrantedAuthority {

    private UserRole userRole;

    public UserAuthority(UserRole userRole) {
        this.userRole = userRole;
    }

    @Override
    public String getAuthority() {
        return this.userRole.toString();
    }
}
