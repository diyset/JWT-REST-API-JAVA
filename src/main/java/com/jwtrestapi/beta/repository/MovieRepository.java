package com.jwtrestapi.beta.repository;

import com.jwtrestapi.beta.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    Optional<Movie> findByFilmName(String filmName);
    Boolean existsByFilmName(String filmName);
}
