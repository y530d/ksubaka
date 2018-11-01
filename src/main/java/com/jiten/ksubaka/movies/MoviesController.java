package com.jiten.ksubaka.movies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.net.URLEncoder;


@RestController
public class MoviesController {

    @Resource
    MovieService OMDBMovieService;

    @Resource
    MovieService theMovieDBService;

    Logger logger = LoggerFactory.getLogger(MoviesController.class);

    @RequestMapping(value = "/movies")
    public ResponseEntity<String> getMovieInfo(@RequestParam String searchTerm, @RequestParam String api){

        logger.info("Request - searchTerm = "+ searchTerm + ", api = "+api);

        switch(api){
            case "omdb":
                return OMDBMovieService.retrieveMovieInfo(URLEncoder.encode(searchTerm));
            case "moviedb":
                return theMovieDBService.retrieveMovieInfo(URLEncoder.encode(searchTerm));
            default:
                return new ResponseEntity<String>("{\"API\": \"not found\"}", HttpStatus.BAD_REQUEST);
        }
    }
}
