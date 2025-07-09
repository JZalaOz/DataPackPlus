package dev.jzalaoz.datapackplus.math.operations;

import dev.jzalaoz.datapackplus.math.MathOperationHandler;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.command.ServerCommandSource;

public class RoundingOperation extends MathOperationHandler {
    @Override
    public void evaluate(NbtList input, NbtList output, ServerCommandSource source) {
        double num = parseNumberAt(input, 0, null);

        switch (this.operation) {
            case CEIL -> num = Math.ceil(num);
            case ROUND -> num = Math.round(num);
            case FLOOR -> num = Math.floor(num);
            default -> throw new AssertionError();
        }

        output.add(NbtDouble.of(num));
    }
}
