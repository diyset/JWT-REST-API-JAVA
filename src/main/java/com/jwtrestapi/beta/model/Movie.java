package com.jwtrestapi.beta.model;

import com.jwtrestapi.beta.model.audit.DateAudit;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "movie")
@Data
public class Movie extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "FilmName is Not Null!")
    @Size(min = 6)
    private String filmName;

    @NotNull(message = "ReleaseDate is Not Null!")
    private Date releaseDate;

    private String imgBanner;

    public Movie() {
    }

    public Movie(String filmName, Date releaseDate) {
        this.filmName = filmName;
        this.releaseDate = releaseDate;
    }
}
