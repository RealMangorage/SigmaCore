package org.mangorage.sigmagradle.tasks;

import org.gradle.api.tasks.JavaExec;
import org.mangorage.sigmagradle.Config;

import javax.inject.Inject;
import java.nio.file.Path;
import java.util.List;

public class RunIgniteServer extends JavaExec {
    @Inject
    public RunIgniteServer(Config config) {
        setDependsOn(getProject().getTasksByName("PrepareServerTask", false));
        setMustRunAfter(getProject().getTasksByName("PrepareServerTask", false));

        Path dir = getProject().getProjectDir().toPath();
        Path run = dir.resolve("run");
        Path boot = run.resolve("boot.jar");

        setClasspath(null);

        setWorkingDir(run);
        setMain("-jar");
        setArgs(List.of(
                boot.toAbsolutePath().toString()
        ));
    }
}
