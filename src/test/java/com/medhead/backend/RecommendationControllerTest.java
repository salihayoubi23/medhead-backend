package com.medhead.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RecommendationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldRecommendHospitalWhenSpecialityAndBedsAvailable() throws Exception {
        String body = """
            {"speciality":"Cardiologie","originZone":"LONDON_CENTRAL"}
        """;

        mockMvc.perform(post("/recommendations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.hospitalName", notNullValue()))
            .andExpect(jsonPath("$.reason", containsString("Chosen")));
    }

    @Test
    void shouldReturnClearMessageWhenZoneIsUnknown() throws Exception {
        String body = """
            {"speciality":"Cardiologie","originZone":"UNKNOWN_ZONE"}
        """;

        mockMvc.perform(post("/recommendations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.hospitalName", nullValue()))
            .andExpect(jsonPath("$.reason", containsString("No distance data")));
    }

    @Test
    void shouldReturnClearMessageWhenNoBedsAvailable() throws Exception {
        // On teste un cas où aucune reco n'est possible.
        // Choisis une spécialité qui existe MAIS mets tous les lits à 0 dans hospitals.json,
        // OU utilise une spécialité inexistante.
        String body = """
            {"speciality":"SPECIALITE_INEXISTANTE","originZone":"LONDON_CENTRAL"}
        """;

        mockMvc.perform(post("/recommendations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.hospitalName", nullValue()))
            .andExpect(jsonPath("$.reason", notNullValue()));
    }
}
