package com.example.intern1;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileProcessor {

    public static void main(String[] args) {
        String inputFile = "C:\\Users\\GIFT IMUSEH\\IdeaProjects\\Intern1\\src\\main\\resources\\input.txt";
        String outputFile = "C:\\Users\\GIFT IMUSEH\\IdeaProjects\\Intern1\\src\\main\\resources\\output_reversed.txt";

        System.out.println("Starting file processing...");
        processFile(inputFile, outputFile);
        System.out.println("File processing complete. Check '" + outputFile + "'");
    }

    public static void processFile(String inputFile, String outputFile) {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            // Initialize reader and writer
            reader = new BufferedReader(new FileReader(inputFile));
            writer = new BufferedWriter(new FileWriter(outputFile));

            String line;
            while ((line = reader.readLine()) != null) {
                String reversedWord = new StringBuilder(line).reverse().toString();
                writer.write(reversedWord);
                writer.newLine();
            }
            System.out.println("Successfully reversed words from " + inputFile + " to " + outputFile);

        } catch (IOException e) {
            System.err.println("An I/O error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                System.err.println("Error closing file resources: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
