package dk.reimer.claus.accountservice.dao;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public long accountId;
    public Date date;
    public BigDecimal amount;

    public Transaction() {}

    public Transaction(long id, long accountId, Date date, BigDecimal amount) {
        this.id = id;
        this.accountId = accountId;
        this.date = date;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", date=" + date +
                ", amount=" + amount +
                '}';
    }
}
