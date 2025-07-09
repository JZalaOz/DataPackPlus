package dev.jzalaoz.datapackplus.math.operations;

import dev.jzalaoz.datapackplus.math.MathOperation;
import dev.jzalaoz.datapackplus.math.MathOperationHandler;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.command.ServerCommandSource;

public class PowerOperation extends MathOperationHandler {
    @Override
    public void evaluate(NbtList input, NbtList output, ServerCommandSource source) {
        double a = parseNumberAt(input, 0, "a");
        double b = parseNumberAt(input, 1, "b");

        double num;

        if (this.operation == MathOperation.POWER) {
            num = Math.pow(a, b);
        } else if (this.operation == MathOperation.ROOT) {
            num = Math.pow(a, 1.0 / b);
        } else {
            throw new AssertionError();
        }

        output.add(NbtDouble.of(num));
    }
}
