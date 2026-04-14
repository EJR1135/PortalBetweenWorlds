package io.github.EJR1135.portalbetweenworlds.gen.dim;


import io.github.EJR1135.portalbetweenworlds.block.otherWorldBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.SandBlock;
import net.minecraft.client.gui.screen.LoadingDisplay;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSource;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;


public class chunkProviderOtherWorld {
    World world;
    long seed;
    public chunkProviderOtherWorld(World world, long seed){
        this.world = world;
        this.seed = seed;
    }
}