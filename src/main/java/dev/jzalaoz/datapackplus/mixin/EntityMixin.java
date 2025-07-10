package dev.jzalaoz.datapackplus.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @ModifyExpressionValue(
            method = "Lnet/minecraft/entity/Entity;startRiding(Lnet/minecraft/entity/Entity;Z)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityType;isSaveable()Z")
    )
    private static boolean allowPlayerRiding(boolean original, @Local(argsOnly = true) Entity entity) {
        if (entity.getType() == EntityType.PLAYER) {
            return true;
        }

        return original;
    }

}
