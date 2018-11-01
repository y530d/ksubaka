package com.jiten.ksubaka.movies;

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
import java.util.ArrayList;
import java.util.List;

@Service
public class TheMovieDBService implements MovieService {

    public static final String RESULTS = "results";
    public static final String TITLE = "title";
    public static final String RELEASE_DATE = "release_date";
    public static final String ID = "id";
    public static final String CREW = "crew";
    public static final String NAME = "name";
    public static final String JOB = "job";
    public static final String DIRECTOR = "Director";

    Logger logger = LoggerFactory.getLogger(TheMovieDBService.class);

    @Autowired
    RestTemplate restTemplate;

    @Value("#{'${movieDBAPIKey}'}")
    private String movieDBAPIKey;

    @Override
    public ResponseEntity<String> retrieveMovieInfo(String searchTerm) {


        ResponseEntity<String> searchResponse = performKeywordSearch(searchTerm);

        return retrieveAndPopulateMovieData(searchResponse.getBody());
    }

    @HystrixCommand(fallbackMethod = "serviceFailure")
    private ResponseEntity<String> performKeywordSearch(String searchTerm) {

        URI uri = URI.create("https://api.themoviedb.org/3/search/movie?query=" + searchTerm + "&api_key=" + movieDBAPIKey);
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        logger.info("MovieDB Search Response Code: " + response.getStatusCode() + ", Body = " + response.getBody());

        return response;
    }

    @HystrixCommand(fallbackMethod = "serviceFailure")
    private ResponseEntity<String> retrieveAndPopulateMovieData(String searchResponseBody) {

        JsonNode searchList;
        List<Movie> movies = new ArrayList<>();

        try {
            searchList = new ObjectMapper().readTree(searchResponseBody).get(RESULTS);

            for (JsonNode movieResult : searchList) {

                Movie movie = new Movie();

                //extract name and year from search result
                if (movieResult.hasNonNull(TITLE)) movie.Title = movieResult.get(TITLE).asText();
                if (movieResult.hasNonNull(RELEASE_DATE) && movieResult.get(RELEASE_DATE).asText().length() > 4) movie.Year = movieResult.get(RELEASE_DATE).asText().substring(0, 4);


                //make second call to get director

                if(movieResult.hasNonNull(ID)){
                    String movieId = movieResult.get(ID).asText();
                    URI uri = URI.create("https://api.themoviedb.org/3/movie/" + movieId + "/credits?api_key=" + movieDBAPIKey);
                    ResponseEntity<String> creditsResponse = restTemplate.getForEntity(uri, String.class);

                    logger.info("MovieDB Credits Look Up Response Code: " + creditsResponse.getStatusCode() + ", Body = " + creditsResponse.getBody());

                    JsonNode creditsNode = new ObjectMapper().readTree(creditsResponse.getBody());

                    if (creditsNode.hasNonNull(CREW)) {
                        JsonNode crewNode = creditsNode.get(CREW);
                        //first item in crew list is director
                        if (crewNode.isArray() && crewNode.hasNonNull(0))
                            if (crewNode.get(0).hasNonNull(JOB) && crewNode.get(0).get(JOB).asText().equals(DIRECTOR) && crewNode.get(0).hasNonNull(NAME))
                                movie.Director = crewNode.get(0).get(NAME).asText();
                    }
                }


                movies.add(movie);
            }

            MovieList movieList = new MovieList();
            movieList.movies = movies;

            return new ResponseEntity<>(new ObjectMapper().writeValueAsString(movieList), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("{\"API\": \"MovieDB Search Error\"}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> serviceFailure(String searchTerm) {

        return new ResponseEntity<>("{\"API\": \"MovieDB is currently not available\"}", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
