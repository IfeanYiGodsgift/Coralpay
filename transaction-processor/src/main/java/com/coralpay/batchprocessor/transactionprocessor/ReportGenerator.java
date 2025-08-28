package com.coralpay.batchprocessor.transactionprocessor;

import org.springframework.stereotype.Component;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class ReportGenerator {

    /**
     * Generates a final summary report to a file with a unique ID and prints a simplified version to the console.
     * @param transactions The list of all transactions after processing.
     */
    public void generateReport(List<Transaction> transactions) {
        System.out.println("\nGenerating final report...");

        // Generate a unique ID for the report file
        String reportId = UUID.randomUUID().toString();
        String reportFileName = "report-" + reportId.substring(0, 8) + ".txt";

        // Calculate report data
        long totalTransactions = transactions.size();
        long successCount = transactions.stream().filter(t -> t.getStatus() == Status.SUCCESS).count();
        long failedCount = transactions.stream().filter(t -> t.getStatus() == Status.FAILED).count();

        // Find the top 3 acquirers by amount using the Stream API (equivalent to LINQ)
        Map<String, Double> acquirerAmounts = transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getAcquirerId,
                        Collectors.summingDouble(Transaction::getAmount)));

        List<Map.Entry<String, Double>> topAcquirers = acquirerAmounts.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(3)
                .collect(Collectors.toList());

        // Write the detailed report to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(reportFileName))) {
            writer.write("Summary Report (Report ID: " + reportId + ")\n");
            writer.write("----------------------------------------\n");
            writer.write(String.format("Total Transactions: %d\n", totalTransactions));
            writer.write(String.format("- Success: %d\n", successCount));
            writer.write(String.format("- Failed: %d\n", failedCount));
            writer.write("\nTop 3 Acquirers by Amount:\n");
            for (int i = 0; i < topAcquirers.size(); i++) {
                writer.write(String.format("  %d. %s - ₦%,.2f\n",
                        i + 1, topAcquirers.get(i).getKey(), topAcquirers.get(i).getValue()));
            }
            System.out.println("Detailed report file created successfully: " + reportFileName);
        } catch (IOException e) {
            System.err.println("Error writing report file: " + e.getMessage());
        }

        // Add a small delay for realism before printing the console report
        try {
            TimeUnit.MILLISECONDS.sleep(1500); // 1.5 seconds delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Print the simplified report to the console
        System.out.println("\nProcessing complete!");
        System.out.println("\nSummary Report:");
        System.out.printf("Total Transactions: %d\n", totalTransactions);
        System.out.printf("- Success: %d\n", successCount);
        System.out.printf("- Failed: %d\n", failedCount);
        System.out.println("\n- Top 3 Acquirers by Amount:");
        for (int i = 0; i < topAcquirers.size(); i++) {
            System.out.printf("  %d. %s - ₦%,.2f\n",
                    i + 1, topAcquirers.get(i).getKey(), topAcquirers.get(i).getValue());
        }
    }
}
