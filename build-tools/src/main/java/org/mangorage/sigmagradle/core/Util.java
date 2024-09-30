package org.mangorage.sigmagradle.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

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
}
