package ru.bicev.movie_ratings.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import ru.bicev.movie_ratings.entitites.User;

/**
 * Custom implementation of {@link UserDetails} for Spring Security.
 * This class adapts the application's {@link User} entity to work with Spring
 * Security authentication system.
 */
public class CustomUserDetails implements UserDetails {

    private final User user;

    /**
     * Constructor to initialize with a {@link User} entity
     * 
     * @param user the user entity from which details will be extracted for
     *             authentication
     */
    public CustomUserDetails(User user) {
        this.user = user;
    }

    /**
     * Returns the authorities to the user. This method converts the user's role
     * into a Spring Security authority.
     * 
     * @return a collection of granted authorities (roles) assigned to the user
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("ROLE_" + user.getRole().name());
    }

    /**
     * Returns the user's password for authentication.
     * 
     * @return the user's password
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Returns the usename (in this case, the user's email) for authentication.
     * 
     * @return the user's email, which act like username
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * Indicates whether the user's account has expired. This implementation always
     * returns true, meaning the account is valid.
     *
     * @return true, indicating the account is not expired.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is locked. This implementation always returns
     * true, meaning the account is not locked.
     *
     * @return true, indicating the account is not locked.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) have expired. This
     * implementation always returns true.
     *
     * @return true, indicating the credentials are not expired.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled. This implementation always
     * returns true.
     *
     * @return true, indicating the user is enabled.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

}
