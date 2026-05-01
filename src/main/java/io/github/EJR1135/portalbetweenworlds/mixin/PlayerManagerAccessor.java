package io.github.EJR1135.portalbetweenworlds.mixin;

import net.minecraft.server.ChunkMap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerManager.class)
public interface PlayerManagerAccessor {
    @Accessor("server")
    MinecraftServer getServer();

    @Accessor("chunkMaps")
    ChunkMap[] getChunkMaps();

    @Accessor("chunkMaps")
    void setChunkMaps(ChunkMap[] chunkMaps);
}