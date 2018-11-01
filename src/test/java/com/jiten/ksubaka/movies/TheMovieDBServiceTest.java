package com.jiten.ksubaka.movies;

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


import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class TheMovieDBServiceTest {

    @Mock
    RestTemplate restTemplate;

    @Mock
    Logger logger;

    @InjectMocks
    TheMovieDBService theMovieDBService;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void returnsValidList(){

        ResponseEntity<Object> re1 = new ResponseEntity<>("{" +
                "    \"page\": 1," +
                "    \"total_results\": 8807," +
                "    \"total_pages\": 441," +
                "    \"results\": [" +
                "        {" +
                "            \"vote_count\": 396," +
                "            \"id\": 369972," +
                "            \"video\": false," +
                "            \"vote_average\": 7.2," +
                "            \"title\": \"First Man\"," +
                "            \"popularity\": 91.744," +
                "            \"poster_path\": \"/i91mfvFcPPlaegcbOyjGgiWfZzh.jpg\"," +
                "            \"original_language\": \"en\"," +
                "            \"original_title\": \"First Man\"," +
                "            \"genre_ids\": [" +
                "                36," +
                "                18" +
                "            ]," +
                "            \"backdrop_path\": \"/z1FkoHO7bz40S4JiptWHSYoPpxq.jpg\"," +
                "            \"adult\": false," +
                "            \"overview\": \"A look at the life of the astronaut, Neil Armstrong, and the legendary space mission that led him to become the first man to walk on the Moon on July 20, 1969.\"," +
                "            \"release_date\": \"2018-10-11\"" +
                "        }," +
                "        {" +
                "            \"vote_count\": 280," +
                "            \"id\": 439015," +
                "            \"video\": false," +
                "            \"vote_average\": 4.3," +
                "            \"title\": \"Slender Man\"," +
                "            \"popularity\": 45.982," +
                "            \"poster_path\": \"/huSncs4RyvQDBmHjBBYHSBYJbSJ.jpg\"," +
                "            \"original_language\": \"en\"," +
                "            \"original_title\": \"Slender Man\"," +
                "            \"genre_ids\": [" +
                "                9648," +
                "                53" +
                "            ]," +
                "            \"backdrop_path\": \"/5K0fgMaJSdEvqSsS8e3Ez4TpvXR.jpg\"," +
                "            \"adult\": false," +
                "            \"overview\": \"In a small town in Massachusetts, four high school girls perform a ritual in an attempt to debunk the lore of Slender Man. When one of the girls goes mysteriously missing, they begin to suspect that she is, in fact, his latest victim.\"," +
                "            \"release_date\": \"2018-08-10\"" +
                "        }]},", HttpStatus.OK);

        ResponseEntity<Object> re2 = new ResponseEntity<>("{" +
                "    \"id\": 369972," +
                "    \"cast\": [" +
                "        {" +
                "            \"cast_id\": 3," +
                "            \"character\": \"Neil Armstrong\"," +
                "            \"credit_id\": \"586577a0c3a36852ba022d96\"," +
                "            \"gender\": 2," +
                "            \"id\": 30614," +
                "            \"name\": \"Ryan Gosling\"," +
                "            \"order\": 0," +
                "            \"profile_path\": \"/5rOcicCrTCWye0O2S3dnbnWaCr1.jpg\"" +
                "        }]," + "\"crew\": [" +
                "{" +
                "\"credit_id\": \"586577839251412b8d021710\"," +
                "\"department\": \"Directing\"," +
                "\"gender\": 2," +
                "\"id\": 136495," +
                "\"job\": \"Director\"," +
                "\"name\": \"Damien Chazelle\"," +
                "\"profile_path\": \"/8LBuD2byosS7jcYakm8JmBXC0N9.jpg\"" +
                "}]}"

                , HttpStatus.OK );

        ResponseEntity<Object> re3 = new ResponseEntity<>("{" +
                "    \"id\": 369972," +
                "    \"cast\": [" +
                "        {" +
                "            \"cast_id\": 3," +
                "            \"character\": \"Neil Armstrong\"," +
                "            \"credit_id\": \"586577a0c3a36852ba022d96\"," +
                "            \"gender\": 2," +
                "            \"id\": 30614," +
                "            \"name\": \"Ryan Gosling\"," +
                "            \"order\": 0," +
                "            \"profile_path\": \"/5rOcicCrTCWye0O2S3dnbnWaCr1.jpg\"" +
                "        }]," + "\"crew\": [" +
                "{" +
                "\"credit_id\": \"586577839251412b8d021710\"," +
                "\"department\": \"Directing\"," +
                "\"gender\": 2," +
                "\"id\": 136495," +
                "\"job\": \"Director\"," +
                "\"name\": \"Damien Chazelle\"," +
                "\"profile_path\": \"/8LBuD2byosS7jcYakm8JmBXC0N9.jpg\"" +
                "}]}", HttpStatus.OK );



        ResponseEntity<String> returnObject = new ResponseEntity<>("{\"movies\":[{\"Title\":\"First Man\",\"Year\":\"2018\",\"Director\":\"Damien Chazelle\"},{\"Title\":\"Slender Man\",\"Year\":\"2018\",\"Director\":\"Damien Chazelle\"}]}", HttpStatus.OK);

        when(restTemplate.getForEntity(any(),any())).thenReturn(re1).thenReturn(re2).thenReturn(re3);

        assertEquals(returnObject, theMovieDBService.retrieveMovieInfo("anyString"));
    }
}
