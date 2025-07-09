package dev.jzalaoz.datapackplus.math.operations;

import dev.jzalaoz.datapackplus.math.MathOperationHandler;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.command.ServerCommandSource;

public class TrigOperation extends MathOperationHandler {
    @Override
    public void evaluate(NbtList input, NbtList output, ServerCommandSource source) {
        float num = (float) parseNumberAt(input, 0, "x");
        double result;

        switch (this.operation) {
            case SIN -> result = Math.sin(num);
            case COS -> result = Math.cos(num);
            case TAN -> result = Math.tan(num);
            case ARCSIN -> result = Math.asin(num);
            case ARCCOS -> result = Math.acos(num);
            case ARCTAN -> result = Math.atan(num);
            case ARCTAN2 -> result = Math.atan2(num, parseNumberAt(input, 1, "y"));
            default -> throw new AssertionError();
        }

        output.add(NbtDouble.of(result));
    }
}
