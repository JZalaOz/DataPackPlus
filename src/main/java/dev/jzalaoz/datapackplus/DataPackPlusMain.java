package dev.jzalaoz.datapackplus;

import dev.jzalaoz.datapackplus.math.MathOperationArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataPackPlusMain implements ModInitializer {

    public static Logger LOGGER = LoggerFactory.getLogger("DataPackPlus");

    @Override
    public void onInitialize() {
        ArgumentTypeRegistry.registerArgumentType(
                DataPackPlusMain.getIdentifier("math_operation"),
                MathOperationArgumentType.class,
                ConstantArgumentSerializer.of(MathOperationArgumentType::new)
        );
    }

    public static Identifier getIdentifier(String path) {
        return Identifier.of("datapackplus", path);
    }

}
