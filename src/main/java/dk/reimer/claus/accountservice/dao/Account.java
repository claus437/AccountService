package dk.reimer.claus.accountservice.dao;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@NamedQuery(name = "Account.lockAccount", query = "select a from Account a where id = ?1")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public BigDecimal balance;

    public Account() {
    }

    public Account(long id, BigDecimal balance) {
        this.id = id;
        this.balance = balance;
    }
}
