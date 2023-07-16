package dk.reimer.claus;

import dk.reimer.claus.accountservice.Application;
import dk.reimer.claus.accountservice.dao.Account;
import dk.reimer.claus.accountservice.dao.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTest {

    @Value("http://localhost:${local.server.port}")
    private String address;

    @Autowired
    private TestRestTemplate rest;

    @Test
    public void createAccount() {
        Account account = rest.postForEntity(address + "/create", null, Account.class).getBody();
        assertNotEquals(0, account.id);
        assertEquals(BigDecimal.ZERO, account.balance);
    }

    @Test
    public void deposit() {
        Account account = rest.postForEntity(address + "/create", null, Account.class).getBody();

        Transaction transaction = rest.postForEntity(address + "/transaction/add/" + account.id, BigDecimal.valueOf(100), Transaction.class).getBody();
        assertEquals(0, BigDecimal.valueOf(100).compareTo(transaction.amount));

        BigDecimal balance = rest.getForEntity(address + "/balance/" + account.id, BigDecimal.class).getBody();
        assertEquals(0, BigDecimal.valueOf(100).compareTo(balance));
    }

    @Test
    public void withdraw() {
        Account account = rest.postForEntity(address + "/create", null, Account.class).getBody();

        Transaction transaction = rest.postForEntity(address + "/transaction/add/" + account.id, BigDecimal.valueOf(100), Transaction.class).getBody();
        assertEquals(0, BigDecimal.valueOf(100).compareTo(transaction.amount));

        transaction = rest.postForEntity(address + "/transaction/add/" + account.id, BigDecimal.valueOf(-25), Transaction.class).getBody();
        assertEquals(0, BigDecimal.valueOf(-25).compareTo(transaction.amount));

        BigDecimal balance = rest.getForEntity(address + "/balance/" + account.id, BigDecimal.class).getBody();
        assertEquals(0, BigDecimal.valueOf(75).compareTo(balance));
    }
}