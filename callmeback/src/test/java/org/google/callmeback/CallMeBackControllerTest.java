package org.google.callmeback;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.auditing.IsNewAwareAuditingHandler;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
public class CallMeBackControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MappingMongoConverter converter;
    @MockBean
    private IsNewAwareAuditingHandler mongoAuditingHandler;

    @Test
	public void getHome() throws Exception {
        mvc.perform(get("/home"))
            .andExpect(status().isOk());
    }
    
    @Test
	public void getThankYou() throws Exception {
        mvc.perform(get("/thankyou"))
            .andExpect(status().isOk());
	}

    @Test
    public void getCancel() throws Exception {
        mvc.perform(get("/cancel"))
            .andExpect(status().isOk());
    }

    @Test
    public void getCancelConfirmation() throws Exception {
        mvc.perform(get("/cancelconfirmation"))
            .andExpect(status().isOk());
    }

    @Test
    public void getReservations() throws Exception {
        mvc.perform(get("/reservations/api/v1/123"))
            .andExpect(status().isOk());
    }

    @Test
    public void getUnsupportedPathReturnsNotFound() throws Exception {
        mvc.perform(get("/hithere"))
            .andExpect(status().isNotFound());
    }
}