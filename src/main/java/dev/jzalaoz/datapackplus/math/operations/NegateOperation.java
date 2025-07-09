package dev.jzalaoz.datapackplus.math.operations;

import dev.jzalaoz.datapackplus.math.MathOperationHandler;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.command.ServerCommandSource;

public class NegateOperation extends MathOperationHandler {
    @Override
    public void evaluate(NbtList input, NbtList output, ServerCommandSource source) {
        double num = -parseNumberAt(input, 0, null);
        output.add(NbtDouble.of(num));
    }
}
