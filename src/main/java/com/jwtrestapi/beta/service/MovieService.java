package com.jwtrestapi.beta.service;

import com.jwtrestapi.beta.model.Movie;
import com.jwtrestapi.beta.payload.MoviePayload;
import com.jwtrestapi.beta.payload.ResponseService;
import com.jwtrestapi.beta.payload.request.MovieRequest;

import java.util.List;

public interface MovieService {


    MoviePayload getOneMovie(Long movieId);

    List<Movie> getAllList();

    ResponseService getAllMovieWithResponseService(String token);

    ResponseService getOneMovieResponse(Long movieId);

    ResponseService createMovieWithResponseService(MovieRequest movieRequest);

    ResponseService updateMovieById(MovieRequest movieRequest, Long movieId);
}
