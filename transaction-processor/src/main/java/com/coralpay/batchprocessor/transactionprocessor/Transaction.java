package com.coralpay.batchprocessor.transactionprocessor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private String transactionId;
    private String acquirerId;
    private double amount;
    private LocalDateTime timestamp;
    private Status status;
}
