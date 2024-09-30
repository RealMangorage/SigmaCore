package org.mangorage.sigmagradle.tasks;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.mangorage.sigmagradle.Config;
import org.mangorage.sigmagradle.core.Util;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class PrepareServerTask extends DefaultTask {
    private final Config config;
    @Inject
    public PrepareServerTask(Config config) {
        mustRunAfter(getProject().getTasks().getByName("build"));
        dependsOn(getProject().getTasks().getByName("build"));
        this.config = config;
    }

    private File getServerFile(Path root) {
        return root.resolve(config.getServerPath()).toFile();
    }

    @TaskAction
    public void run() {

        var dir = getProject().getProjectDir().toPath();
        var run = dir.resolve("run");
        var plugins = run.resolve("plugins");

        var plugin = getProject().getTasks().getByName("jar").getOutputs().getFiles().getSingleFile();
        var boot = getProject().getConfigurations().getByName("boot").getSingleFile();
        var ignite = getProject().getConfigurations().getByName("ignite").getSingleFile();
        var server = getServerFile(run);

        try {
            Files.copy(
                    plugin.toPath(),
                    plugins.resolve("plugin.jar"),
                    StandardCopyOption.REPLACE_EXISTING
            );
            Files.copy(
                    ignite.toPath(),
                    run.resolve("ignite.jar"),
                    StandardCopyOption.REPLACE_EXISTING
            );
            Files.copy(
                    server.toPath(),
                    run.resolve(config.getServerName() + ".jar"),
                    StandardCopyOption.REPLACE_EXISTING
            );
            Files.copy(
                    boot.toPath(),
                    run.resolve("boot.jar"),
                    StandardCopyOption.REPLACE_EXISTING
            );


            var eula = run.resolve("eula.txt");
            if (Files.exists(eula)) {
                Util.editFile(eula, old -> {
                    return old.replaceFirst("false", "true");
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
