package ch.bbw.web;

import ch.bbw.Bank;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Tests for the REST endpoints under /bank and for the CORS configuration
 *
 * @author Cedric Caviezel, cedric.caviezel@stud.kbw.ch
 * @version 1.0
 */
@SpringBootTest
public class BankControllerTests {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    /**
     * Empties the bank before every test and builds MockMvc
     */
    @BeforeEach
    public void setUp() {
        Bank.reset();
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    /**
     * Creates a savings account with the given amount on it
     * @param amount the amount to deposit
     * @throws Exception
     */
    private void createSavingsAccountWith(long amount) throws Exception {
        String body = mockMvc.perform(post("/accounts/savings")).andReturn().getResponse().getContentAsString();
        String id = JsonPath.read(body, "$.id");
        mockMvc.perform(post("/accounts/" + id + "/deposit").contentType(MediaType.APPLICATION_JSON).content("{\"amount\": " + amount + "}"));
    }

    /**
     * An empty bank has a balance of 0
     * @throws Exception
     */
    @Test
    @DisplayName("An empty bank having a balance of 0 should work")
    public void testEmptyBankBalance() throws Exception {
        mockMvc.perform(get("/bank/balance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(0));
    }

    /**
     * The balance of the bank is the sum of all accounts
     * @throws Exception
     */
    @Test
    @DisplayName("The balance of the bank through the API should work")
    public void testBankBalance() throws Exception {
        createSavingsAccountWith(100);
        createSavingsAccountWith(500);

        mockMvc.perform(get("/bank/balance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(600));
    }

    /**
     * The top 5 list returns the 5 highest balances, the highest first
     * @throws Exception
     */
    @Test
    @DisplayName("The top 5 highest balances through the API should work")
    public void testTop5Highest() throws Exception {
        for (int i = 1; i <= 6; i++) {
            createSavingsAccountWith(i * 100);
        }

        mockMvc.perform(get("/bank/top5/highest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].balance").value(600))
                .andExpect(jsonPath("$[4].balance").value(200));
    }

    /**
     * The bottom 5 list returns the 5 lowest balances, the lowest first
     * @throws Exception
     */
    @Test
    @DisplayName("The top 5 lowest balances through the API should work")
    public void testTop5Lowest() throws Exception {
        for (int i = 1; i <= 6; i++) {
            createSavingsAccountWith(i * 100);
        }

        mockMvc.perform(get("/bank/top5/lowest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].balance").value(100))
                .andExpect(jsonPath("$[4].balance").value(500));
    }

    /**
     * The Vite dev server on localhost:5173 is allowed by CORS
     * @throws Exception
     */
    @Test
    @DisplayName("CORS allowing the Vite dev server should work")
    public void testCorsAllowsFrontend() throws Exception {
        mockMvc.perform(options("/accounts/savings")
                        .header("Origin", "http://localhost:5173")
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5173"));
    }

    /**
     * Another origin is not allowed by CORS
     * @throws Exception
     */
    @Test
    @DisplayName("CORS rejecting another origin should work")
    public void testCorsRejectsOtherOrigin() throws Exception {
        mockMvc.perform(options("/accounts/savings")
                        .header("Origin", "http://evil.example.com")
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isForbidden());
    }
}
