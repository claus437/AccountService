package dk.reimer.claus.accountservice.controller;

import dk.reimer.claus.accountservice.dao.Account;
import dk.reimer.claus.accountservice.dao.DBService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountController.class)
@MockBean(DBService.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private DBService dbs;

    @Test
    public void createAccount() throws Exception {
        when(dbs.createNewAccount()).thenReturn(new Account(101L, BigDecimal.ZERO));

        mvc.perform(MockMvcRequestBuilders
            .post("/create")
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(101L));
    }
}