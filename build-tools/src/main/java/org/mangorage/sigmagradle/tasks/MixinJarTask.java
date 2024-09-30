package org.mangorage.sigmagradle.tasks;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.mangorage.sigmagradle.Config;
import org.mangorage.sigmagradle.core.Util;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class MixinJarTask extends DefaultTask {
    @Inject
    public MixinJarTask(Config config) {
    }

    @TaskAction
    public void run() {
        var mixins = getProject().getConfigurations().getByName("mixinJar").getFiles();
        Path dir = getProject().getProjectDir().toPath();
        Path mixinJar = dir.resolve("build/resources/main/mixinjar");

        try {
            if (Files.exists(mixinJar)) {
                Files.delete(mixinJar);
            }
            if (!mixins.isEmpty())
                Files.createDirectories(mixinJar);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        mixins.forEach(m -> {
            try {
                Files.copy(
                        m.toPath(),
                        mixinJar.resolve(m.getName()),
                        StandardCopyOption.REPLACE_EXISTING
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }
}
