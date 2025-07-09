package dev.jzalaoz.datapackplus.math.operations;

import dev.jzalaoz.datapackplus.math.MathOperationException;
import dev.jzalaoz.datapackplus.math.MathOperationHandler;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.command.ServerCommandSource;

public class FactorialOperation extends MathOperationHandler {

    private static final int MAX_FACTORIAL_INPUT;

    static {
        int i = 0;
        int n = 1;

        while (n > 0 && n < Integer.MAX_VALUE) {
            i++;
            n *= i;
        }

        MAX_FACTORIAL_INPUT = i;
    }

    @Override
    public void evaluate(NbtList input, NbtList output, ServerCommandSource source) {
        int num = Math.toIntExact(Math.round(parseNumberAt(input, 0, "x")));

        if (num > MAX_FACTORIAL_INPUT)
            throw new MathOperationException("Cannot calculate factorial of numbers bigger than " + MAX_FACTORIAL_INPUT + " (Integer limit)");

        int result = 1;
        for (int i = 1; i < num; i++) {
            result *= i;
        }

        output.add(NbtInt.of(result));
    }
}
