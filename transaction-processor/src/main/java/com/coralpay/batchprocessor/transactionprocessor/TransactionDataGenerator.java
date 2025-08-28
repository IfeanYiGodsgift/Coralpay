package com.coralpay.batchprocessor.transactionprocessor;

import org.springframework.stereotype.Component;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class TransactionDataGenerator {

    private static final String DATA_FILE = "transactions.txt";
    private static final int NUM_TRANSACTIONS_TO_GENERATE = 1000;
    private static final String[] ACQUIRERS = {"ALPHA CORP", "Divine Group", "Victor Ltd.", "Perfect Pharma"};

    public void generateTransactionFile() {
        System.out.println("Generating transaction data and writing to file...");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE))) {
            for (int i = 0; i < NUM_TRANSACTIONS_TO_GENERATE; i++) {
                String transactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 8);
                String acquirerId = ACQUIRERS[ThreadLocalRandom.current().nextInt(ACQUIRERS.length)];
                double amount = ThreadLocalRandom.current().nextDouble(1000, 1500000);
                LocalDateTime timestamp = LocalDateTime.now();

                // Format the transaction data into a single line to be written to the file
                String transactionLine = String.format("%s,%s,%.2f,%s,%s",
                        transactionId, acquirerId, amount, timestamp, Status.PENDING.name());
                writer.write(transactionLine);
                writer.newLine(); // Move to the next line for the next transaction
            }
            System.out.println("Transaction data file created successfully.");
        } catch (IOException e) {
            System.err.println("Error writing to transaction file: " + e.getMessage());
        }
    }
}
