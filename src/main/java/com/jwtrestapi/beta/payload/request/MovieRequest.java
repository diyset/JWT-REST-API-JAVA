package com.jwtrestapi.beta.payload.request;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class MovieRequest {
    @NotBlank(message = "FilmName is Not Null!")
    @Size(min = 6, max = 70)
    private String filmName;
    @NotNull(message = "RelaseDate is Not Null!")
    private String releaseDate;

    public MovieRequest() {
    }

    public MovieRequest(String filmName, String releaseDate) {
        this.filmName = filmName;
        this.releaseDate = releaseDate;
    }
}
