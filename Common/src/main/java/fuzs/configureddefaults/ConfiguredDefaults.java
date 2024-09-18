package fuzs.configureddefaults;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfiguredDefaults {
    public static final String MOD_ID = "configureddefaults";
    public static final String MOD_NAME = "Configured Defaults";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
