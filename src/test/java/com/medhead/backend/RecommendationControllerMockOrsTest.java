package com.medhead.backend;

import com.medhead.backend.service.OrsDistanceService;
import com.medhead.backend.service.OrsDistanceService.OrsResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RecommendationControllerMockOrsTest {

    @Autowired
    private MockMvc mockMvc;

    // Remplace OrsDistanceService du contexte Spring par un mock Mockito
    @MockBean
    private OrsDistanceService orsDistanceService;

    @Test
    void shouldReturnRecommendationWithoutCallingRealORS() throws Exception {
        // Peu importe les coordonnées, on renvoie une valeur stable
        when(orsDistanceService.route(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(new OrsResult(1234.0, 600.0)); // 1.234 km, 10 min

        String body = """
            {"speciality":"Cardiologie","originZone":"LONDON_CENTRAL"}
        """;

        mockMvc.perform(post("/recommendations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hospitalId", notNullValue()))
                .andExpect(jsonPath("$.hospitalName", notNullValue()))
                .andExpect(jsonPath("$.distanceKm", is(1.2)))   // arrondi à 1 décimale dans ton controller
                .andExpect(jsonPath("$.durationMin", is(10)))
                .andExpect(jsonPath("$.reason", containsString("ORS")));
    }
}
