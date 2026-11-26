package com.jobtrackr;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class JobApplicationControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    void createsAnApplicationWithDefaultStatus() throws Exception {
        mvc.perform(post("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"company\":\"Initech\",\"role\":\"Java Developer\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("WISHLIST"))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    void rejectsAnApplicationWithoutCompany() throws Exception {
        mvc.perform(post("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"role\":\"Java Developer\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.company").exists());
    }

    @Test
    void statsIncludeEveryStatus() throws Exception {
        mvc.perform(get("/api/applications/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.WISHLIST").isNumber())
                .andExpect(jsonPath("$.OFFER").isNumber());
    }

    @Test
    void unknownIdReturns404() throws Exception {
        mvc.perform(get("/api/applications/999999")).andExpect(status().isNotFound());
    }
}
