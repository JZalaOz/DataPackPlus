package dev.jzalaoz.datapackplus.variable.types;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jzalaoz.datapackplus.variable.VariableHandler;
import dev.jzalaoz.datapackplus.variable.VariableTypeModifier;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.command.ServerCommandSource;

public class FallDistanceVariable extends VariableHandler {
    @Override
    public void evaluate(NbtList input, NbtList output, ServerCommandSource source, VariableTypeModifier modifier) throws CommandSyntaxException {
        Entity entity = source.getEntityOrThrow();
        output.add(NbtDouble.of(entity.fallDistance));
    }
}
