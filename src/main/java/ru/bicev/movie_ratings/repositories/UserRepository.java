package ru.bicev.movie_ratings.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.bicev.movie_ratings.entitites.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
