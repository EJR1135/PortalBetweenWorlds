package io.github.EJR1135.portalbetweenworlds.gen.dim;

import io.github.EJR1135.portalbetweenworlds.gen.dim.chunkProviderOtherWorld;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.minecraft.block.Block;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.chunk.OverworldChunkGenerator;
import net.minecraft.world.dimension.Dimension;
import net.modificationstation.stationapi.api.client.world.dimension.TravelMessageProvider;
import net.modificationstation.stationapi.api.util.Identifier;
import java.util.Random;

import static io.github.EJR1135.portalbetweenworlds.gen.dim.otherWorldDimensions.MOD_ID;

@EnvironmentInterface(value = EnvType.CLIENT, itf = TravelMessageProvider.class)
public class otherWorldDimension extends Dimension implements TravelMessageProvider {
    public static final String
        ENTERING_MESSAGE = "gui." + Identifier.of(MOD_ID, "ascending"),
        LEAVING_MESSAGE = "gui." + Identifier.of(MOD_ID, "descending");

    private final float[] colours = new float[4];
    public otherWorldDimension(int serialId) {
        id = serialId;
    }

    /*
    @Override
    protected void initBiomeSource() {
        biomeSource = new otherWorldBiomeSource(1);
    }
    */

    @Override
    public void OverworldChunkGenerator(World world, long seed) {
        long seed = new Random().nextLong();
        this.world = world;
    }
    /*
    @Override
    public float getTimeOfDay(long time, float delta) {
//        boolean hasKilledGold = ModLoader.getMinecraftInstance().statFileWriter.hasAchievementUnlocked(otherWorldAchievements.defeatGold);
//        if(hasKilledGold)
//        {
//            int timeTicks = (int)(l % 0x13880L);
//            float timeFraction = ((float)timeTicks + f) / 120000F - 0.25F;
//            if(timeTicks > 60000)
//            {
//                timeTicks -= 40000;
//                timeFraction = ((float)timeTicks + f) / 20000F - 0.25F;
//            }
//            if(timeFraction < 0.0F)
//            {
//                timeFraction++;
//            }
//            if(timeFraction > 1.0F)
//            {
//                timeFraction--;
//            }
//            float f2 = timeFraction;
//            timeFraction = 1.0F - (float)((Math.cos((double)timeFraction * 3.1415926535897931D) + 1.0D) / 2D);
//            timeFraction = f2 + (timeFraction - f2) / 3F;
//            return timeFraction;
//        } else
//        {
        return 0.0F;
//        }
    }


    @Override
    public boolean hasGround() {
        return false;
    }

    @Override
    public float getCloudHeight() {
        return 8;
    }
*/

    @Override
    public boolean isValidSpawnPoint(int x, int y) {
        int var3 = this.world.getSpawnBlockId(x, y);
        return var3 != 0 && Block.BLOCKS[var3].material.blocksMovement();
    }

    @Override
    public boolean hasWorldSpawn() {
        return false;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public String getEnteringTranslationKey() {
        return ENTERING_MESSAGE;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public String getLeavingTranslationKey() {
        return LEAVING_MESSAGE;
    }
}