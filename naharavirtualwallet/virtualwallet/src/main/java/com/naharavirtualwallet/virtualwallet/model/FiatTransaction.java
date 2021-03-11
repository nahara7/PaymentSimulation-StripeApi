package com.naharavirtualwallet.virtualwallet.model;
import lombok.Data;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import java.util.Date;

@RedisHash("fiatTransaction")
@Data
/*also contained within account as a list*/
public class FiatTransaction {
    @Id
    private Long fiatTransactionId;
    private float amount;
    private Date date;
    @Indexed
    private Long fromAccountNumber;
    @Indexed
    private Long toAccountNumber;

    public FiatTransaction(float amount, Date date, Long fromAccountNumber, Long toAccountNumber) {

        this.amount = amount;
        this.date = date;
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
    }

    @Data
    public class transaction{
        private String customerId;
        private String connectedAccountId;
        private String paymentMethod;
        private Long amount;

        public transaction(){}
    }
}
