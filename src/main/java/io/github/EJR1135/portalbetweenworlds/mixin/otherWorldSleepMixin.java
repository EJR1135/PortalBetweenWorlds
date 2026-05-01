package io.github.EJR1135.portalbetweenworlds.mixin;

import io.github.EJR1135.portalbetweenworlds.gen.dim.otherWorldDimension;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public class otherWorldSleepMixin {

    @Shadow
    private boolean allPlayersSleeping;

    /**
     * canSkipNight() checks allPlayersSleeping, but that flag is only set
     * by updateSleepingPlayers(), which may never be called on our dimension's
     * world instance by the server. We override the check to compute it
     * directly from our own players list instead.
     */
    @Inject(method = "canSkipNight", at = @At("HEAD"), cancellable = true)
    private void portalbetweenworlds$fixCanSkipNight(CallbackInfoReturnable<Boolean> cir) {
        World self = (World)(Object)this;

        if (!(self.dimension instanceof otherWorldDimension)) return;
        if (self.isRemote) {
            cir.setReturnValue(false);
            return;
        }

        // Replicate canSkipNight() logic but using THIS world's players list
        if (self.players.isEmpty()) {
            cir.setReturnValue(false);
            return;
        }

        for (Object obj : self.players) {
            PlayerEntity player = (PlayerEntity) obj;
            if (!player.isFullyAsleep()) {
                cir.setReturnValue(false);
                return;
            }
        }

        cir.setReturnValue(true);
    }
}