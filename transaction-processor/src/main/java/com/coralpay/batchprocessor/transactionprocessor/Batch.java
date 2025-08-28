package com.coralpay.batchprocessor.transactionprocessor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Batch {
    private String batchId;
    private List<Transaction> transactions;
}
