package io.github.EJR1135.portalbetweenworlds.mixin;

import io.github.EJR1135.portalbetweenworlds.gen.dim.otherWorldDimension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class otherWorldClientRespawnMixin {

    @Shadow public World world;
    @Shadow public ClientPlayerEntity player;

    @Inject(method = "respawnPlayer", at = @At("HEAD"), cancellable = true)
    private void portalbetweenworlds$fixClientRespawn(boolean worldSpawn, int dimension, CallbackInfo ci) {
        Minecraft mc = (Minecraft)(Object)this;

        // This mixin is singleplayer only — multiplayer respawn is handled server-side
        if (this.world != null && this.world.isRemote) {
            System.out.println("[PBW-CLIENT] multiplayer detected, skipping client mixin");
            return;
        }

        if (this.player == null) return;

        Vec3i spawnPos = this.player.getSpawnPos();

        System.out.println("[PBW-CLIENT] respawnPlayer called");
        System.out.println("[PBW-CLIENT] worldSpawn: " + worldSpawn);
        System.out.println("[PBW-CLIENT] dimension: " + dimension);
        System.out.println("[PBW-CLIENT] world dim type: " + this.world.dimension.getClass().getSimpleName());
        System.out.println("[PBW-CLIENT] spawnPos: " + spawnPos);
        System.out.println("[PBW-CLIENT] is custom dim: " + (this.world.dimension instanceof otherWorldDimension));

        // Case 1: Died in custom dimension — spawn at bed or custom dim world spawn
        if (this.world.dimension instanceof otherWorldDimension) {
            System.out.println("[PBW-CLIENT] Case 1: died in custom dimension");

            Vec3i respawnPos;
            boolean hasBed = false;

            if (spawnPos != null) {
                hasBed = true;
                System.out.println("[PBW-CLIENT] spawning at bed: " + spawnPos.x + " " + spawnPos.y + " " + spawnPos.z);
            } else {
                respawnPos = this.world.getSpawnPos();
                System.out.println("[PBW-CLIENT] no bed, using world spawn: " + respawnPos.x + " " + respawnPos.y + " " + respawnPos.z);
            }

            this.world.updateSpawnPosition();
            this.world.updateEntityLists();
            int oldId = 0;
            if (this.player != null) {
                oldId = this.player.id;
                this.world.remove(this.player);
            }

            mc.camera = null;
            this.player = (ClientPlayerEntity) mc.interactionManager.createPlayer(this.world);
            this.player.dimensionId = dimension;
            mc.camera = this.player;
            this.player.teleportTop();

            if (hasBed) {
                this.player.setSpawnPos(spawnPos);
                Vec3i wakePos = PlayerEntity.findRespawnPosition(this.world, spawnPos);
                if (wakePos != null) {
                    this.player.setPositionAndAnglesKeepPrevAngles(
                        wakePos.x + 0.5F, wakePos.y + 0.1F, wakePos.z + 0.5F, 0.0F, 0.0F
                    );
                } else {
                    this.player.setPositionAndAnglesKeepPrevAngles(
                        spawnPos.x + 0.5F, spawnPos.y + 1.0F, spawnPos.z + 0.5F, 0.0F, 0.0F
                    );
                }
            } else {
                Vec3i worldSpawnPos = this.world.getSpawnPos();
                this.player.setPositionAndAnglesKeepPrevAngles(
                    worldSpawnPos.x + 0.5F, worldSpawnPos.y + 1.0F, worldSpawnPos.z + 0.5F, 0.0F, 0.0F
                );
            }

            mc.interactionManager.preparePlayer(this.player);
            this.world.addPlayer(this.player);
            this.player.input = new KeyboardInput(mc.options);
            this.player.id = oldId;
            this.player.spawn();
            mc.interactionManager.preparePlayerRespawn(this.player);
            ((MinecraftAccessor) mc).invokePrepareWorld("Respawning");
            if (mc.currentScreen instanceof DeathScreen) {
                mc.setScreen(null);
            }

            ci.cancel();
            return;
        }

        // Case 2: Died in overworld, bed is in custom dimension
        // — clear spawn so vanilla falls back to overworld world spawn
        if (!worldSpawn && spawnPos != null) {
            boolean bedInCurrentWorld = this.world.getBlockId(spawnPos.x, spawnPos.y, spawnPos.z)
                == net.minecraft.block.Block.BED.id;
            System.out.println("[PBW-CLIENT] Case 2 check — bedInCurrentWorld: " + bedInCurrentWorld);
            if (!bedInCurrentWorld) {
                System.out.println("[PBW-CLIENT] bed is in custom dim, clearing spawn for vanilla fallback");
                this.player.setSpawnPos(null);
            }
        }

        // Case 3: vanilla handles everything else
        System.out.println("[PBW-CLIENT] falling through to vanilla");
    }
}