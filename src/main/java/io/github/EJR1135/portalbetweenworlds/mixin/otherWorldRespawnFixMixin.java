package io.github.EJR1135.portalbetweenworlds.mixin;

import io.github.EJR1135.portalbetweenworlds.gen.dim.otherWorldDimension;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.packet.play.PlayerRespawnPacket;
import net.minecraft.server.ChunkMap;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerManager.class)
public class otherWorldRespawnFixMixin {

    @Inject(method = "respawnPlayer", at = @At("HEAD"), cancellable = true)
    private void portalbetweenworlds$fixRespawn(
            ServerPlayerEntity player,
            int dimensionId,
            CallbackInfoReturnable<ServerPlayerEntity> cir
    ) {
        PlayerManager self = (PlayerManager)(Object)this;
        net.minecraft.server.MinecraftServer server = ((PlayerManagerAccessor) self).getServer();

        Vec3i spawnPos = player.getSpawnPos();

        System.out.println("[PBW] respawnPlayer called");
        System.out.println("[PBW] player.dimensionId: " + player.dimensionId);
        System.out.println("[PBW] player.world.dimension type: " + player.world.dimension.getClass().getSimpleName());
        System.out.println("[PBW] dimensionId param: " + dimensionId);
        System.out.println("[PBW] spawnPos: " + spawnPos);
        System.out.println("[PBW] is otherWorldDimension: " + (player.world.dimension instanceof otherWorldDimension));

        // Case 1: Died in custom dimension
        if (player.world.dimension instanceof otherWorldDimension) {
            System.out.println("[PBW] Case 1: died in custom dimension");

            ServerWorld customWorld = server.getWorld(player.dimensionId);
            ServerWorld overworldWorld = server.getWorld(0);
            ServerWorld respawnWorld = customWorld;

            if (spawnPos != null) {
                int bedInOverworld = overworldWorld.getBlockId(spawnPos.x, spawnPos.y, spawnPos.z);
                int bedInCustom = customWorld.getBlockId(spawnPos.x, spawnPos.y, spawnPos.z);

                System.out.println("[PBW] bed check — overworld block: " + bedInOverworld + ", custom block: " + bedInCustom + " (bed id: " + net.minecraft.block.Block.BED.id + ")");

                if (bedInOverworld == net.minecraft.block.Block.BED.id) {
                    respawnWorld = overworldWorld;
                    System.out.println("[PBW] bed found in overworld, respawning there");
                } else if (bedInCustom == net.minecraft.block.Block.BED.id) {
                    respawnWorld = customWorld;
                    System.out.println("[PBW] bed found in custom dim, respawning there");
                } else {
                    System.out.println("[PBW] bed not found in either world, using custom dim world spawn");
                    spawnPos = null;
                }
            }

            cir.setReturnValue(portalbetweenworlds$doRespawn(self, server, player, respawnWorld, spawnPos));
            return;
        }

        // Case 2: Died in overworld, check if bed is in custom dimension
        if (spawnPos != null && player.dimensionId == 0) {
            ServerWorld overworldWorld = server.getWorld(0);
            ServerWorld customWorld = server.getWorld(2);

            int bedInOverworld = overworldWorld.getBlockId(spawnPos.x, spawnPos.y, spawnPos.z);
            int bedInCustom = customWorld.getBlockId(spawnPos.x, spawnPos.y, spawnPos.z);

            System.out.println("[PBW] Case 2 bed check — overworld: " + bedInOverworld + ", custom: " + bedInCustom);

            if (bedInOverworld != net.minecraft.block.Block.BED.id
                    && bedInCustom == net.minecraft.block.Block.BED.id) {
                System.out.println("[PBW] Case 2: bed in custom dim, respawning there");
                cir.setReturnValue(portalbetweenworlds$doRespawn(self, server, player, customWorld, spawnPos));
                return;
            }
        }

        // Case 3: vanilla handles everything else
        System.out.println("[PBW] Case 3: vanilla handling");
    }

