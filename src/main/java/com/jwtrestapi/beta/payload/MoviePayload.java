package com.jwtrestapi.beta.payload;

import lombok.Data;

@Data
public class MoviePayload {

    private Long id;
    private String filmName;
    private String releaseDate;
    private String imgBanner;
    public MoviePayload() {
    }

    public MoviePayload(Long id, String filmName, String releaseDate, String imgBanner) {
        this.id = id;
        this.filmName = filmName;
        this.releaseDate = releaseDate;
        this.imgBanner = imgBanner;
    }
}
