package ru.bicev.movie_ratings.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ru.bicev.movie_ratings.entitites.User;
import ru.bicev.movie_ratings.repositories.UserRepository;
import ru.bicev.movie_ratings.security.CustomUserDetails;

/**
 * Custom iplementaiton of{@link UserDetailsService} to load user-specific data.
 * This service is used by Spring Security for authentification.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructor the initializes the service with a {@link UserRepository}
     * 
     * @param userRepository repository for accessing user data
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads a user by their email, which serves as the username in this application
     * 
     * @param identifier the email of the user to load
     * @return {@link UserDetails} for the authenticated user
     * @throws UsernameNotFoundException if no user with the given email exists
     */
    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(identifier)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Username with email: " + identifier + " is not found"));
        System.out.println("Loaded user password: " + user.getPassword());
        return new CustomUserDetails(user);
    }
}
