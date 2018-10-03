package com.jwtrestapi.beta.controller;

import com.jwtrestapi.beta.exception.ResourceNotFoundException;
import com.jwtrestapi.beta.model.Movie;
import com.jwtrestapi.beta.payload.MoviePayload;
import com.jwtrestapi.beta.payload.ResponseService;
import com.jwtrestapi.beta.payload.request.MovieRequest;
import com.jwtrestapi.beta.payload.response.*;
import com.jwtrestapi.beta.repository.MovieRepository;
import com.jwtrestapi.beta.service.FileStorageService;
import com.jwtrestapi.beta.service.MovieService;
import com.jwtrestapi.beta.util.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MovieController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieController.class);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    MovieRepository movieRepository;

    @Autowired
    MovieService movieService;
    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/movies/{movieId}")
    public ResponseEntity<?> getOneMovies(@PathVariable(value = "movieId") Long movieId) {
        LOGGER.info("****Start Get One Movie");
        try {
            MoviePayload movie = movieService.getOneMovie(movieId);
            LOGGER.info("****End Get One Movie");
            return ResponseEntity.ok(new ResponseDataApi("success", true, movie));
        } catch (Exception e) {
            LOGGER.error(e.getLocalizedMessage());
            LOGGER.error("****End Get One Movie");
            return new ResponseEntity(new RestApiResponse(false, "Error " + e.getMessage(), "Error"), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/movies")
    public ResponseEntity<?> getAllMovieList() {
        LOGGER.info("****Start Get All Movie List");
        List<?> movie = movieService.getAllList();
        if (movie.isEmpty()) {
            LOGGER.error("Not Found Data");
            LOGGER.info("****End Get All Movie List");
            return new ResponseEntity(new ResponseDataApi("Data Not Found!", false, null), HttpStatus.NOT_FOUND);
        }
        ResponseDataApi response = new ResponseDataApi("success", true, movie);
        LOGGER.info("****End Get All Movie List");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/movieUpload/{movieId}")
    public ResponseEntity<?> uploadFileMovie(@PathVariable(value = "movieId") Long movieId, @RequestParam("file") MultipartFile multipartFile) {

        return movieRepository.findById(movieId).map(movie -> {
            String fileName = fileStorageService.storeFile(multipartFile);
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/downloadFile/Movie/")
                    .path(fileName)
                    .toUriString();
            movie.setImgBanner(fileName);
            movieRepository.save(movie);
            return ResponseEntity.ok(new ApiResponse(true, "Success upload Image! " + fileDownloadUri));
        }).orElseThrow(() -> new ResourceNotFoundException("Movie", "movieId", movieId));
    }

    @PostMapping("/movie/create")
    public ResponseEntity<?> createMovie(@Valid @RequestBody MovieRequest movieRequest) {
        if (movieRepository.existsByFilmName(movieRequest.getFilmName())) {
            return new ResponseEntity(new ApiResponse(false, "Film is Existing!"), HttpStatus.BAD_REQUEST);
        }
        try {
            Movie movie = new Movie(movieRequest.getFilmName(), sdf.parse(movieRequest.getReleaseDate()));

            movieRepository.save(movie);
        } catch (Exception ex) {
            LOGGER.error("Error" + ex.getMessage());
            return new ResponseEntity(new ApiResponse(false, "Something Error! " + ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new ApiResponse(true, "Success Created Movie!"));
    }

    @PostMapping("/movie/update/{movieId}")
    public ResponseEntity<?> updateMovie(@PathVariable(value = "movieId") String movieId, @Valid @RequestBody MovieRequest movieRequest) {
        return movieRepository.findById(new Long(movieId)).map(movie -> {
            movie.setFilmName(movieRequest.getFilmName());
            try {
                movie.setReleaseDate(sdf.parse(movieRequest.getReleaseDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            movieRepository.save(movie);
            return new ResponseEntity(new ApiResponse(true, "Successfully update movie with id " + movieId), HttpStatus.BAD_REQUEST);
        }).orElseThrow(() -> new ResourceNotFoundException("Movie", "movieId", movieId));
    }

    @GetMapping("/movie/all")
    public ResponseEntity<MovieResponseDataAll> getAllMovie() {
        List<Movie> movies = movieRepository.findAll();
        if (movies == null) {
            return new ResponseEntity(new ApiResponse(false, "Data Not Found"), HttpStatus.BAD_REQUEST);
        }
        List<MoviePayload> payload = new ArrayList<>();
        for (Movie movie : movies) {
            payload.add(new MoviePayload(movie.getId(), movie.getFilmName(), movie.getReleaseDate().toString(), movie.getImgBanner()));
        }

        return ResponseEntity.ok(new MovieResponseDataAll(true, payload));
    }

    @GetMapping("/movie/all/response")
    public ResponseEntity<?> getAllMovies(@RequestHeader("Authorization") String token) {
        LOGGER.info("****START GET ALL MOVIES");
        ResponseService responseService = movieService.getAllMovieWithResponseService(token);

        if (!responseService.getSuccess()) {
            return new ResponseEntity(new ApiResponse(false, "Data Not Found"), HttpStatus.BAD_REQUEST);
        }
        String data = responseService.getData();
        LOGGER.info("****END ALL MOVIES");
        return ResponseEntity.ok(new RestApiResponse(true, "Success", data));
    }

    @PostMapping("/movie/delete/{movieId}")
    public ResponseEntity<?> deleteMovieById(@PathVariable(value = "movieId") String movieId) {
        if (movieId == null) {
            return new ResponseEntity(new ApiResponse(false, "Validasi Param!"), HttpStatus.BAD_REQUEST);
        }

        return movieRepository.findById(new Long(movieId)).map(movie -> {
            movieRepository.delete(movie);
            return new ResponseEntity(new ApiResponse(true, "Successfully delete movie with id " + movieId),
                    HttpStatus.BAD_REQUEST);
        }).orElseThrow(() -> new ResourceNotFoundException("Movie", "movieId", movieId));
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<?> getOneMovie(@PathVariable(value = "movieId") Long movieId) {

        return movieRepository.findById(movieId).map(movie -> {
            MoviePayload payload = new MoviePayload(movie.getId(), movie.getFilmName(), sdf.format(movie.getReleaseDate()),
                    movie.getImgBanner());
            return ResponseEntity.ok(new MovieResponseDataOne(true, payload));
        }).orElseThrow(() -> new ResourceNotFoundException("Movie", "movieId", movieId));
    }

    @PostMapping("/movie/create/response")
    public ResponseEntity<?> createMovieByResponse(@Valid @RequestBody MovieRequest movieRequest, BindingResult bindingResult) {

        ResponseService responseService = new ResponseService();
        if (!ValidationUtils.fieldValidation(bindingResult, responseService, LOGGER)) {
            return new ResponseEntity(responseService, HttpStatus.BAD_REQUEST);
        }
        responseService = movieService.createMovieWithResponseService(movieRequest);

        if (!responseService.getSuccess()) {
            return new ResponseEntity(new RestApiResponse(false, "Error " + bindingResult.getFieldError(), responseService.getData()), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(new RestApiResponse(true, "SuccessFully Create", responseService.getData()));
    }

    @GetMapping("/movie/response/{movieId}")
    public ResponseEntity<?> getOneMovieWithResponse(@PathVariable(value = "movieId") Long movieId) {
        LOGGER.info("****Start getOneMovie");
        ResponseService responseService;
        try {
            responseService = movieService.getOneMovieResponse(movieId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            LOGGER.info("****End GetOneMovie");
            return new ResponseEntity(new RestApiResponse(false, "Error " + e.getMessage(), "null"), HttpStatus.BAD_REQUEST);
        }
        LOGGER.info("****End GetOneMovie");
        return ResponseEntity.ok(new RestApiResponse(true, "Success", responseService.getData()));
    }

    @PostMapping("/movie/response/{movieId}")
    public ResponseEntity<?> updateMovieWithResponse(@PathVariable(value = "movieId") Long movieId, @RequestBody MovieRequest movieRequest) {
        LOGGER.info("***START Update Movie");
        ResponseService responseService;
        try {
            responseService = movieService.updateMovieById(movieRequest, movieId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            LOGGER.info("****End Update Movie");
            return new ResponseEntity(new RestApiResponse(false, "Error " + e.getMessage(), "Error"), HttpStatus.NOT_FOUND);
        }
        LOGGER.info("****End Update Movie " + movieId);
        return ResponseEntity.ok(new RestApiResponse(false, "Success", responseService.getData()));
    }
}
