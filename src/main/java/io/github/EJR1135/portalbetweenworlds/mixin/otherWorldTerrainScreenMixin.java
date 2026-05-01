package io.github.EJR1135.portalbetweenworlds.mixin;

import io.github.EJR1135.portalbetweenworlds.gen.dim.otherWorldDimension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.network.ClientNetworkHandler;
import net.minecraft.network.packet.play.PlayerRespawnPacket;
import net.minecraft.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientNetworkHandler.class)
public class otherWorldTerrainScreenMixin {

    @Shadow private Minecraft minecraft;
    @Shadow private ClientWorld world;
    @Shadow private boolean started;

    @Inject(method = "onPlayerRespawn", at = @At("HEAD"))
    private void portalbetweenworlds$forceTerrainScreen(PlayerRespawnPacket packet, CallbackInfo ci) {
        if (this.minecraft == null || this.minecraft.player == null) return;
        if (this.minecraft.world == null) return;

        boolean inCustomDim = this.minecraft.world.dimension instanceof otherWorldDimension;
        boolean respawningIntoCustomDim = packet.dimensionRawId == 2;
        boolean sameDim = packet.dimensionRawId == this.minecraft.player.dimensionId;

        if ((inCustomDim || respawningIntoCustomDim) && sameDim) {
            System.out.println("[PBW-CLIENT] forcing DownloadingTerrainScreen");
            ClientNetworkHandler handler = (ClientNetworkHandler)(Object)this;
            this.minecraft.setScreen(new DownloadingTerrainScreen(handler));
            ((ClientNetworkHandlerAccessor) handler).setStarted(false);
        }
    }
}