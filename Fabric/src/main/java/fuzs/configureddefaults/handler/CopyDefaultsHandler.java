package fuzs.configureddefaults.handler;

import fuzs.configureddefaults.ConfiguredDefaults;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Predicate;

public class CopyDefaultsHandler {
    public static final String DEFAULTS_DIRECTORY = ConfiguredDefaults.MOD_ID;
    public static final String README_FILE = "README.md";
    private static final String README_CONTENTS = """
            # %1$s
            
            This whole directory servers as a synchronized mirror of `.minecraft`. Every sub-directory and / or file placed within will be copied to the main `.minecraft` directory during game launch if the directory / file is not already present.
            There is no way of overriding an existing file, a copy will only be made when the target destination is empty.
            
            Please note that due to the way Minecraft handles `options.txt` specifically it is sufficient to include only the options you want to set a preset for. All missing options will be filled in using their internal defaults when the file is read by the game.
            
            Examples:
            - `.minecraft/%2$s/options.txt` will be copied to `.minecraft/options.txt` if not already present
            - `.minecraft/%2$s/config/jei/jei.toml` will be copied to `.minecraft/config/jei/jei.toml` if not already present
            
            Note that this `README.md` file is excluded from being copied to `.minecraft`.
            """.formatted(ConfiguredDefaults.MOD_NAME, ConfiguredDefaults.MOD_ID);

    private static boolean initialized;

    public static void initialize(Path gamePath) {
        // this is constructed twice, we only need to run once
        // (once when Forge is counting available language loaders, and the second time when the language loader is actually used)
        if (!initialized) {
            initialized = true;
            ConfiguredDefaults.LOGGER.info("Applying default files...");
            Path defaultPresetsPath = gamePath.resolve(DEFAULTS_DIRECTORY);
            Path readmePath = defaultPresetsPath.resolve(README_FILE);
            try {
                trySetupFresh(gamePath, defaultPresetsPath, readmePath);
                Set<Path> blacklistedPaths = Set.of(defaultPresetsPath, readmePath, defaultPresetsPath.resolve(".DS_Store"));
                tryCopyFiles(gamePath, defaultPresetsPath, blacklistedPaths::contains);
            } catch (IOException exception) {
                ConfiguredDefaults.LOGGER.error("Failed to setup default files", exception);
            }
        }
    }

    private static void trySetupFresh(Path gamePath, Path defaultPresetsPath, Path readmePath) throws IOException {
        Path gameParentPath = gamePath.getParent();
        if (Files.notExists(defaultPresetsPath)) {
            if (!defaultPresetsPath.toFile().mkdir()) {
                ConfiguredDefaults.LOGGER.info("Failed to create fresh '{}' directory", relativizeAndNormalize(gameParentPath, defaultPresetsPath));
                return;
            } else {
                ConfiguredDefaults.LOGGER.info("Successfully created fresh '{}' directory", relativizeAndNormalize(gameParentPath, defaultPresetsPath));
            }
        }
        if (Files.notExists(readmePath)) {
            Files.write(readmePath, README_CONTENTS.getBytes());
            ConfiguredDefaults.LOGGER.info("Successfully created fresh '{}' file", relativizeAndNormalize(gameParentPath, readmePath));
        }
    }

    private static void tryCopyFiles(Path gamePath, Path defaultPresetsPath, Predicate<Path> filter) throws IOException {
        Path gameParentPath = gamePath.getParent();
        Files.walk(defaultPresetsPath).forEach(sourcePath -> {
            if (!filter.test(sourcePath)) {
                try {
                    Path targetPath = relativizeAndNormalize(gamePath, defaultPresetsPath.relativize(sourcePath));
                    // check if file already exists, otherwise copy will throw an exception
                    if (sourcePath.toFile().exists() && !targetPath.toFile().exists()) {
                        try {
                            // we do not need to handle creating parent directories as the file tree is traversed depth-first
                            Files.copy(sourcePath, targetPath);
                            ConfiguredDefaults.LOGGER.info("Successfully copied '{}' to '{}'", relativizeAndNormalize(gameParentPath, sourcePath), relativizeAndNormalize(gameParentPath, targetPath));
                        } catch (IOException e) {
                            ConfiguredDefaults.LOGGER.info("Failed to copy '{}' to '{}'", relativizeAndNormalize(gameParentPath, sourcePath), relativizeAndNormalize(gameParentPath, targetPath));
                        }
                    }
                } catch (Throwable throwable) {
                    ConfiguredDefaults.LOGGER.error("Oh no!", throwable);
                }
            }
        });
    }

    private static Path relativizeAndNormalize(Path parentPath, Path path) {
        return parentPath.toAbsolutePath().relativize(path.toAbsolutePath()).normalize();
    }
}
