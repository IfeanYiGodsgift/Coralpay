package com.coralpay.batchprocessor.transactionprocessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TransactionProcessorApplication implements CommandLineRunner {

    // Inject all the necessary components
    private final TransactionDataGenerator dataGenerator;
    private final TransactionProcessor transactionProcessor;

    @Autowired
    public TransactionProcessorApplication(TransactionDataGenerator dataGenerator, TransactionProcessor transactionProcessor) {
        this.dataGenerator = dataGenerator;
        this.transactionProcessor = transactionProcessor;
    }

    public static void main(String[] args) {
        SpringApplication.run(TransactionProcessorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Step 1: Generate the raw transaction data and save it to a file
        dataGenerator.generateTransactionFile();

        // Step 2: Start the main transaction processing flow.
        // The processor will now get its data from the file and handle everything else.
        transactionProcessor.processTransactions();
    }
}
