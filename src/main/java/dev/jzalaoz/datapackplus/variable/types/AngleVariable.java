package dev.jzalaoz.datapackplus.variable.types;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jzalaoz.datapackplus.variable.VariableHandler;
import dev.jzalaoz.datapackplus.variable.VariableTypeModifier;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.command.ServerCommandSource;

public class AngleVariable extends VariableHandler {
    @Override
    public void evaluate(NbtList input, NbtList output, ServerCommandSource source, VariableTypeModifier modifier) throws CommandSyntaxException {
        Entity entity = source.getEntityOrThrow();

        if (modifier == VariableTypeModifier.GET) {
            output.add(NbtDouble.of(entity.getYaw()));
            output.add(NbtDouble.of(entity.getPitch()));
            return;
        }

        double yaw = parseNumberAt(input, 0, "yaw");
        double pitch = parseNumberAt(input, 1, "pitch");

        if (modifier == VariableTypeModifier.ADD) {
            yaw = entity.getYaw() + yaw;
            pitch = entity.getPitch() + pitch;
        }

        entity.rotate((float) yaw, (float) pitch);
    }
}
