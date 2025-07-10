package dev.jzalaoz.datapackplus.variable.types;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jzalaoz.datapackplus.variable.VariableHandler;
import dev.jzalaoz.datapackplus.variable.VariableTypeModifier;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class MotionVariable extends VariableHandler {
    @Override
    public void evaluate(NbtList input, NbtList output, ServerCommandSource source, VariableTypeModifier modifier) throws CommandSyntaxException {
        Entity entity = source.getEntityOrThrow();

        if (modifier == VariableTypeModifier.GET) {
            output.add(NbtDouble.of(entity.getVelocity().x));
            output.add(NbtDouble.of(entity.getVelocity().y));
            output.add(NbtDouble.of(entity.getVelocity().z));
            return;
        }

        double x = parseNumberAt(input, 0, "x");
        double y = parseNumberAt(input, 1, "y");
        double z = parseNumberAt(input, 2, "z");

        if (modifier == VariableTypeModifier.ADD) {
            x = entity.getVelocity().x + x;
            y = entity.getVelocity().y + y;
            z = entity.getVelocity().z + z;
        }

        entity.setVelocity(new Vec3d(x, y, z));
        if (entity instanceof ServerPlayerEntity player) {
            player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));
        }
    }
}