    private ServerPlayerEntity portalbetweenworlds$doRespawn(
        PlayerManager self,
        net.minecraft.server.MinecraftServer server,
        ServerPlayerEntity player,
        ServerWorld targetWorld,
        Vec3i spawnPos
    ) {
        System.out.println("[PBW] doRespawn — targetWorld dim: " + targetWorld.dimension.getClass().getSimpleName());
        System.out.println("[PBW] doRespawn — spawnPos: " + spawnPos);

        ServerWorld currentWorld = server.getWorld(player.dimensionId);
        server.getEntityTracker(player.dimensionId).removeListener(player);
        server.getEntityTracker(player.dimensionId).onEntityRemoved(player);
        self.players.remove(player);
        currentWorld.serverRemove(player);

        ServerPlayerEntity newPlayer = new ServerPlayerEntity(
            server,
            targetWorld,
            player.name,
            new ServerPlayerInteractionManager(targetWorld)
        );
        newPlayer.id = player.id;
        newPlayer.networkHandler = player.networkHandler;
        newPlayer.standingEyeHeight = 1.62F;
        newPlayer.dimensionId = targetWorld.dimension.id;
        System.out.println("[PBW] newPlayer created, initial pos: " + newPlayer.x + " " + newPlayer.y + " " + newPlayer.z);

        if (spawnPos != null) {
            newPlayer.setSpawnPos(spawnPos);

            int bedBlockId = targetWorld.getBlockId(spawnPos.x, spawnPos.y, spawnPos.z);
            System.out.println("[PBW] block at spawnPos in targetWorld: " + bedBlockId + " (bed id is " + net.minecraft.block.Block.BED.id + ")");

            Vec3i wakePos = PlayerEntity.findRespawnPosition(targetWorld, spawnPos);
            if (wakePos != null) {
                float spawnX = wakePos.x + 0.5F;
                float spawnZ = wakePos.z + 0.5F;

                if (!targetWorld.isAir(wakePos.x - 1, wakePos.y, wakePos.z)) spawnX += 0.3F;
                if (!targetWorld.isAir(wakePos.x + 1, wakePos.y, wakePos.z)) spawnX -= 0.3F;
                if (!targetWorld.isAir(wakePos.x, wakePos.y, wakePos.z - 1)) spawnZ += 0.3F;
                if (!targetWorld.isAir(wakePos.x, wakePos.y, wakePos.z + 1)) spawnZ -= 0.3F;

                System.out.println("[PBW] spawning at wake pos: " + wakePos.x + " " + wakePos.y + " " + wakePos.z);
                newPlayer.setPositionAndAnglesKeepPrevAngles(
                    spawnX, wakePos.y + 0.1F, spawnZ, 0.0F, 0.0F
                );
            } else {
                System.out.println("[PBW] bed blocked, spawning on top of bed");
                newPlayer.setPositionAndAnglesKeepPrevAngles(
                    spawnPos.x + 0.5F, spawnPos.y + 1.0F, spawnPos.z + 0.5F, 0.0F, 0.0F
                );
            }
        } else {
            Vec3i worldSpawn = targetWorld.getSpawnPos();
            System.out.println("[PBW] no spawnPos, using world spawn: " + worldSpawn.x + " " + worldSpawn.y + " " + worldSpawn.z);
            newPlayer.setPositionAndAnglesKeepPrevAngles(
                worldSpawn.x + 0.5F, worldSpawn.y + 1.0F, worldSpawn.z + 0.5F, 0.0F, 0.0F
            );
        }

        System.out.println("[PBW] intended position: " + newPlayer.x + " " + newPlayer.y + " " + newPlayer.z);

        newPlayer.networkHandler.sendPacket(new PlayerRespawnPacket((byte)newPlayer.dimensionId));

        // Pick the correct ChunkMap directly — do NOT call updatePlayerAfterDimensionChange
        // as it causes double-add when respawning in custom dimension
        ChunkMap[] maps = ((PlayerManagerAccessor) self).getChunkMaps();
        ChunkMap chunkMap;
        if (newPlayer.dimensionId == -1) {
            chunkMap = maps[1];
        } else if (maps.length > 2) {
            chunkMap = maps[2];
        } else {
            chunkMap = maps[0];
        }

        // Remove from all maps first to prevent double-add
        for (ChunkMap map : maps) {
            map.removePlayer(newPlayer);
        }

        chunkMap.addPlayer(newPlayer);
        targetWorld.spawnEntity(newPlayer);
        self.players.add(newPlayer);
        newPlayer.initScreenHandler();
        newPlayer.method_318();

        newPlayer.networkHandler.teleport(newPlayer.x, newPlayer.y, newPlayer.z, newPlayer.yaw, newPlayer.pitch);
        self.sendWorldInfo(newPlayer, targetWorld);
        self.sendPlayerStatus(newPlayer);
        System.out.println("[PBW] doRespawn complete, final pos: " + newPlayer.x + " " + newPlayer.y + " " + newPlayer.z);

        return newPlayer;
    }
}