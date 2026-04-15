package io.github.EJR1135.portalbetweenworlds.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import java.util.Random;
import net.minecraft.block.Block;

public class otherWorldPortalFrame extends TemplateBlock {

    public otherWorldPortalFrame(Identifier identifier) {
        super(identifier, Material.STONE);
    }

    public boolean isOpaque() {
        return true;
    }

    public void onPlaced(World world, int i, int j, int k, int l) {
        world.setBlock(i, j, k, this.id);
        world.setBlockMeta(i, j, k, 1);
    }

    @Override
    public void onBreak(World world, int x, int y, int z) {
        if (!world.isRemote) {
            world.spawnEntity(new net.minecraft.entity.ItemEntity(
                world,
                x + 0.5,
                y + 0.5,
                z + 0.5,
                new net.minecraft.item.ItemStack(this)
            ));
        }
    }
}