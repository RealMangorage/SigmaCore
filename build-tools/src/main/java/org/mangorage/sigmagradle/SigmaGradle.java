package org.mangorage.sigmagradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.mangorage.sigmagradle.core.TaskRegistry;
import org.mangorage.sigmagradle.tasks.MixinJarTask;
import org.mangorage.sigmagradle.tasks.PrepareServerTask;
import org.mangorage.sigmagradle.tasks.RunIgniteServer;

public class SigmaGradle implements Plugin<Project> {
    private final Config config = new Config(this);
    private final TaskRegistry taskRegistry = new TaskRegistry(config);


    public SigmaGradle() {
        taskRegistry.register(t -> {
            t.register("prepareServer", PrepareServerTask.class, config);
            t.register("runIgniteServer", RunIgniteServer.class, config);
            t.register("prepareMixinJar", MixinJarTask.class, config);
        });
    }

    @Override
    public void apply(Project target) {
        target.getExtensions().add("SigmaGradle", config);

        target.getConfigurations().create("ignite", t -> {
            t.setVisible(true);
            t.setTransitive(false);
        });

        target.getConfigurations().create("mixinJar", t -> {
            t.setVisible(true);
            t.setTransitive(false);
        });

        target.afterEvaluate(a -> {
            taskRegistry.apply(a);

            var task = target.getTasks().getByName("prepareMixinJar");

            target.getTasks().getByName("processResources").mustRunAfter(task);
            target.getTasks().getByName("processResources").dependsOn(task);
        });
    }
}
