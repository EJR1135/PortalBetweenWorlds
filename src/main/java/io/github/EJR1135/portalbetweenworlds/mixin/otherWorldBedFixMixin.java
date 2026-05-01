package io.github.EJR1135.portalbetweenworlds.mixin;

import io.github.EJR1135.portalbetweenworlds.gen.dim.otherWorldDimension;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class otherWorldBedFixMixin {

    @Inject(method = "wakeUp", at = @At("HEAD"))
    private void portalbetweenworlds$wakeUpFix(
            boolean resetSleepTimer,
            boolean updateSleepingPlayers,
            boolean setSpawnPos,
            CallbackInfo ci
    ) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        World world = player.world;

        if (!(world.dimension instanceof otherWorldDimension)) return;

        // Redirect spawn-setting to THIS dimension's world properties
        // instead of letting vanilla write it elsewhere.
        if (setSpawnPos) {
            Vec3i pos = player.getSpawnPos();
            if (pos != null) {
                world.getProperties().setSpawn(pos.x, pos.y, pos.z);
            }
        }
        // Do NOT cancel — let vanilla wakeUp continue normally.
    }
}