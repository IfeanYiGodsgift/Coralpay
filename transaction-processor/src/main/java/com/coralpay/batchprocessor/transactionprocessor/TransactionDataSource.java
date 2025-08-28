package com.coralpay.batchprocessor.transactionprocessor;

import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionDataSource {

    private static final String DATA_FILE = "transactions.txt";

    /**
     * Reads transaction data from a file and returns a list of Transaction objects.
     * @return A list of Transaction objects.
     */
    public List<Transaction> getTransactionsFromFile() {
        List<Transaction> transactions = new ArrayList<>();
        System.out.println("Reading transaction data from file...");

        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line by the comma delimiter
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    // Reconstruct the Transaction object from the file data
                    String transactionId = parts[0];
                    String acquirerId = parts[1];
                    double amount = Double.parseDouble(parts[2]);
                    LocalDateTime timestamp = LocalDateTime.parse(parts[3]);
                    Status status = Status.valueOf(parts[4]);

                    transactions.add(new Transaction(transactionId, acquirerId, amount, timestamp, status));
                }
            }
            System.out.printf("Successfully read %d transactions from file.\n", transactions.size());
        } catch (IOException e) {
            System.err.println("Error reading transaction file: " + e.getMessage());
        }
        return transactions;
    }
}
