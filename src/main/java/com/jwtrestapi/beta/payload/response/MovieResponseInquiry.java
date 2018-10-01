package com.jwtrestapi.beta.payload.response;

import lombok.Data;

@Data
public class MovieResponseInquiry {

    private Long id;

    private String filmName;

    private String imgBanner;

    private String releaseDate;

    public MovieResponseInquiry() {
    }
}
