package ru.bicev.movie_ratings.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ru.bicev.movie_ratings.entitites.User;
import ru.bicev.movie_ratings.repositories.UserRepository;
import ru.bicev.movie_ratings.security.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(identifier)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Username with email: " + identifier + " is not found"));
        System.out.println("Loaded user password: " + user.getPassword());
        return new CustomUserDetails(user);
    }
}
