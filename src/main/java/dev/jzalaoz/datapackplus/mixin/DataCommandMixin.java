package dev.jzalaoz.datapackplus.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.jzalaoz.datapackplus.math.MathCommand;
import dev.jzalaoz.datapackplus.variable.VariableCommand;
import net.minecraft.server.command.DataCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DataCommand.class)
public abstract class DataCommandMixin {

    // Register custom sub-commands to the /data command

    @Inject(
            method = "register",
            at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/CommandDispatcher;register(Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;)Lcom/mojang/brigadier/tree/LiteralCommandNode;")
    )
    private static void registerSubCommands(CommandDispatcher<ServerCommandSource> dispatcher, CallbackInfo ci, @Local LiteralArgumentBuilder<ServerCommandSource> literalArgumentBuilder) {
        MathCommand.register(literalArgumentBuilder);
        VariableCommand.register(literalArgumentBuilder);
    }

}
