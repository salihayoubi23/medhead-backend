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
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReserveBedSuccessfully() throws Exception {

        String body = """
            {"hospitalId":"HOSP-001"}
        """;

        mockMvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.hospitalId").value("HOSP-001"))
            .andExpect(jsonPath("$.remainingBeds", greaterThanOrEqualTo(0)))
            .andExpect(jsonPath("$.message", containsString("confirmed")));
    }

    @Test
    void shouldReturn404WhenHospitalNotFound() throws Exception {

        String body = """
            {"hospitalId":"UNKNOWN_ID"}
        """;

        mockMvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", containsString("not found")));
    }

    @Test
    void shouldReturn409WhenNoBedsAvailable() throws Exception {

        // On force un hôpital déjà à 0 lit (ex: HOSP-004 chez toi)
        String body = """
            {"hospitalId":"HOSP-004"}
        """;

        mockMvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.message", containsString("No beds")));
    }
}
