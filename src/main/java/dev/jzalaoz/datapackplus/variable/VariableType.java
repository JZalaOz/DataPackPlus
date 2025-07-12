package dev.jzalaoz.datapackplus.variable;

import com.mojang.brigadier.Message;
import dev.jzalaoz.datapackplus.variable.types.*;
import net.minecraft.text.Text;
import oshi.util.tuples.Triplet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public enum VariableType {
    MOTION("motion", new MotionVariable()),
    HUNGER("hunger", new HungerVariable()),
    HEALTH("health", new HealthVariable()),
    HELD_SLOT("held_slot", new HeldSlotVariable()),
    FALL_DISTANCE("fall_distance", new FallDistanceVariable(), false, false),
    DIMENSION("dimension", new DimensionVariable(), false, false),
    POSITION("position", new PositionVariable()),
    ANGLE("angle", new AngleVariable());

    public final String key;
    public final VariableHandler handler;
    public final List<VariableTypeModifier> modifiers;

    VariableType(String key, VariableHandler handler, boolean set, boolean add) {
        this.key = key;
        this.handler = handler;
        this.handler.setType(this);

        List<VariableTypeModifier> modifiers = new ArrayList<>();
        modifiers.add(VariableTypeModifier.GET);
        if (set) modifiers.add(VariableTypeModifier.SET);
        if (add) modifiers.add(VariableTypeModifier.ADD);

        this.modifiers = modifiers.stream().toList();
    }

    VariableType(String key, VariableHandler handler) {
        this(key, handler, true, true);
    }

    public static Collection<Triplet<String, Message, VariableType>> getSuggestions() {
        Collection<Triplet<String, Message, VariableType>> list = new ArrayList<>();

        for (VariableType value : values()) {
            String title = value.name().replace('_', ' ');
            title = title.substring(0, 1).toUpperCase() + title.substring(1).toLowerCase();
            list.add(new Triplet<>(value.key, Text.literal(title), value));
        }

        return list;
    }

    public static VariableType getFromKey(String key) {
        return Arrays.stream(VariableType.values())
                .filter((it) -> it.key.equalsIgnoreCase(key))
                .findFirst()
                .orElseThrow(() -> new VariableException("Invalid variable string '" + key + "'"));
    }

}
