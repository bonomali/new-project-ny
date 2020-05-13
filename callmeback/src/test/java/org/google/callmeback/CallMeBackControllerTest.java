package org.google.callmeback;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class CallMeBackControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void getHome() throws Exception {
        mvc.perform(get("/home").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void getThankYou() throws Exception {
        mvc.perform(get("/thankyou").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void getCancel() throws Exception {
        mvc.perform(get("/cancel").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void getCancelConfirmation() throws Exception {
        mvc.perform(get("/cancelconfirmation").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void getReservations() throws Exception {
        mvc.perform(get("/reservations/api/v1/123").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void getUnsupportedPathReturnsNotFound() throws Exception {
        mvc.perform(get("/hithere").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
}