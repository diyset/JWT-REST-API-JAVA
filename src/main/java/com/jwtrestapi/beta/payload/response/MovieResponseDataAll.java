package com.jwtrestapi.beta.payload.response;

import com.jwtrestapi.beta.payload.MoviePayload;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class MovieResponseDataAll {

   private Boolean success;
   private List<MoviePayload> data;

    public MovieResponseDataAll() {
    }

    public MovieResponseDataAll(Boolean success, List<MoviePayload> data) {
        this.success = success;
        this.data = data;
    }


}
