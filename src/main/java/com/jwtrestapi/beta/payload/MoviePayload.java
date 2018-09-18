package com.jwtrestapi.beta.payload;

import lombok.Data;

@Data
public class MoviePayload {

    private Long id;
    private String filmName;
    private String releaseDate;

    public MoviePayload() {
    }

    public MoviePayload(Long id, String filmName, String releaseDate) {
        this.id = id;
        this.filmName = filmName;
        this.releaseDate = releaseDate;
    }
}
