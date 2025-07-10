package dev.jzalaoz.datapackplus.variable.types;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jzalaoz.datapackplus.variable.VariableHandler;
import dev.jzalaoz.datapackplus.variable.VariableTypeModifier;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.command.ServerCommandSource;

public class PositionVariable extends VariableHandler {
    @Override
    public void evaluate(NbtList input, NbtList output, ServerCommandSource source, VariableTypeModifier modifier) throws CommandSyntaxException {
        Entity entity = source.getEntityOrThrow();

        if (modifier == VariableTypeModifier.GET) {
            output.add(NbtDouble.of(entity.getX()));
            output.add(NbtDouble.of(entity.getY()));
            output.add(NbtDouble.of(entity.getZ()));
            return;
        }

        double x = parseNumberAt(input, 0, "x");
        double y = parseNumberAt(input, 1, "y");
        double z = parseNumberAt(input, 2, "z");

        if (modifier == VariableTypeModifier.ADD) {
            x = entity.getX() + x;
            y = entity.getY() + y;
            z = entity.getZ() + z;
        }

        entity.requestTeleport(x, y, z);
    }
}
