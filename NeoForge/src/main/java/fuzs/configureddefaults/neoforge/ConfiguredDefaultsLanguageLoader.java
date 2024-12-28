package fuzs.configureddefaults.neoforge;

import fuzs.configureddefaults.ConfiguredDefaults;
import fuzs.configureddefaults.handler.CopyDefaultsHandler;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingException;
import net.neoforged.fml.loading.BuiltInLanguageLoader;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforgespi.language.IModInfo;
import net.neoforged.neoforgespi.language.ModFileScanData;

public class ConfiguredDefaultsLanguageLoader extends BuiltInLanguageLoader {

    public ConfiguredDefaultsLanguageLoader() {
        // with Architectury Loom NeoForge is unable to access the common module, so copy everything here
        CopyDefaultsHandler.initialize(FMLPaths.GAMEDIR.get(), true);
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
