package dev.golddiggerapi.security.principal;

import io.jsonwebtoken.Claims;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class UserPrincipal extends User {

    private final Long id;

    private final String username;

    public UserPrincipal(dev.golddiggerapi.user.domain.User user) {
        super(user.getUsername(), user.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.id = user.getId();
        this.username = user.getUsername();
    }

    public UserPrincipal(Claims claims) {
        super(claims.getSubject(), "", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.id = claims.get("id", Long.class);
        this.username = claims.get("username", String.class);
    }
}
