package fuzs.configureddefaults.neoforge;

import fuzs.configureddefaults.common.ConfiguredDefaults;
import fuzs.configureddefaults.common.handler.CopyDefaultsHandler;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingException;
import net.neoforged.fml.loading.BuiltInLanguageLoader;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforgespi.language.IModInfo;
import net.neoforged.neoforgespi.language.ModFileScanData;

public class ConfiguredDefaultsLanguageLoader extends BuiltInLanguageLoader {

    public ConfiguredDefaultsLanguageLoader() {
        CopyDefaultsHandler.initialize(FMLLoader.getCurrent().getGameDir(), FMLEnvironment.getDist().isClient());
    }

    @Override
    public String name() {
        return ConfiguredDefaults.MOD_NAME;
    }

    @Override
    public ModContainer loadMod(IModInfo info, ModFileScanData modFileScanResults, ModuleLayer layer) throws ModLoadingException {
        return null;
    }
}
