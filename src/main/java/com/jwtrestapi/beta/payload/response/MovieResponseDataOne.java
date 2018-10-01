package com.jwtrestapi.beta.payload.response;

import com.jwtrestapi.beta.model.Movie;
import com.jwtrestapi.beta.payload.MoviePayload;
import lombok.Data;

@Data
public class MovieResponseDataOne {

    private Boolean success;

    private MoviePayload data;

    public MovieResponseDataOne(Boolean success, MoviePayload data) {
        this.success = success;
        this.data = data;
    }
}
