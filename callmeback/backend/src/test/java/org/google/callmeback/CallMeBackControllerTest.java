// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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

  @Autowired private MockMvc mvc;

  @MockBean private MappingMongoConverter converter;
  @MockBean private IsNewAwareAuditingHandler mongoAuditingHandler;

  @Test
  public void getHome() throws Exception {
    mvc.perform(get("/home")).andExpect(status().isOk());
  }

  @Test
  public void getThankYou() throws Exception {
    mvc.perform(get("/thankyou")).andExpect(status().isOk());
  }

  @Test
  public void getCancel() throws Exception {
    mvc.perform(get("/cancel")).andExpect(status().isOk());
  }

  @Test
  public void getCancelConfirmation() throws Exception {
    mvc.perform(get("/cancelconfirmation")).andExpect(status().isOk());
  }

  @Test
  public void getReservations() throws Exception {
    mvc.perform(get("/reservations/api/v1/123")).andExpect(status().isOk());
  }

  @Test
  public void getUnsupportedPathReturnsNotFound() throws Exception {
    mvc.perform(get("/hithere")).andExpect(status().isNotFound());
  }
}
