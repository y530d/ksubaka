package com.jiten.ksubaka.movies;

import org.springframework.http.ResponseEntity;

public interface MovieService {

    ResponseEntity<String> retrieveMovieInfo(String searchTerm);

    ResponseEntity<String> serviceFailure(String searchTerm);

}
