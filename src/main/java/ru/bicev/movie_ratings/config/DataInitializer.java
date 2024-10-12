package ru.bicev.movie_ratings.config;

import ru.bicev.movie_ratings.entitites.User;
import ru.bicev.movie_ratings.utils.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import ru.bicev.movie_ratings.repositories.UserRepository;

@Component
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

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
