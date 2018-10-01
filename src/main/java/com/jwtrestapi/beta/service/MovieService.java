package com.jwtrestapi.beta.service;

import com.jwtrestapi.beta.model.Movie;
import com.jwtrestapi.beta.payload.ResponseService;
import com.jwtrestapi.beta.payload.request.MovieRequest;

import java.util.List;
import java.util.Optional;

public interface MovieService {


    List<Movie> getAllList();

    ResponseService getAllMovieWithResponseService(String token);

    ResponseService getOneMovieResponse(Long movieId);

    ResponseService createMovieWithResponseService(MovieRequest movieRequest);

    ResponseService updateMovieById(MovieRequest movieRequest, Long movieId);
}
