package dk.reimer.claus.accountservice.controller;

import dk.reimer.claus.accountservice.AccountServiceException;
import dk.reimer.claus.accountservice.dao.Account;
import dk.reimer.claus.accountservice.dao.DBService;
import dk.reimer.claus.accountservice.dao.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Rest Controller for using account and transactions methods.
 */
@RestController
public class AccountController {
    private final Logger logger = LoggerFactory.getLogger(AccountController.class);
    private final DBService dbs;

    public AccountController(DBService dbs) {
        this.dbs = dbs;
    }

    /**
     * Creates a new account.
     *
     * @return The new account.
     */
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public Account createAccount() {
        Account account = dbs.createNewAccount();
        logger.info("created account " + account.id);
        return account;
    }

    /**
     * Gets the balance of the given account.
     *
     * @param accountId The account id to get the balance of.
     * @return The balance.
     * @throws AccountServiceException if the given account doesn't exist.
     */
    @RequestMapping(value = "balance/{accountId}", method = RequestMethod.GET)
    public BigDecimal getBalance(@PathVariable long accountId) throws AccountServiceException  {
        BigDecimal balance = dbs.getBalance(accountId);
        logger.info("fetched balance " + balance + " of account " + accountId);
        return balance;
    }

    /**
     * Add a new transaction with the given amount to the account's transactions list.
     *
     * If the transaction is added successfully the account balance is updated accordingly.
     *
     * @param accountId The id of the account to add the transaction to.
     * @param amount The amount to be added to the balance (negative to deduct te balance).
     * @return The transaction details.
     * @throws AccountServiceException if the account doesn't exist, or if the balance would go below zero after deducting the given amount.
     */
    @RequestMapping(value = "transaction/add/{accountId}", method = RequestMethod.POST)
    public Transaction addTransaction(@PathVariable long accountId, @RequestBody BigDecimal amount) {
        Transaction transaction = new Transaction(0, accountId, new Date(), amount);
        dbs.addTransaction(transaction);
        logger.info("added transaction of " + amount + " to account " + accountId);
        return transaction;
    }

    /**
     * Lists the latest 10 transactions for the given account.
     *
     * @param accountId The id of the account.
     * @return The latest 10 transactions.
     */
    @RequestMapping (value = "transaction/list/{accountId}")
    public List<Transaction> getLatest10Transactions(@PathVariable long accountId) {
        List<Transaction> transactions =  dbs.getLast10Transactions(accountId);
        logger.info("listing " + transactions.size() + " transactions of account " + accountId);
        return transactions;
    }
}
