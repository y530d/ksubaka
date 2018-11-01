package com.jiten.ksubaka.movies;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.containsString;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MoviesControllerMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnBadRequest() throws Exception {
        this.mockMvc.perform(get("/movies")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnOkWithContentFromOMDB() throws Exception {
        this.mockMvc.perform(get("/movies?searchTerm=man&api=omdb")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("movies")));
    }

    @Test
    public void shouldReturnOkWithContentFromMovieDB() throws Exception {
        this.mockMvc.perform(get("/movies?searchTerm=man&api=moviedb")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("movies")));
    }

    @Test
    public void shouldReturnBadRequestBecauseOfAPIName() throws Exception {
        this.mockMvc.perform(get("/movies?searchTerm=man&api=movied")).andDo(print()).andExpect(status().isBadRequest());
    }

}
