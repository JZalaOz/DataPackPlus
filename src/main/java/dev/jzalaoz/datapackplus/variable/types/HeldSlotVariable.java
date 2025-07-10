package dev.jzalaoz.datapackplus.variable.types;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jzalaoz.datapackplus.variable.VariableHandler;
import dev.jzalaoz.datapackplus.variable.VariableTypeModifier;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.s2c.play.UpdateSelectedSlotS2CPacket;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class HeldSlotVariable extends VariableHandler {
    @Override
    public void evaluate(NbtList input, NbtList output, ServerCommandSource source, VariableTypeModifier modifier) throws CommandSyntaxException {
        ServerPlayerEntity entity = source.getPlayerOrThrow();

        if (modifier == VariableTypeModifier.GET) {
            output.add(NbtInt.of(entity.getInventory().getSelectedSlot()));
            return;
        }

        int slot = (int) parseNumberAt(input, 0, "slot");

        if (modifier == VariableTypeModifier.ADD) {
            slot = slot + entity.getInventory().getSelectedSlot();
        }

        entity.getInventory().setSelectedSlot(slot);
        entity.networkHandler.sendPacket(new UpdateSelectedSlotS2CPacket(entity.getInventory().getSelectedSlot()));
    }
}
