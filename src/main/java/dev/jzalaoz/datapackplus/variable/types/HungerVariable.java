package dev.jzalaoz.datapackplus.variable.types;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jzalaoz.datapackplus.variable.VariableHandler;
import dev.jzalaoz.datapackplus.variable.VariableTypeModifier;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class HungerVariable extends VariableHandler {
    @Override
    public void evaluate(NbtList input, NbtList output, ServerCommandSource source, VariableTypeModifier modifier) throws CommandSyntaxException {
        ServerPlayerEntity entity = source.getPlayerOrThrow();

        if (modifier == VariableTypeModifier.GET) {
            output.add(NbtDouble.of(entity.getHungerManager().getFoodLevel()));
            output.add(NbtDouble.of(entity.getHungerManager().getSaturationLevel()));
            return;
        }

        double food = parseNumberAt(input, 0, "food");
        double saturation = parseNumberAt(input, 1, "saturation");

        if (modifier == VariableTypeModifier.ADD) {
            food = entity.getHungerManager().getFoodLevel() + food;
            saturation = entity.getHungerManager().getSaturationLevel() + saturation;
        }

        entity.getHungerManager().setFoodLevel((int) food);
        entity.getHungerManager().setSaturationLevel((float) saturation);
    }
}
