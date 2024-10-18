package ru.bicev.movie_ratings.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.bicev.movie_ratings.entitites.User;

/**
 * Repository interface for managing {@link User} entities.
 * Provides methods for common CRUD operations and additional methods
 * to find users by email or username.
 * 
 * This interface extends {@link JpaRepository}, which provides several standard
 * data access methods.
 * 
 * Annotated with {@link Repository}, it indicates that it's a Spring-managed
 * repository bean, and it is responsible for interacting with the data source.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email adress.
     * 
     * @param email the email of the user to find.
     * @return an {@link Optional} containing the found user, or empty
     *         {@link Optional} if the user was not found
     */
    Optional<User> findByEmail(String email);

    /**
     * Finds a user by their username.
     * 
     * @param userName the username of the user to find.
     * @return an {@link Optional} containing the found user, or empty
     *         {@link Optional} if the user was not found
     */
    Optional<User> findByUserName(String userName);

}
