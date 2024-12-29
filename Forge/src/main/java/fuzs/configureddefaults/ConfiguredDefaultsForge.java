package fuzs.configureddefaults;

import fuzs.configureddefaults.handler.CopyDefaultsHandler;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.forgespi.language.ILifecycleEvent;
import net.minecraftforge.forgespi.language.IModLanguageProvider;
import net.minecraftforge.forgespi.language.ModFileScanData;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ConfiguredDefaultsForge implements IModLanguageProvider {

    public ConfiguredDefaultsForge() {
        CopyDefaultsHandler.initialize(FMLPaths.GAMEDIR.get(), FMLEnvironment.dist.isClient());
    }

    @Override
    public String name() {
        return ConfiguredDefaults.MOD_NAME;
    }

    @Override
    public Consumer<ModFileScanData> getFileVisitor() {
        return Function.identity()::apply;
    }

    @Override
    public <R extends ILifecycleEvent<R>> void consumeLifecycleEvent(Supplier<R> consumeEvent) {
        // NO-OP
    }
}
