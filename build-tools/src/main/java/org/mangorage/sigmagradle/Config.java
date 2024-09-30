package org.mangorage.sigmagradle;

public class Config {
    private final SigmaGradle plugin;
    private String serverName;
    private String serverPath;

    public Config(SigmaGradle plugin) {
        this.plugin = plugin;
    }

    public void setServer(String path, String name) {
        this.serverPath = path;
        this.serverName = name;
    }

    public String getServerPath() {
        return serverPath;
    }

    public String getServerName() {
        return serverName;
    }

}
