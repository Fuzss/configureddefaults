package fuzs.configureddefaults;

import fuzs.configureddefaults.handler.CopyDefaultsHandler;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.LanguageAdapter;
import net.fabricmc.loader.api.LanguageAdapterException;
import net.fabricmc.loader.api.ModContainer;

public class ConfiguredDefaultsFabric implements LanguageAdapter {

    public ConfiguredDefaultsFabric() {
        CopyDefaultsHandler.initialize(FabricLoader.getInstance().getGameDir(), true);
    }

    @Override
    public <T> T create(ModContainer mod, String value, Class<T> type) throws LanguageAdapterException {
        throw new LanguageAdapterException(ConfiguredDefaults.MOD_NAME);
    }
}
