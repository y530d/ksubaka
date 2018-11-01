package com.jiten.ksubaka.movies.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie {

    public String Title;
    public String Year;
    public String Director;


}
