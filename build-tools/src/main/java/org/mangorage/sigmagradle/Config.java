package org.mangorage.sigmagradle;

public class Config {
    private final SigmaGradle plugin;
    private String serverName;

    public Config(SigmaGradle plugin) {
        this.plugin = plugin;
    }

    public void setServer(String path) {
        this.serverName = path;
    }

    public String getServerName() {
        return serverName;
    }

}
