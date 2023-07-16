package dk.reimer.claus.accountservice.dao;

import dk.reimer.claus.accountservice.AccountServiceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(DBService.class)
class DBServiceTest {
    @Autowired
    private DBService service;

    @Test
    public void createAccount() {
        Account account = service.createNewAccount();
        assertNotEquals(0, account.id);
    }

    @Test
    public void getBalance() {
        Account account = service.createNewAccount();
        assertEquals(BigDecimal.ZERO, account.balance);
    }

    @Test
    public void updateAccount() {
        Account account = service.createNewAccount();
        assertEquals(BigDecimal.ZERO, account.balance);

        service.addTransaction(new Transaction(0, account.id, new Date(), BigDecimal.valueOf(150)));
        assertEquals(BigDecimal.valueOf(150), service.getBalance(account.id));

        service.addTransaction(new Transaction(0, account.id, new Date(), BigDecimal.valueOf(25)));
        assertEquals(BigDecimal.valueOf(175), service.getBalance(account.id));

        service.addTransaction(new Transaction(0, account.id, new Date(), BigDecimal.valueOf(-125)));
        assertEquals(BigDecimal.valueOf(50), service.getBalance(account.id));
    }

    @Test
    public void overdraft() {
        Account account = service.createNewAccount();

        service.addTransaction(new Transaction(0, account.id, new Date(), BigDecimal.valueOf(150)));

        Transaction transaction = new Transaction(0, account.id, new Date(), BigDecimal.valueOf(-151));
        assertThrows(AccountServiceException.class, () -> service.addTransaction(transaction));
    }

    @Test
    public void last10Transactions() {
        Account account = service.createNewAccount();

        List<Transaction> transactions = new ArrayList<>();
        long time = new Date().getTime();
        for (int i = 0; i < 20; i ++) {
            time += TimeUnit.HOURS.toMillis(1);
            Transaction transaction = new Transaction(0, account.id, new Date(time),BigDecimal.valueOf(i));
            transactions.add(transaction);
            service.addTransaction(transaction);
        }

        transactions.sort(Comparator.comparingLong(a -> a.date.getTime()));
        Collections.reverse(transactions);

        List<Long> actual = service.getLast10Transactions(account.id).stream().map((t) -> t.id).toList();
        List<Long> expected = transactions.subList(0, 10).stream().map((t) -> t.id).toList();

        assertArrayEquals(expected.toArray(), actual.toArray());
    }
}