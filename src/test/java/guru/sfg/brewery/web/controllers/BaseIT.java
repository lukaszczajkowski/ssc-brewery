package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

public class BaseIT {
    //this brings the Spring Security filters
    @Autowired
    WebApplicationContext context;

    MockMvc mockMvc;

    /*
    @MockBean
    BeerRepository beerRepository;

    @MockBean
    BreweryService breweryService;

    @MockBean
    BeerInventoryRepository beerInventoryRepository;

    @MockBean
    CustomerRepository customerRepository;

    @MockBean
    BeerService beerService;

     */

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                // web app context needs to be injected here
                .webAppContextSetup(context)
                // this activates spring security
                .apply(springSecurity())
                .build();
    }

    public static Stream<Arguments> getStreamAdminCustomer() {
        return Stream.of(Arguments.of("spring", "guru"),
                Arguments.of("scott", "tiger"));
    }
}
