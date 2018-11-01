package com.jiten.ksubaka.movies;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiten.ksubaka.movies.dto.Movie;
import com.jiten.ksubaka.movies.dto.MovieList;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.*;

@Service
public class OMDBMovieService implements MovieService{

    public static final String SEARCH = "Search";
    public static final String IMDB_ID = "imdbID";

    Logger logger = LoggerFactory.getLogger(OMDBMovieService.class);

    @Value("#{'${omdbAPIKey}'}")
    private String omdbAPIKey;

    @Autowired
    RestTemplate restTemplate;

    @Override

    public ResponseEntity<String> retrieveMovieInfo(String searchTerm) {


        ResponseEntity<String> searchResponse = performKeywordSearch(searchTerm);

        return retrieveAndPopulateMovieData(searchResponse.getBody());

    }

    @HystrixCommand(fallbackMethod = "serviceFailure")
    private ResponseEntity<String>  performKeywordSearch(String searchTerm) {

        URI uri = URI.create("http://www.omdbapi.com/?s="+searchTerm+"&apikey="+omdbAPIKey);
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        logger.info("OMDB Search Response Code: " + response.getStatusCode() + ", Body = " + response.getBody());

        return response;
    }

    @HystrixCommand(fallbackMethod = "serviceFailure")
    private ResponseEntity<String> retrieveAndPopulateMovieData(String searchResponseBody){

        JsonNode searchList;
        List<Movie> movies = new ArrayList<>();

        try {
            searchList = new ObjectMapper().readTree(searchResponseBody).get(SEARCH);

            for(JsonNode movieResult: searchList){

                if(movieResult.has(IMDB_ID)) {
                    URI uri = URI.create("http://www.omdbapi.com/?plot=full&i=" + movieResult.get(IMDB_ID).asText() + "&apikey=" + omdbAPIKey);
                    ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

                    logger.info("OMDB Movie Look Up Response Code: " + response.getStatusCode() + ", Body = " + response.getBody());

                    ObjectMapper mapper = new ObjectMapper();

                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JsonNode movieData = mapper.readTree(response.getBody());

                    Movie movie = mapper.convertValue(movieData, Movie.class);
                    movies.add(movie);
                }
            }

            MovieList movieList = new MovieList();
            movieList.movies = movies;

            return new ResponseEntity<String>(new ObjectMapper().writeValueAsString(movieList), HttpStatus.OK );

        } catch (Exception e) {
            return new ResponseEntity<String>("{\"API\": \"OMDB Search Error\"}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> serviceFailure(String searchTerm) {

        return new ResponseEntity<String>("{\"API\": \"OMDB is currently not available\"}", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
