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

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Tests for the REST endpoints under /accounts
 *
 * @author Cedric Caviezel, cedric.caviezel@stud.kbw.ch
 * @version 1.0
 */
@SpringBootTest
public class AccountControllerTests {
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
     * Creates a savings account and returns its id
     * @return the account id
     * @throws Exception
     */
    private String createSavingsAccount() throws Exception {
        String body = mockMvc.perform(post("/accounts/savings")).andReturn().getResponse().getContentAsString();

        return JsonPath.read(body, "$.id");
    }

    /**
     * Creating a savings account answers with 201 and the new id
     * @throws Exception
     */
    @Test
    @DisplayName("Creating a savings account through the API should work")
    public void testCreateSavingsAccount() throws Exception {
        mockMvc.perform(post("/accounts/savings"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", startsWith("S-")));
    }

    /**
     * Creating a promo youth savings account answers with 201 and the new id
     * @throws Exception
     */
    @Test
    @DisplayName("Creating a promo youth savings account through the API should work")
    public void testCreatePromoYouthSavingsAccount() throws Exception {
        mockMvc.perform(post("/accounts/promo-youth"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", startsWith("Y-")));
    }

    /**
     * Creating a salary account with a credit limit answers with 201 and the new id
     * @throws Exception
     */
    @Test
    @DisplayName("Creating a salary account through the API should work")
    public void testCreateSalaryAccount() throws Exception {
        mockMvc.perform(post("/accounts/salary").contentType(MediaType.APPLICATION_JSON).content("{\"creditLimit\": -50000}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", startsWith("P-")));
    }

    /**
     * A positive credit limit is answered with 400 and an error message
     * @throws Exception
     */
    @Test
    @DisplayName("A positive credit limit being answered with 400 should work")
    public void testCreateSalaryAccountWithPositiveLimit() throws Exception {
        mockMvc.perform(post("/accounts/salary").contentType(MediaType.APPLICATION_JSON).content("{\"creditLimit\": 50000}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    /**
     * Getting an account returns its id, type and balance
     * @throws Exception
     */
    @Test
    @DisplayName("Getting an account through the API should work")
    public void testGetAccount() throws Exception {
        String id = createSavingsAccount();

        mockMvc.perform(get("/accounts/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.type").value("Savings Account"))
                .andExpect(jsonPath("$.balance").value(0));
    }

    /**
     * An unknown account id is answered with 404 and an error message
     * @throws Exception
     */
    @Test
    @DisplayName("An unknown account being answered with 404 should work")
    public void testUnknownAccount() throws Exception {
        mockMvc.perform(get("/accounts/S-9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    /**
     * A deposit returns the new balance
     * @throws Exception
     */
    @Test
    @DisplayName("Depositing through the API should work")
    public void testDeposit() throws Exception {
        String id = createSavingsAccount();

        mockMvc.perform(post("/accounts/" + id + "/deposit").contentType(MediaType.APPLICATION_JSON).content("{\"amount\": 1500, \"text\": \"This is a test text\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(1500));
        mockMvc.perform(get("/accounts/" + id + "/balance"))
                .andExpect(jsonPath("$.balance").value(1500));
    }

    /**
     * A withdrawal returns the new balance
     * @throws Exception
     */
    @Test
    @DisplayName("Withdrawing through the API should work")
    public void testWithdraw() throws Exception {
        String id = createSavingsAccount();
        mockMvc.perform(post("/accounts/" + id + "/deposit").contentType(MediaType.APPLICATION_JSON).content("{\"amount\": 1500}"));

        mockMvc.perform(post("/accounts/" + id + "/withdraw").contentType(MediaType.APPLICATION_JSON).content("{\"amount\": 500}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(1000));
    }

    /**
     * Withdrawing more than the balance of a savings account is answered with 400, not 500
     * @throws Exception
     */
    @Test
    @DisplayName("Withdrawing too much being answered with 400 should work")
    public void testWithdrawTooMuch() throws Exception {
        String id = createSavingsAccount();

        mockMvc.perform(post("/accounts/" + id + "/withdraw").contentType(MediaType.APPLICATION_JSON).content("{\"amount\": 1}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Amount is greater than balance"));
    }

    /**
     * A date in the past or a date that can't be read is answered with 400
     * @throws Exception
     */
    @Test
    @DisplayName("A date in the past or a wrong date being answered with 400 should work")
    public void testInvalidDate() throws Exception {
        String id = createSavingsAccount();

        mockMvc.perform(post("/accounts/" + id + "/deposit").contentType(MediaType.APPLICATION_JSON).content("{\"amount\": 1500, \"date\": \"2000-01-01T00:00:00Z\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        mockMvc.perform(post("/accounts/" + id + "/deposit").contentType(MediaType.APPLICATION_JSON).content("{\"amount\": 1500, \"date\": \"yesterday\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    /**
     * The bookings of an account are returned with amount and text
     * @throws Exception
     */
    @Test
    @DisplayName("Getting the bookings of an account through the API should work")
    public void testBookings() throws Exception {
        String id = createSavingsAccount();
        mockMvc.perform(post("/accounts/" + id + "/deposit").contentType(MediaType.APPLICATION_JSON).content("{\"amount\": 1500, \"text\": \"This is a test text\"}"));
        mockMvc.perform(post("/accounts/" + id + "/withdraw").contentType(MediaType.APPLICATION_JSON).content("{\"amount\": 500}"));

        mockMvc.perform(get("/accounts/" + id + "/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].amount").value(1500))
                .andExpect(jsonPath("$[0].text").value("This is a test text"))
                .andExpect(jsonPath("$[1].amount").value(-500))
                .andExpect(jsonPath("$[1].text").value("Auszahlung"));
    }
}
