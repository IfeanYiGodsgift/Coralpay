package com.coralpay.batchprocessor.transactionprocessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class TransactionProcessor {

    private static final int BATCH_SIZE_LIMIT = 100;
    private static final double BATCH_AMOUNT_LIMIT = 1_000_000;
    private static final String BATCH_LOG_FILE = "batch_log.txt";

    // Inject the TransactionDataSource and ReportGenerator classes
    private final TransactionDataSource dataSource;
    private final ReportGenerator reportGenerator;

    @Autowired
    public TransactionProcessor(TransactionDataSource dataSource, ReportGenerator reportGenerator) {
        this.dataSource = dataSource;
        this.reportGenerator = reportGenerator;
    }

    // This method orchestrates the entire process
    public void processTransactions() {
        printWithDelay("Beginning transaction processing...");
        List<Transaction> transactions = dataSource.getTransactionsFromFile();

        printWithDelay("Creating and processing batches...");
        List<Batch> batches = createBatches(transactions);

        printWithDelay("Simulating async processing of batches...");
        processBatches(batches);

        // Generate the final report after all processing is complete
        reportGenerator.generateReport(transactions);
    }

    /**
     * Creates batches based on size and amount limits, logs full transaction details to a file,
     * and prints a simplified summary to the console with a delay.
     * @param allTransactions The full list of transactions.
     * @return A list of created batches.
     */
    private List<Batch> createBatches(List<Transaction> allTransactions) {
        List<Batch> batches = new ArrayList<>();
        List<Transaction> currentBatchTransactions = new ArrayList<>();
        double currentBatchAmount = 0.0;
        int batchCount = 1;

        for (Transaction transaction : allTransactions) {
            currentBatchTransactions.add(transaction);
            currentBatchAmount += transaction.getAmount();

            // Check if batch limits are met
            if (currentBatchTransactions.size() >= BATCH_SIZE_LIMIT || currentBatchAmount >= BATCH_AMOUNT_LIMIT) {
                String batchId = "BATCH-" + String.format("%03d", batchCount++);
                Batch newBatch = new Batch(batchId, new ArrayList<>(currentBatchTransactions));
                batches.add(newBatch);

                // Print simplified log to console with a delay
                printWithDelay(String.format("Batch %s created with %d transactions (Total ₦%,.2f)",
                        batchId, newBatch.getTransactions().size(), currentBatchAmount));

                currentBatchTransactions.clear();
                currentBatchAmount = 0.0;
            }
        }

        // Add any remaining transactions to a final batch
        if (!currentBatchTransactions.isEmpty()) {
            String batchId = "BATCH-" + String.format("%03d", batchCount);
            Batch newBatch = new Batch(batchId, currentBatchTransactions);
            batches.add(newBatch);

            // Print simplified log to console with a delay
            printWithDelay(String.format("Batch %s created with %d transactions (Total ₦%,.2f)",
                    batchId, newBatch.getTransactions().size(), currentBatchAmount));
        }

        return batches;
    }

    /**
     * Simulates asynchronous processing of each batch and writes detailed log to file.
     * @param batches The list of batches to process.
     */
    private void processBatches(List<Batch> batches) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (Batch batch : batches) {
            for (Transaction transaction : batch.getTransactions()) {
                // Use CompletableFuture to run the processing asynchronously
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    processSingleTransaction(transaction);
                });
                futures.add(future);
            }
        }

        // Wait for all async tasks to complete
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allOf.join(); // This will block until all tasks are done

        // Now that all processing is done, write to the log file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BATCH_LOG_FILE))) {
            for (Batch batch : batches) {
                writer.write(String.format("Batch %s created with %d transactions (Total ₦%,.2f)\n",
                        batch.getBatchId(), batch.getTransactions().size(), batch.getTransactions().stream().mapToDouble(Transaction::getAmount).sum()));
                for (Transaction t : batch.getTransactions()) {
                    writer.write(String.format("  - %s,%s,%.2f,%s,%s\n",
                            t.getTransactionId(), t.getAcquirerId(), t.getAmount(), t.getTimestamp(), t.getStatus().name()));
                }
                writer.newLine();
            }
            printWithDelay("Batch log file created successfully.");
        } catch (IOException e) {
            System.err.println("Error during batching process: " + e.getMessage());
        }
    }

    /**
     * Simulates the processing of a single transaction with a random delay.
     * @param transaction The transaction to process.
     */
    private void processSingleTransaction(Transaction transaction) {
        try {
            // Randomly set status to SUCCESS or FAILED
            if (ThreadLocalRandom.current().nextBoolean()) {
                transaction.setStatus(Status.SUCCESS);
            } else {
                transaction.setStatus(Status.FAILED);
            }
        } catch (Exception e) {
            System.err.println("Error processing transaction: " + transaction.getTransactionId());
        }
    }

    /**
     * Prints a message to the console with a random delay.
     * @param message The message to print.
     */
    private void printWithDelay(String message) {
        try {
            System.out.println(message);
            TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextLong(35, 70));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
