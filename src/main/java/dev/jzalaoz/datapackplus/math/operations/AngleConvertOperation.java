package dev.jzalaoz.datapackplus.math.operations;

import dev.jzalaoz.datapackplus.math.MathOperation;
import dev.jzalaoz.datapackplus.math.MathOperationHandler;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.command.ServerCommandSource;

public class AngleConvertOperation extends MathOperationHandler {
    @Override
    public void evaluate(NbtList input, NbtList output, ServerCommandSource source) {
        double x = parseNumberAt(input, 0, null);
        double num;

        if (this.operation == MathOperation.RAD_TO_DEG) {
            num = Math.toDegrees(x);
        } else if (this.operation == MathOperation.DEG_TO_RAD) {
            num = Math.toRadians(x);
        } else {
            throw new AssertionError();
        }

        output.add(NbtDouble.of(num));
    }
}
