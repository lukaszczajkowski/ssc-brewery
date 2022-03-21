package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class BreweryControllerTest extends BaseIT {

    @Test
    void listBreweriesNoAuth() throws Exception {
        mockMvc.perform(get("/brewery/breweries"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void listBreweriesWithCustomerRole() throws Exception {
        mockMvc.perform(get("/brewery/breweries")
                .with(httpBasic("scott", "tiger")))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("breweries"))
                .andExpect(view().name("breweries/index"));
    }

    @Test
    void listBreweriesWithoutCustomerRole() throws Exception {
        mockMvc.perform(get("/brewery/breweries")
                .with(httpBasic("user", "password")))
                .andExpect(status().isForbidden());
    }

    @Test
    void getBreweriesJsonWithCustomerRole() throws Exception {
        mockMvc.perform(get("/brewery/api/v1/breweries")
                .with(httpBasic("scott", "tiger")))
                .andExpect(status().isOk());
    }

    @Test
    void getBreweriesJsonWithoutCustomerRole() throws Exception {
        mockMvc.perform(get("/brewery/api/v1/breweries")
                .with(httpBasic("user", "password")))
                .andExpect(status().isForbidden());
    }

    @Test
    void getBreweriesJsonWithNoAuth() throws Exception {
        mockMvc.perform(get("/brewery/api/v1/breweries"))
                .andExpect(status().isUnauthorized());
    }
}