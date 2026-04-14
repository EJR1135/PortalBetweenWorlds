package io.github.EJR1135.portalbetweenworlds.block;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;

public class otherWorldBlocks {
    @Entrypoint.Namespace
    public static Namespace MOD_ID = Null.get();
    public static Block Portal;
    public static Block portalFrame;
    

    @EventListener
    public void registerBlocks(BlockRegistryEvent event) {
        Portal = (new otherWorldPortal(Identifier.of(MOD_ID, "otherWorld_portal"))).setHardness(-1.0F).setResistance(6000000.0F).setTranslationKey(MOD_ID, "otherWorld_portal");
        portalFrame = (new otherWorldPortal(Identifier.of(MOD_ID, "otherWorld_portalframe"))).setHardness(0.5F).setSoundGroup(Block.STONE_SOUND_GROUP).setTranslationKey(MOD_ID, "otherWorld_portalframe");
        //otherWorldItems.Aercloud = (new ItemBlockAercloud(Aercloud.id));

        /*
        ModLoader.RegisterBlock(Holystone, ItemBlockHolystone.class);
        ModLoader.RegisterBlock(Aercloud, ItemBlockAercloud.class);
        ModLoader.RegisterBlock(Log, ItemBlockotherWorldLog.class);
        ModLoader.RegisterBlock(DungeonStone, ItemDungeonBlock.class);
        ModLoader.RegisterBlock(LightDungeonStone, ItemDungeonBlock.class);
        ModLoader.RegisterBlock(Pillar, ItemDungeonBlock.class);
        ModLoader.RegisterBlock(Quicksoil, ItemBlockQuicksoil.class);
        ModLoader.RegisterTileEntity(TileEntityIncubator.class, "Incubator");
        ModLoader.RegisterTileEntity(TileEntityEnchanter.class, "Enchanter");
        ModLoader.RegisterTileEntity(TileEntityFreezer.class, "Freezer");
        ModLoader.RegisterEntityID(EntityMimic.class, "Mimic", ModLoader.getUniqueEntityId());*/
        /*
        Pickaxe = new ToolBase();
        Shovel = new ToolBase();
        Axe = new ToolBase();
        Pickaxe.mineBlocks.addAll(Arrays.asList(new BlockHarvestPower(Holystone.id, 20.0F), new BlockHarvestPower(Icestone.id, 20.0F), new BlockHarvestPower(AmbrosiumOre.id, 20.0F), new BlockHarvestPower(DungeonStone.id, 20.0F), new BlockHarvestPower(LightDungeonStone.id, 20.0F), new BlockHarvestPower(Pillar.id, 20.0F), new BlockHarvestPower(TreasureChest.id, 20.0F), new BlockHarvestPower(ZaniteOre.id, 40.0F), new BlockHarvestPower(GravititeOre.id, 60.0F), new BlockHarvestPower(EnchantedGravitite.id, 60.0F), new BlockHarvestPower(Aerogel.id, 60.0F)));
        Shovel.mineBlocks.addAll(Arrays.asList(new BlockHarvestPower(Dirt.id, 0.0F), new BlockHarvestPower(Grass.id, 0.0F), new BlockHarvestPower(Quicksoil.id, 0.0F), new BlockHarvestPower(Aercloud.id, 0.0F)));
        Axe.mineBlocks.addAll(Arrays.asList(new BlockHarvestPower(Log.id, 0.0F), new BlockHarvestPower(Plank.id, 0.0F), new BlockHarvestPower(SkyrootLeaves.id, 0.0F), new BlockHarvestPower(GoldenOakLeaves.id, 60.0F)));
        */
        /*
         */
    }

    public void RegisterBlocks(Block... blocks) {
        Block[] arr$ = blocks;
        int len$ = blocks.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            Block block = arr$[i$];
            //ModLoader.RegisterBlock(block);
        }

    }

    public int override(String path) {
        return 0;
    }
}