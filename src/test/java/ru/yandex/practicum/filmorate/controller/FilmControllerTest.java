package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = DEFINED_PORT)
@AutoConfigureMockMvc
public class FilmControllerTest {
    @Autowired TestRestTemplate testRestTemplate;
    @Autowired MockMvc mvc;

    @Test
    public void shouldCreateNewFilm() throws Exception {
        String json = "{\n" +
                "  \"name\": \"nisi eiusmod\",\n" +
                "  \"description\": \"adipisicing\",\n" +
                "  \"releaseDate\": \"1967-03-25\",\n" +
                "  \"duration\": 100\n" +
                "}";

        mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/api/v1/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                        .andExpect(status().isOk());
    }
}
