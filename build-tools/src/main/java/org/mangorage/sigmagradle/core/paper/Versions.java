package org.mangorage.sigmagradle.core.paper;

import com.google.gson.Gson;
import org.mangorage.sigmagradle.core.Util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public record Versions(
        String project_id,
        String project_name,
        String version,
        List<Integer> builds
) {
    private static final String BUILDS_DATA = "https://api.papermc.io/v2/projects/%s/versions/%s/";
    // https://api.papermc.io/v2/projects/paper/versions/1.21.1/builds/115/downloads/paper-1.21.1-115.jar
    private static final String BUILD_URL = "https://api.papermc.io/v2/projects/%s/versions/%s/builds/%s/downloads/%s-%s-%s.jar";
    private static final Gson GSON = new Gson();


    public static Versions getForMinecraftVersion(String mcVersion) {
        try {
            return GSON.fromJson(
                    Util.downloadRawData(BUILDS_DATA.formatted("paper", mcVersion)),
                    Versions.class
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void downloadTo(Path dest, String project_id, String mcVersion, String build) {
        if (Files.exists(dest)) return; // CACHED
        var url = BUILD_URL.formatted(project_id, mcVersion, build, project_id, mcVersion, build);
        try {
            URL websiteUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) websiteUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(5000);
            // 5 seconds timeout

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (ReadableByteChannel channel = Channels.newChannel(connection.getInputStream()); FileOutputStream fos = new FileOutputStream(dest.toFile())) {
                    fos.getChannel().transferFrom(channel,
                            0, Long.MAX_VALUE);
                }
                System.out.println("Downloaded " + url + " to " + dest);
            } else {
                throw new IOException("Error downloading data from " + url + ": HTTP status code " + responseCode);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        var a = getForMinecraftVersion("1.21.1");
        System.out.println(a.version);
        System.out.println(a.project_name);
        System.out.println(a.project_id);
        System.out.println(a.builds().size());
    }

}
