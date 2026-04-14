package io.github.EJR1135.portalbetweenworlds.gen.dim.portal;

import io.github.EJR1135.portalbetweenworlds.block.otherWorldPortal;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.event.world.BlockSetEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import io.github.EJR1135.portalbetweenworlds.block.otherWorldBlocks;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class portalListener {
    @EventListener
    public static void blockSet(BlockSetEvent event) {
        if (
                (event.blockState.getBlock().id == Block.WATER.id || event.blockState.getBlock().id == Block.FLOWING_WATER.id) &&
                        event.world.getBlockId(event.x, event.y - 1, event.z) == Block.GLOWSTONE.id && ((otherWorldPortal) otherWorldBlocks.Portal).create(event.world, event.x, event.y, event.z)
        ) {
            event.cancel();
            event.world.setBlock(event.x, event.y, event.z, otherWorldBlocks.Portal.id);
        }
    }
}
