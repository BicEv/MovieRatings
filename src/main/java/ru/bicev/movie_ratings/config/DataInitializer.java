package ru.bicev.movie_ratings.config;

import ru.bicev.movie_ratings.entitites.User;
import ru.bicev.movie_ratings.utils.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import ru.bicev.movie_ratings.repositories.UserRepository;

/**
 * Initializes default data for the application on startup.
 * Specifically, it checks if an admin user exists and creates one if it does
 * not.
 */
@Component
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor for injecting dependencies.
     * 
     * @param userRepository  the repository used to access user data
     * @param passwordEncoder the password encoder used to encode password
     */
    @Autowired
    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Initializes the admin user after the application context is created.
     * If an admin with the email "admin@examole.com" does not exist, it creates one
     * with a predefined password.
     */
    @PostConstruct
    public void init() {
        if (userRepository.findByEmail("admin@example.com").isEmpty()) {
            User admin = new User();
            admin.setEmail("admin@example.com");
            admin.setUserName("admin");
            admin.setPassword(passwordEncoder.encode("admin_password"));
            admin.setRole(Role.ADMIN);

            userRepository.save(admin);
            System.out.println("Admin user created with email: admin@example.com and password: admin_password");
        } else {
            System.out.println("Admin user already exists");
        }
    }
}
