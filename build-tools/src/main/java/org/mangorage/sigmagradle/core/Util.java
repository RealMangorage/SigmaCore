package org.mangorage.sigmagradle.core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Util {

    public static void deleteDirectory(Path directoryPath) throws IOException {
        File directory = directoryPath.toFile();

        if (!directory.exists()) {
            return;
        }

        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file.toPath()); // Recursively delete subdirectories
                    } else {
                        file.delete();
                    }
                }
            }
        }

        directory.delete(); // Delete the empty directory
    }


    public static void editFile(Path filePath, Function<String, String> editor) {
        try {
            // Read the file content into a StringBuilder
            StringBuilder content = new StringBuilder();
            Scanner scanner = new Scanner(filePath);
            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine()).append("\n");
            }
            scanner.close();

            // Apply the editor function to each line of the content
            String editedContent = content.toString().lines()
                    .map(editor)
                    .collect(Collectors.joining("\n"));

            // Write the edited content back to the file
            PrintWriter writer = new PrintWriter(new FileWriter(filePath.toString()));
            writer.write(editedContent);
            writer.close();

            System.out.println("File edited successfully!");
        } catch (IOException e) {
            System.err.println("Error occurred while editing file: " + e.getMessage());
        }
    }
}
