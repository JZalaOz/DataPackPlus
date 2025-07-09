package dev.jzalaoz.datapackplus.math.operations;

import dev.jzalaoz.datapackplus.math.MathOperationHandler;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.command.ServerCommandSource;

public class ModuloOperation extends MathOperationHandler {
    @Override
    public void evaluate(NbtList input, NbtList output, ServerCommandSource source) {
        double left = parseNumberAt(input, 0, "left");
        double right = parseNumberAt(input, 1, "right");

        output.add(NbtDouble.of(left % right));
    }
}
