package com.jwtrestapi.beta.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jwtrestapi.beta.exception.ResourceNotFoundException;
import com.jwtrestapi.beta.model.Movie;
import com.jwtrestapi.beta.payload.ResponseService;
import com.jwtrestapi.beta.payload.request.MovieRequest;
import com.jwtrestapi.beta.payload.response.MovieResponseInquiry;
import com.jwtrestapi.beta.repository.MovieRepository;
import com.jwtrestapi.beta.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service("movieService")
@Transactional
public class MovieServiceImpl implements MovieService {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final Logger LOGGER = LoggerFactory.getLogger(MovieServiceImpl.class);
    @Autowired
    MovieRepository movieRepository;

    @Override
    public List<Movie> getAllList() {
        LOGGER.info("****Start Service GetAllList");
        List<Movie> movie = movieRepository.findAll();
        LOGGER.info("****End Service");
        return movie;
    }

    @Override
    public ResponseService getAllMovieWithResponseService(String token) {
        LOGGER.info("****Start Service Get All Inquiry Movie");
        LOGGER.info("Token : " + token);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ResponseService responseService = new ResponseService();
        List<MovieResponseInquiry> movieResponseInquiry = new ArrayList<>();
        String json;
        try {
            List<Movie> movies = movieRepository.findAll();
            if (movies.size() == 0 && movies.isEmpty()) {
                LOGGER.info("Data : Data Not Found");
                LOGGER.info("****End Service Get All Inquiry Movie");
                return new ResponseService(false, "null");
            }
            for (Movie movie : movies) {
                MovieResponseInquiry response = new MovieResponseInquiry();
                response.setImgBanner(movie.getImgBanner());
                response.setReleaseDate(sdf.format(movie.getReleaseDate()));
                response.setId(movie.getId());
                response.setFilmName(movie.getFilmName());
                movieResponseInquiry.add(response);
            }
            json = gson.toJson(movieResponseInquiry);
            responseService.setSuccess(true);
            responseService.setData(json);
            LOGGER.info("Data : " + json);
        } catch (Exception e) {
            LOGGER.error("Error: " + e.getMessage());
        }
        LOGGER.info("****End Service Get All Inquiry Movie");
        return responseService;
    }

    @Override
    public ResponseService getOneMovieResponse(Long movieId) {
        LOGGER.info("****Start Service getOneMovieResponse");
        MovieResponseInquiry movieResponseInquiry = new MovieResponseInquiry();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Gson gson = new Gson();
        ResponseService responseService = new ResponseService();

        Movie movieGetData = movieRepository.findById(movieId).orElseThrow(() -> {
            LOGGER.error("Error " + new ResourceNotFoundException("Movie", "movieId", movieId).toString());
            LOGGER.info("****End Service GetOneMovieResponse");
            return new ResourceNotFoundException("Movie", "movieId", movieId);
        });

        movieResponseInquiry.setId(movieGetData.getId());
        movieResponseInquiry.setReleaseDate(movieGetData.getReleaseDate() != null ? sdf.format(movieGetData.getReleaseDate()) : "");
        movieResponseInquiry.setFilmName(movieGetData.getFilmName());
        responseService.setData(gson.toJson(movieResponseInquiry));
        responseService.setSuccess(true);
        LOGGER.info("Data : " + responseService.getData());
        LOGGER.info("****End Service getOneMovieResponse");
        return responseService;
    }

    @Override
    public ResponseService createMovieWithResponseService(@Valid MovieRequest movieRequest) {
        LOGGER.info("****Start Service Create Movie");
        Movie movie = new Movie();
        ResponseService responseService = new ResponseService();
        movie.setFilmName(movieRequest.getFilmName());
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        try {
            movie.setReleaseDate(simpleDateFormat.parse(movieRequest.getReleaseDate()));
            movieRepository.save(movie);
        } catch (ParseException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            LOGGER.info("****END SERVICE");
            new ResponseService(false, e.getMessage() + " " + movieRequest.getReleaseDate());
        }
        responseService.setSuccess(true);
        responseService.setData(gson.toJson(movieRequest));
        LOGGER.info("Data : " + responseService.getData());
        LOGGER.info("****END SERVICE");
        return responseService;
    }

    @Override
    public ResponseService updateMovieById(MovieRequest movieRequest, Long movieId) {
        LOGGER.info("****Start Service Update Movie");
        ResponseService responseService = new ResponseService();
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> {
            LOGGER.error("Error : " + new ResourceNotFoundException("Movie", "movieId", movieId));
            LOGGER.info("****END SERVICE");
            return new ResourceNotFoundException("Movie", "movieId", movieId);
        });
        try {
            movie.setId(movieId);
            movie.setReleaseDate(movieRequest.getReleaseDate() != null ? simpleDateFormat.parse(movieRequest.getReleaseDate()) : movie.getReleaseDate());
            movie.setFilmName(movieRequest.getFilmName() != null ? movieRequest.getFilmName():movie.getFilmName());
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        movieRepository.save(movie);
        responseService.setSuccess(true);
        responseService.setData("Success Update Movie Id " + movieId);
        LOGGER.info("Data : " + responseService.getData());
        LOGGER.info("****END SERVICE");

        return responseService;
    }


}
