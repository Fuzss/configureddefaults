package fuzs.configureddefaults.handler;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import fuzs.configureddefaults.ConfiguredDefaults;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

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
                                                  """.formatted(ConfiguredDefaults.MOD_NAME, DEFAULTS_DIRECTORY);
    private static final String OPTIONS_FILE = "options.txt";
    private static final Splitter OPTION_SPLITTER = Splitter.on(':').limit(2);

    private static boolean initialized;

    public static void initialize(Path path, boolean mergeOptions) {
        // this is constructed twice, we only need to run once
        // (once when Forge is counting available language loaders, and the second time when the language loader is actually used)
        if (!initialized) {
            initialized = true;
            ConfiguredDefaults.LOGGER.info("Applying default files...");
            try {
                path = path.toAbsolutePath();
                setupIfNecessary(path);
                copyFiles(path, mergeOptions);
                if (mergeOptions) {
                    mergeOptions(path);
                }
            } catch (IOException exception) {
                ConfiguredDefaults.LOGGER.error("Failed to setup default files", exception);
            }
        }
    }

    private static void setupIfNecessary(Path path) throws IOException {
        Path defaultPresetsPath = path.resolve(DEFAULTS_DIRECTORY);
        Path gameParentPath = path.getParent();
        if (Files.notExists(defaultPresetsPath)) {
            if (!defaultPresetsPath.toFile().mkdir()) {
                ConfiguredDefaults.LOGGER.info("Failed to create fresh '{}' directory",
                        relativizeAndNormalize(gameParentPath, defaultPresetsPath));
                return;
            } else {
                ConfiguredDefaults.LOGGER.info("Successfully created fresh '{}' directory",
                        relativizeAndNormalize(gameParentPath, defaultPresetsPath));
            }
        }
        Path readmePath = defaultPresetsPath.resolve(README_FILE);
        if (Files.notExists(readmePath)) {
            Files.write(readmePath, README_CONTENTS.getBytes());
            ConfiguredDefaults.LOGGER.info("Successfully created fresh '{}' file",
                    relativizeAndNormalize(gameParentPath, readmePath));
        }
    }

    private static void copyFiles(Path path, boolean mergeOptions) throws IOException {
        Path defaultPresetsPath = path.resolve(DEFAULTS_DIRECTORY);
        Path gameParentPath = path.getParent();
        Set<Path> exclusionPaths = getExclusionPaths(defaultPresetsPath, mergeOptions);
        Files.walk(defaultPresetsPath).forEach((Path sourcePath) -> {
            if (!exclusionPaths.contains(sourcePath)) {
                try {
                    Path targetPath = relativizeAndNormalize(path, defaultPresetsPath.relativize(sourcePath));
                    // check if file already exists, otherwise copy will throw an exception
                    if (sourcePath.toFile().exists() && !targetPath.toFile().exists()) {
                        try {
                            // we do not need to handle creating parent directories as the file tree is traversed depth-first
                            Files.copy(sourcePath, targetPath);
                            ConfiguredDefaults.LOGGER.info("Successfully copied '{}' to '{}'",
                                    relativizeAndNormalize(gameParentPath, sourcePath),
                                    relativizeAndNormalize(gameParentPath, targetPath));
                        } catch (IOException e) {
                            ConfiguredDefaults.LOGGER.info("Failed to copy '{}' to '{}'",
                                    relativizeAndNormalize(gameParentPath, sourcePath),
                                    relativizeAndNormalize(gameParentPath, targetPath));
                        }
                    }
                } catch (Throwable throwable) {
                    ConfiguredDefaults.LOGGER.error("Oh no!", throwable);
                }
            }
        });
    }

    private static Set<Path> getExclusionPaths(Path defaultPresetsPath, boolean mergeOptions) {
        Set<Path> paths = new HashSet<>(Arrays.asList(defaultPresetsPath,
                defaultPresetsPath.resolve(README_FILE),
                defaultPresetsPath.resolve(".DS_Store")));
        if (mergeOptions) paths.add(defaultPresetsPath.resolve(OPTIONS_FILE));
        return ImmutableSet.copyOf(paths);
    }

    private static Path relativizeAndNormalize(Path parentPath, Path path) {
        return parentPath.toAbsolutePath().relativize(path.toAbsolutePath()).normalize();
    }

    private static void mergeOptions(Path path) {
        Map<String, String> options = new LinkedHashMap<>();
        File file = path.resolve(OPTIONS_FILE).toFile();
        loadOptions(file, options);
        int size = options.size();
        // compare size as we only allow adding new options via Map::putIfAbsent,
        // so only if the size value changes we must rewrite the file
        if (loadOptions(path.resolve(DEFAULTS_DIRECTORY).resolve(OPTIONS_FILE).toFile(), options) &&
                options.size() != size) {
            saveOptions(file, options);
        }
    }

    private static boolean loadOptions(File file, Map<String, String> options) {
        if (file.exists()) {
            try (BufferedReader bufferedReader = com.google.common.io.Files.newReader(file, Charsets.UTF_8)) {
                bufferedReader.lines().forEach((String string) -> {
                    try {
                        Iterator<String> iterator = OPTION_SPLITTER.split(string).iterator();
                        options.putIfAbsent(iterator.next(), iterator.next());
                    } catch (Exception exception) {
                        ConfiguredDefaults.LOGGER.warn("Skipping bad option: {}", string);
                    }
                });

                return true;
            } catch (Throwable throwable) {
                ConfiguredDefaults.LOGGER.error("Failed to load options", throwable);
            }
        }

        return false;
    }

    private static void saveOptions(File file, Map<String, String> options) {
        if (!options.isEmpty()) {
            try (PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file),
                    StandardCharsets.UTF_8))) {
                for (Map.Entry<String, String> entry : options.entrySet()) {
                    printWriter.print(entry.getKey());
                    printWriter.print(':');
                    printWriter.println(entry.getValue());
                }
            } catch (Throwable throwable) {
                ConfiguredDefaults.LOGGER.error("Failed to save options", throwable);
            }
        }
    }
}
