package io.quarkus.ts.startstop.utils;

import java.util.stream.Stream;

import static io.quarkus.ts.startstop.utils.Commands.getQuarkusGroupId;
import static io.quarkus.ts.startstop.utils.Commands.getQuarkusNativeProperties;
import static io.quarkus.ts.startstop.utils.Commands.getLocalMavenRepoDir;
import static io.quarkus.ts.startstop.utils.Commands.getQuarkusVersion;

/**
 * Maven commands
 */
public enum MvnCmds {
    JVM(new String[][]{
            new String[]{"mvn", "clean", "dependency:tree", "compile", "quarkus:build", "-Dquarkus.package.output-name=quarkus"},
            new String[]{Commands.JAVA_BIN, "-jar", "target/quarkus-app/quarkus-run.jar"}
    }),
    DEV(new String[][]{
            new String[]{"mvn", "clean", "quarkus:dev", "-Dmaven.repo.local=" + getLocalMavenRepoDir(), "-Dquarkus.analytics.disabled=true"}
    }),
    NATIVE(new String[][]{
            Stream.concat(Stream.of("mvn", "clean", "compile", "package", "-Pnative"),
                    getQuarkusNativeProperties().stream()).toArray(String[]::new),
            new String[]{Commands.isThisWindows ? "target\\quarkus-runner.exe" : "./target/quarkus-runner"}
    }),
    GENERATOR(new String[][]{
            new String[]{
                    "mvn",
                    getQuarkusGroupId() + ":quarkus-maven-plugin:" + getQuarkusVersion() + ":create",
                    "-DprojectGroupId=my-groupId",
                    "-DprojectArtifactId=" + Apps.GENERATED_SKELETON.dir,
                    "-DprojectVersion=1.0.0-SNAPSHOT",
                    "-DpackageName=org.my.group",
                    "-DquarkusRegistryClient=false"
            }
    }),
    MVNW_DEV(new String[][]{
            new String[]{Commands.mvnw(), "-e", "quarkus:dev", "-Dquarkus.analytics.disabled=true"}
    }),
    MVNW_JVM(new String[][]{
        new String[]{Commands.mvnw(), "clean", "dependency:tree", "compile", "quarkus:build", "-Dquarkus.package.output-name=quarkus"},
        new String[]{Commands.JAVA_BIN, "-jar", "target/quarkus-app/quarkus-run.jar"}
    }),
    MVNW_NATIVE(new String[][]{
        Stream.concat(Stream.of(Commands.mvnw(), "clean", "dependency:tree", "compile", "package", "-Pnative", "-Dquarkus.package.output-name=quarkus"),
                getQuarkusNativeProperties().stream()).toArray(String[]::new),
        new String[]{Commands.isThisWindows ? "target\\quarkus-runner" : "./target/quarkus-runner"}
    });

    public final String[][] mvnCmds;

    MvnCmds(String[][] mvnCmds) {
        this.mvnCmds = mvnCmds;
    }
}
