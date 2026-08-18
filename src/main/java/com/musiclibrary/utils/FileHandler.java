package com.musiclibrary.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    /**
     * Reads all lines from a file. If the file does not exist, it is created empty.
     */
    public static List<String> readLines(String filePath) {
        List<String> lines = new ArrayList<>();
        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            } catch (IOException e) {
                System.err.println("Warning: Could not create file " + filePath + ". " + e.getMessage());
                return lines;
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
        }

        return lines;
    }

    /**
     * Writes all lines to a file, overwriting the existing content.
     */
    public static void writeLines(String filePath, List<String> lines) {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            try {
                if (path.getParent() != null) {
                    Files.createDirectories(path.getParent());
                }
            } catch (IOException e) {
                System.err.println("Error creating directory for file: " + filePath);
                return;
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + filePath);
        }
    }
}
