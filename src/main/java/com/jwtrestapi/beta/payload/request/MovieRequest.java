package com.jwtrestapi.beta.payload.request;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class MovieRequest {
    @NotBlank
    @Size(min = 6)
    private String filmName;
    @NotNull
    private String releaseDate;

    public MovieRequest() {
    }

    public MovieRequest(String filmName, String releaseDate) {
        this.filmName = filmName;
        this.releaseDate = releaseDate;
    }
}
