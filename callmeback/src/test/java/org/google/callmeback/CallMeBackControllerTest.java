package org.google.callmeback;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class CallMeBackControllerTest {

	@Autowired
	private MockMvc mvc;

	@Test
	public void getHome() throws Exception {
		mvc.perform(
            MockMvcRequestBuilders.get("/home").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
    }
    
    @Test
	public void getThankYou() throws Exception {
		mvc.perform(
            MockMvcRequestBuilders.get("/thankyou").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
    }
    
    @Test
	public void getCancel() throws Exception {
		mvc.perform(
            MockMvcRequestBuilders.get("/cancel").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

    @Test
	public void getCancelConfirmation() throws Exception {
		mvc.perform(
            MockMvcRequestBuilders.get("/cancelconfirmation").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
    }
    
    @Test
	public void getReservations() throws Exception {
		mvc.perform(
            MockMvcRequestBuilders.get("/reservations/api/v1/123").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void getUnsupportedPathReturnsNotFound() throws Exception {
		mvc.perform(
            MockMvcRequestBuilders.get("/hithere").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
    }
}