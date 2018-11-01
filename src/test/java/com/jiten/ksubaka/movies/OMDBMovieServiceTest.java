package com.jiten.ksubaka.movies;

import org.hibernate.validator.internal.constraintvalidators.bv.time.pastorpresent.PastOrPresentValidatorForZonedDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.regex.Matcher;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class OMDBMovieServiceTest {

    @Mock
    RestTemplate restTemplate;

    @Mock
    Logger logger;

    @InjectMocks
    OMDBMovieService omdbMovieService;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void returnsValidList(){

        ResponseEntity<Object> re1 = new ResponseEntity<>("{" +
                "    \"Search\": [" +
                "        {" +
                "            \"Title\": \"The Avengers\"," +
                "            \"Year\": \"2012\"," +
                "            \"imdbID\": \"tt0848228\"," +
                "            \"Type\": \"movie\"," +
                "            \"Poster\": \"https://m.media-amazon.com/images/M/MV5BNDYxNjQyMjAtNTdiOS00NGYwLWFmNTAtNThmYjU5ZGI2YTI1XkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_SX300.jpg\"" +
                "        }," +
                "        {" +
                "            \"Title\": \"Avengers: Age of Ultron\"," +
                "            \"Year\": \"2015\"," +
                "            \"imdbID\": \"tt2395427\"," +
                "            \"Type\": \"movie\"," +
                "            \"Poster\": \"https://m.media-amazon.com/images/M/MV5BMTM4OGJmNWMtOTM4Ni00NTE3LTg3MDItZmQxYjc4N2JhNmUxXkEyXkFqcGdeQXVyNTgzMDMzMTg@._V1_SX300.jpg\"" +
                "        }]," +
                "    \"totalResults\": \"95\"," +
                "    \"Response\": \"True\" }", HttpStatus.OK);

        ResponseEntity<Object> re2 = new ResponseEntity<>("{" +
                "            \"Title\": \"The Avengers\"," +
                "                \"Year\": \"2012\"," +
                "                \"Rated\": \"PG-13\"," +
                "                \"Released\": \"04 May 2012\"," +
                "                \"Runtime\": \"143 min\"," +
                "                \"Genre\": \"Action, Adventure, Sci-Fi\"," +
                "                \"Director\": \"Joss Whedon\"," +
                "                \"Writer\": \"Joss Whedon (screenplay), Zak Penn (story), Joss Whedon (story)\"," +
                "                \"Actors\": \"Robert Downey Jr., Chris Evans, Mark Ruffalo, Chris Hemsworth\"," +
                "                \"Plot\": \"Earth's mightiest heroes must come together and learn to fight as a team if they are going to stop the mischievous Loki and his alien army from enslaving humanity.\"," +
                "                \"Language\": \"English, Russian, Hindi\"," +
                "                \"Country\": \"USA\"," +
                "                \"Awards\": \"Nominated for 1 Oscar. Another 38 wins & 79 nominations.\"," +
                "                \"Poster\": \"https://m.media-amazon.com/images/M/MV5BNDYxNjQyMjAtNTdiOS00NGYwLWFmNTAtNThmYjU5ZGI2YTI1XkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_SX300.jpg\"," +
                "                \"Ratings\": [" +
                "            {" +
                "                \"Source\": \"Internet Movie Database\"," +
                "                    \"Value\": \"8.1/10\"" +
                "            }," +
                "            {" +
                "                \"Source\": \"Rotten Tomatoes\"," +
                "                    \"Value\": \"92%\"" +
                "            }," +
                "            {" +
                "                \"Source\": \"Metacritic\"," +
                "                    \"Value\": \"69/100\"" +
                "            }" +
                "    ]," +
                "            \"Metascore\": \"69\"," +
                "                \"imdbRating\": \"8.1\"," +
                "                \"imdbVotes\": \"1,127,047\"," +
                "                \"imdbID\": \"tt0848228\"," +
                "                \"Type\": \"movie\"," +
                "                \"DVD\": \"25 Sep 2012\"," +
                "                \"BoxOffice\": \"$623,279,547\"," +
                "                \"Production\": \"Walt Disney Pictures\"," +
                "                \"Website\": \"http://marvel.com/avengers_movie\"," +
                "                \"Response\": \"True\"" +
                "        }", HttpStatus.OK );

        ResponseEntity<Object> re3 = new ResponseEntity<>("{" +
                "    \"Title\": \"Avengers: Age of Ultron\"," +
                "    \"Year\": \"2015\"," +
                "    \"Rated\": \"PG-13\"," +
                "    \"Released\": \"01 May 2015\"," +
                "    \"Runtime\": \"141 min\"," +
                "    \"Genre\": \"Action, Adventure, Sci-Fi\"," +
                "    \"Director\": \"Joss Whedon\"," +
                "    \"Writer\": \"Joss Whedon, Stan Lee (based on the Marvel comics by), Jack Kirby (based on the Marvel comics by), Joe Simon (character created by: Captain America), Jack Kirby (character created by: Captain America), Jim Starlin (character created by: Thanos)\"," +
                "    \"Actors\": \"Robert Downey Jr., Chris Hemsworth, Mark Ruffalo, Chris Evans\"," +
                "    \"Plot\": \"When Tony Stark and Bruce Banner try to jump-start a dormant peacekeeping program called Ultron, things go horribly wrong and it's up to Earth's mightiest heroes to stop the villainous Ultron from enacting his terrible plan.\"," +
                "    \"Language\": \"English, Korean\"," +
                "    \"Country\": \"USA\"," +
                "    \"Awards\": \"7 wins & 45 nominations.\"," +
                "    \"Poster\": \"https://m.media-amazon.com/images/M/MV5BMTM4OGJmNWMtOTM4Ni00NTE3LTg3MDItZmQxYjc4N2JhNmUxXkEyXkFqcGdeQXVyNTgzMDMzMTg@._V1_SX300.jpg\"," +
                "    \"Ratings\": [" +
                "        {" +
                "            \"Source\": \"Internet Movie Database\"," +
                "            \"Value\": \"7.4/10\"" +
                "        }," +
                "        {" +
                "            \"Source\": \"Rotten Tomatoes\"," +
                "            \"Value\": \"74%\"" +
                "        }," +
                "        {" +
                "            \"Source\": \"Metacritic\"," +
                "            \"Value\": \"66/100\"" +
                "        }" +
                "    ]," +
                "    \"Metascore\": \"66\"," +
                "    \"imdbRating\": \"7.4\"," +
                "    \"imdbVotes\": \"608,504\"," +
                "    \"imdbID\": \"tt2395427\"," +
                "    \"Type\": \"movie\"," +
                "    \"DVD\": \"02 Oct 2015\"," +
                "    \"BoxOffice\": \"$429,113,729\"," +
                "    \"Production\": \"Walt Disney Pictures\"," +
                "    \"Website\": \"http://marvel.com/avengers\"," +
                "    \"Response\": \"True\"" +
                "}", HttpStatus.OK );



        ResponseEntity<String> returnObject = new ResponseEntity<>("{\"movies\":[{\"Title\":\"The Avengers\",\"Year\":\"2012\",\"Director\":\"Joss Whedon\"},{\"Title\":\"Avengers: Age of Ultron\",\"Year\":\"2015\",\"Director\":\"Joss Whedon\"}]}", HttpStatus.OK);

        when(restTemplate.getForEntity(any(),any())).thenReturn(re1).thenReturn(re2).thenReturn(re3);

        assertEquals(returnObject, omdbMovieService.retrieveMovieInfo("anyString"));
    }
}
