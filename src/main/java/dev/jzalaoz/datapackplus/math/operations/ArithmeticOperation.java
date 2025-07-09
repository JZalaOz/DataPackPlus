package dev.jzalaoz.datapackplus.math.operations;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jzalaoz.datapackplus.math.MathOperationHandler;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.command.ServerCommandSource;

public class ArithmeticOperation extends MathOperationHandler {
    @Override
    public void evaluate(NbtList input, NbtList output, ServerCommandSource source) throws CommandSyntaxException {
        double num = 0.0;

        for (int i = 0; i < input.size(); i++) {
            if (i == 0) {
                num = parseNumberAt(input, 0, null);
                continue;
            }

            double next = parseNumberAt(input, i, null);
            switch (operation) {
                case ADD -> num += next;
                case SUBTRACT -> num -= next;
                case MULTIPLY -> num *= next;
                case DIVIDE -> num /= next;
                default -> throw new AssertionError();
            }
        }

        output.add(NbtDouble.of(num));
    }
}
