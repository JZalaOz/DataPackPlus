package dev.jzalaoz.datapackplus.math;

import com.mojang.brigadier.Message;
import dev.jzalaoz.datapackplus.math.operations.*;
import net.minecraft.text.Text;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public enum MathOperation {
    ADD("add", new ArithmeticOperation()),
    SUBTRACT("subtract", new ArithmeticOperation()),
    MULTIPLY("multiply", new ArithmeticOperation()),
    DIVIDE("divide", new ArithmeticOperation()),

    MODULO("mod", new ModuloOperation()),
    NEGATE("negate", new NegateOperation()),
    FACTORIAL("factorial", new FactorialOperation()),

    POWER("power", new PowerOperation()),
    ROOT("root", new PowerOperation()),

    DISTANCE("distance", new DistanceOperation()),
    DISTANCE_SQUARED("distance_squared", new DistanceOperation()),

    RAD_TO_DEG("rad2deg", new AngleConvertOperation()),
    DEG_TO_RAD("deg2rad", new AngleConvertOperation()),

    SIN("sin", new TrigOperation()),
    COS("cos", new TrigOperation()),
    TAN("tan", new TrigOperation()),
    ARCSIN("arcsin", new TrigOperation()),
    ARCCOS("arccos", new TrigOperation()),
    ARCTAN("arctan", new TrigOperation()),
    ARCTAN2("arctan2", new TrigOperation()),

    CEIL("ceil", new RoundingOperation()),
    ROUND("round", new RoundingOperation()),
    FLOOR("floor", new RoundingOperation()),

    ;

    public final String key;
    public final MathOperationHandler handler;

    MathOperation(String key, MathOperationHandler handler) {
        this.key = key;
        this.handler = handler;
        this.handler.setOperation(this);
    }

    public static Collection<Pair<String, Message>> getSuggestions() {
        Collection<Pair<String, Message>> list = new ArrayList<>();

        for (MathOperation value : values()) {
            String title = value.name().replace('_', ' ');
            title = title.substring(0, 1).toUpperCase() + title.substring(1).toLowerCase();
            list.add(new Pair<>(value.key, Text.literal(title)));
        }

        return list;
    }

    public static MathOperation getFromKey(String key) {
        return Arrays.stream(MathOperation.values())
                .filter((it) -> it.key.equalsIgnoreCase(key))
                .findFirst()
                .orElseThrow(() -> new MathOperationException("Invalid operation string '" + key + "'"));
    }

}
