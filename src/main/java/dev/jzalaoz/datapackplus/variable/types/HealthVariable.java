package dev.jzalaoz.datapackplus.variable.types;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jzalaoz.datapackplus.variable.VariableException;
import dev.jzalaoz.datapackplus.variable.VariableHandler;
import dev.jzalaoz.datapackplus.variable.VariableTypeModifier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.command.ServerCommandSource;

public class HealthVariable extends VariableHandler {
    @Override
    public void evaluate(NbtList input, NbtList output, ServerCommandSource source, VariableTypeModifier modifier) throws CommandSyntaxException {
        Entity target = source.getEntityOrThrow();

        if (!(target instanceof LivingEntity entity)) {
            throw new VariableException("A Living Entity is required to run this command.");
        }

        if (modifier == VariableTypeModifier.GET) {
            output.add(NbtDouble.of(entity.getHealth()));
            return;
        }

        double health = parseNumberAt(input, 0, "health");

        if (modifier == VariableTypeModifier.ADD) {
            health = entity.getHealth() + health;
        }

        entity.setHealth((float) health);
    }
}
