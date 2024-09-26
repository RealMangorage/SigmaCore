package org.mangorage.boot;

import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class Boot {
    private static final String IGNITE = "ignite.jar";

    private static final Path PLUGINS = Path.of("plugins");
    private static final Path MODS = Path.of("mods");

    private static final String TEMP_NAME = "TEMP_MIXIN_JAR_";

    public static void main(String[] args) throws IOException {
        System.out.println("Started MangoRage's Bootup System...");

        if (Files.exists(PLUGINS)) {

            if (Files.exists(MODS)) {
                System.out.println("Deleting Unpacked Mods and finding Packed Ignite Mods...");
                // DELETE TEMP MODS
                Files.walk(MODS)
                        .filter(path -> path.toString().startsWith(TEMP_NAME)) // Only look for .jar files
                        .forEach(f -> {
                            try {
                                Files.delete(f);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
            } else {
                System.out.println("Finding Packed Ignite Mods...");
            }

            // Search for jar files in the rootDir
            Files.walk(PLUGINS)
                    .filter(path -> path.toString().endsWith(".jar")) // Only look for .jar files
                    .forEach(Boot::inspectJarFile);
        }

        System.out.println("Getting Ignite's Main Class");
        Manifest manifest = findManifest(Path.of(IGNITE));
        String mainClass = manifest.getMainAttributes().getValue("Main-class");
        String agentClass = manifest.getMainAttributes().getValue("Agent-Class");
        if (mainClass == null) {
            System.out.println("Failed to get Main Class");
            return;
        }
        if (agentClass == null) {
            System.out.println("Failed to get Agent Class");
            return;
        }

        System.out.println("Booting Up Ignite....");

        try {
            Class<?> agentClz = Class.forName(agentClass);
            Method agentMethod = agentClz.getDeclaredMethod("premain", String.class, Instrumentation.class);
            agentMethod.invoke(null, BootAgent.args, BootAgent.instrumentation);

            Class<?> mainClz = Class.forName(mainClass);
            Method mainMethod = mainClz.getDeclaredMethod("main", String[].class);
            mainMethod.invoke(null, (Object) args);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        System.out.println("Finished and Closing...");
    }

    /**
     * Finds the Main-Class attribute from a given jar file.
     *
     * @param jarPath The path to the jar file.
     * @return The fully qualified Main-Class name, or null if not found.
     * @throws IOException If an I/O error occurs when reading the jar file.
     */
    public static Manifest findManifest(Path jarPath) throws IOException {
        // Open the jar file
        try (JarFile jarFile = new JarFile(jarPath.toFile())) {
            // Get the manifest from the jar
            Manifest manifest = jarFile.getManifest();
            // If the manifest exists, try to get the Main-Class attribute
            if (manifest != null) {
                return manifest;
            }
        }

        // Return null if no Main-Class is found
        return null;
    }

    private static void inspectJarFile(Path jarPath) {
        try (JarFile jarFile = new JarFile(jarPath.toFile())) {
            Enumeration<JarEntry> entries = jarFile.entries();

            // Iterate over the entries inside the jar
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();

                // Check if the entry is a .jar file inside the mixinJar folder
                if (entry.getName().startsWith("mixinjar/") && entry.getName().endsWith(".jar")) {
                    System.out.println("Found mixinJar and un-packing it: " + entry.getName() + " in " + jarPath);
                    // You can process the found jar entry here
                    copyJarFile(jarFile, entry, MODS);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Function to copy the jar file from within the mixinJar folder to another directory
    private static void copyJarFile(JarFile jarFile, JarEntry entry, Path targetDir) {
        Path outputFile = targetDir.resolve(TEMP_NAME + Paths.get(entry.getName()).getFileName());

        // Create directories if they don't exist
        try {
            Files.createDirectories(targetDir);

            // Open the input stream from the jar entry and copy it to the output file
            try (InputStream inputStream = jarFile.getInputStream(entry)) {
                Files.copy(inputStream, outputFile, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Copied to: " + outputFile);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
