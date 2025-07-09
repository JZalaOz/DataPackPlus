package dev.jzalaoz.datapackplus.math.operations;

import dev.jzalaoz.datapackplus.math.MathOperation;
import dev.jzalaoz.datapackplus.math.MathOperationHandler;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.MathHelper;

public class DistanceOperation extends MathOperationHandler {
    @Override
    public void evaluate(NbtList input, NbtList output, ServerCommandSource source) {
        NbtList pos1 = getList(input, 0, "pos1");
        NbtList pos2 = getList(input, 1, "pos1");

        throwIfNotSize(pos1, 3, "Expected pos1 with size 3");
        throwIfNotSize(pos2, 3, "Expected pos2 with size 3");

        double x = MathHelper.square(parseNumberAt(pos2, 0, "x2") - parseNumberAt(pos1, 0, "x1"));
        double y = MathHelper.square(parseNumberAt(pos2, 1, "y2") - parseNumberAt(pos1, 1, "y1"));
        double z = MathHelper.square(parseNumberAt(pos2, 2, "z2") - parseNumberAt(pos1, 2, "z1"));

        if (this.operation == MathOperation.DISTANCE_SQUARED) {
            output.add(NbtDouble.of(x + y + z));
        } else if (this.operation == MathOperation.DISTANCE) {
            output.add(NbtDouble.of(Math.sqrt(x + y + z)));
        } else {
            throw new AssertionError();
        }
    }
}