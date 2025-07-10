package dev.jzalaoz.datapackplus.variable;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jzalaoz.datapackplus.math.MathOperationException;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.Nullable;

public abstract class VariableHandler {

    protected VariableType type;

    public void setType(VariableType type) {
        assert this.type == null;
        this.type = type;
    }

    public VariableType getType() {
        return this.type;
    }

    public abstract void evaluate(NbtList input, NbtList output, ServerCommandSource source, VariableTypeModifier modifier) throws CommandSyntaxException;

    public void throwIfNotSize(NbtList input, int size, String message) {
        if (input.size() != size) {
            throw new VariableException(message);
        }
    }

    public NbtList getList(NbtList input, int index, @Nullable String variableName) {
        return input.getList(index).orElseThrow(() -> {
            if (variableName == null) {
                return new VariableException("Cannot find input variable TAG_LIST at index " + index);
            } else {
                return new VariableException("Cannot find input variable TAG_LIST named '" + variableName + "' at index" + index);
            }
        });
    }

    public double parseNumberAt(NbtList input, int index, @Nullable String variableName) {
        try {
            NbtElement element = input.get(index);
            return parseNumber(element, variableName);
        } catch (IndexOutOfBoundsException exception) {
            if (variableName == null) {
                throw new VariableException("Cannot find input variable at index " + index);
            } else {
                throw new VariableException("Cannot find input variable named '" + variableName + "' at index" + index);
            }
        }
    }

    public double parseNumber(NbtElement element, @Nullable String variableName) {
        double num;

        if (element.getType() == NbtElement.BYTE_TYPE) {
            num = element.asByte().orElseThrow();
        } else if (element.getType() == NbtElement.SHORT_TYPE) {
            num = element.asShort().orElseThrow();
        } else if (element.getType() == NbtElement.INT_TYPE) {
            num = element.asInt().orElseThrow();
        } else if (element.getType() == NbtElement.FLOAT_TYPE) {
            num = element.asFloat().orElseThrow();
        } else if (element.getType() == NbtElement.DOUBLE_TYPE) {
            num = element.asDouble().orElseThrow();
        } else {
            throw new VariableException("Cannot parse " + element.getNbtType().getCommandFeedbackName());
        }

        return num;
    }

}
