package com.example.intern1; // Make sure this matches your project's package!

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.charset.StandardCharsets;

public class NIOOperations {

    public static void main(String[] args) {
        // Define paths for directory and file
        Path directoryPath = Paths.get("NIO"); // New directory name
        Path filePath = directoryPath.resolve("my_nio_file.txt"); // File inside it

        System.out.println("Starting NIO operations...");

        try {
            // Create a new directory
            // If the directory doesn't exist, create it.
            // Files.createDirectories also creates parent directories if needed.
            Files.createDirectories(directoryPath);
            System.out.println("Directory created (or already existed): " + directoryPath.toAbsolutePath());

            // Create a file and Write some text data into it.
            String contentToWrite = "This is a simple message from Java NIO.\nLearning to use Channels and Buffers directly!\nDone by THE David God's gift\nYes the apostrophe ' is part of my name.";

            // Convert the string to bytes
            byte[] contentBytes = contentToWrite.getBytes(StandardCharsets.UTF_8);

            // Create a ByteBuffer, sized exactly to hold our content
            ByteBuffer writeBuffer = ByteBuffer.allocate(contentBytes.length);
            writeBuffer.put(contentBytes); // Put the data into the buffer

            // 'Flip' the buffer to prepare for reading from it by the channel
            writeBuffer.flip();

            // Open a FileChannel for writing to the file
            // StandardOpenOption.CREATE_NEW will create the file only if it doesn't exist
            // StandardOpenOption.WRITE will open it for writing
            try (FileChannel writeChannel = FileChannel.open(filePath,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING)) { // TRUNCATE to clear existing content

                // Write the buffer's content to the file channel
                //writeChannel.write(writeBuffer);
                int bytesWritten = writeChannel.write(writeBuffer);
                System.out.println(bytesWritten + " bytes written.");
                System.out.println("Content written to file: " + filePath.toAbsolutePath());
            }

            // Read the file content and print it to the console
            System.out.println("\n--- Reading content from file ---");

            // Open a FileChannel for reading from the file
            try (FileChannel readChannel = FileChannel.open(filePath, StandardOpenOption.READ)) {

                // Create a ByteBuffer for reading data into
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);

                // Read bytes from the channel into the buffer
                int bytesRead = readChannel.read(readBuffer);

                // Flip the buffer to prepare for reading from it by our program
                readBuffer.flip();

                // Convert the bytes read in the buffer to a String and print
                if (bytesRead != -1) { // Make sure something was actually read
                    String readContent = new String(readBuffer.array(), 0, bytesRead, StandardCharsets.UTF_8);
                    System.out.println(readContent);
                } else {
                    System.out.println("File was empty or no content read.");
                }
            }
            System.out.println("-----------------------------------");

        } catch (IOException e) {
            System.err.println("An I/O error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}