package com.jwtrestapi.beta.controller;

import com.jwtrestapi.beta.model.Movie;
import com.jwtrestapi.beta.payload.MoviePayload;
import com.jwtrestapi.beta.payload.request.MovieRequest;
import com.jwtrestapi.beta.payload.response.ApiResponse;
import com.jwtrestapi.beta.payload.response.MovieResponse;
import com.jwtrestapi.beta.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.ws.Response;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MovieController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieController.class);

    @Autowired
    MovieRepository movieRepository;

    @PostMapping("/movie/create")
    public ResponseEntity<?> createMovie(@Valid @RequestBody MovieRequest movieRequest) {
        if (movieRepository.existsByFilmName(movieRequest.getFilmName())) {
            return new ResponseEntity(new ApiResponse(false, "Film is Existing!"), HttpStatus.BAD_REQUEST);
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Movie movie = new Movie(movieRequest.getFilmName(), sdf.parse(movieRequest.getReleaseDate()));


            movieRepository.save(movie);
        } catch (Exception ex) {
            LOGGER.error("Error" + ex.getMessage());
            return new ResponseEntity(new ApiResponse(false,"Something Error!"+ex.getMessage()),HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new ApiResponse(true, "Success Created Movie!"));
    }

    @GetMapping("/movie/all")
    public ResponseEntity<MovieResponse> getAllMovie() {
        List<Movie> movies = movieRepository.findAll();
        if(movies==null){
            return new ResponseEntity(new ApiResponse(false,"Data Not Found"),HttpStatus.BAD_REQUEST);
        }
        List<MoviePayload> payload = new ArrayList<>();
        for (Movie movie: movies) {
            payload.add(new MoviePayload(movie.getId(),movie.getFilmName(),movie.getReleaseDate().toString()));
        }

        return ResponseEntity.ok(new MovieResponse(false,payload));
    }
}
