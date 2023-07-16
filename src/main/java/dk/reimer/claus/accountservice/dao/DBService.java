package dk.reimer.claus.accountservice.dao;


import dk.reimer.claus.accountservice.AccountServiceException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

/**
 * DataBase service for managing entities.
 */
@Service
public class DBService {
    private final AccountRepository accounts;
    private final TransactionRepository transactions;

    public DBService(AccountRepository accounts, TransactionRepository transactions) {
        this.accounts = accounts;
        this.transactions = transactions;
    }

    /**
     * Creates a new Account
     * @return a new Account
     */
    public Account createNewAccount() {
        Account account = new Account();
        account.balance = BigDecimal.ZERO;
        accounts.saveAndFlush(account);
        return account;
    }

    /**
     * Gets the balance of the given account.
     *
     * @param accountId The id of the account.
     * @return The balance.
     * @throws AccountServiceException if the account doesn't exist.
     */
    public BigDecimal getBalance(long accountId) {
        Account account = accounts.findById(accountId).orElseThrow(() -> new AccountServiceException("no such account " + accountId));
        return account.balance;
    }

    /**
     * Gets the 10 latest transactions.
     *
     * @param accountId The id of the account.
     * @return THe latest 10 transactions.
     * todo: if account doesnt exist throw exception
     */
    public List<Transaction> getLast10Transactions(long accountId) {
        return transactions.findTop10ByAccountIdOrderByDateDesc(accountId);
    }

    /**
     * Adds the given transaction and updates the account balance.
     *
     * The account is locked during the update, so it is safe to call this method from multiple threads or even
     * multiple running application instances.
     *
     * @param transaction The transaction to be added
     * @throws AccountServiceException if the account doesn't exist or if the balance will go below zero.
     * todo: we should not allow updating the transaction if it already exist.git init
     */
    @Transactional
    public void addTransaction(Transaction transaction) {
        Account account = accounts.lockAccount(transaction.accountId);
        if (account == null) {
            throw new AccountServiceException("no such account " + transaction.accountId);
        }

        BigDecimal newBalance = account.balance.add(transaction.amount);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new AccountServiceException("account overdraft, current balance " + account.balance + " requested amount " + transaction.amount + " new balance " + newBalance);
        }

        account.balance = newBalance;
        accounts.save(account);

        transactions.save(transaction);
    }
}
