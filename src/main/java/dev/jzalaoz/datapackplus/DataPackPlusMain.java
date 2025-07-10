package dev.jzalaoz.datapackplus;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.api.ModInitializer;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataPackPlusMain implements ModInitializer {

    public static Logger LOGGER = LoggerFactory.getLogger("DataPackPlus");
    public static final SuggestionProvider<ServerCommandSource> STORAGE_SUGGESTION_PROVIDER = (context, builder) ->
            CommandSource.suggestIdentifiers(context.getSource().getServer().getDataCommandStorage().getIds(), builder);

    @Override
    public void onInitialize() {

    }

    public static Identifier getIdentifier(String path) {
        return Identifier.of("datapackplus", path);
    }

}
