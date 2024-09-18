package ru.bicev.movie_ratings.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.bicev.movie_ratings.entitites.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

}
