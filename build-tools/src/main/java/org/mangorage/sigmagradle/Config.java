package org.mangorage.sigmagradle;

public class Config {
    private final SigmaGradle plugin;
    private String serverName;
    private String mcVersion;
    private String build;

    public Config(SigmaGradle plugin) {
        this.plugin = plugin;
    }

    public void setServer(String name) {
        this.serverName = name;
    }

    public void setMCVersion(String version) {
        this.mcVersion = version;
    }

    public String getMCVersion() {
        return mcVersion;
    }

    public String getServerName() {
        return serverName;
    }

}
