package fuzs.configureddefaults.fabric;

import fuzs.configureddefaults.ConfiguredDefaults;
import fuzs.configureddefaults.handler.CopyDefaultsHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.LanguageAdapter;
import net.fabricmc.loader.api.LanguageAdapterException;
import net.fabricmc.loader.api.ModContainer;

public class ConfiguredDefaultsLanguageAdapter implements LanguageAdapter {

    public ConfiguredDefaultsLanguageAdapter() {
        // doesn't work in-dev as the main adapter class is not found, but production is fine, no clue why
        CopyDefaultsHandler.initialize(FabricLoader.getInstance().getGameDir(),
                FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT);
    }

    @Override
    public <T> T create(ModContainer mod, String value, Class<T> type) throws LanguageAdapterException {
        throw new LanguageAdapterException(ConfiguredDefaults.MOD_NAME);
    }
}
