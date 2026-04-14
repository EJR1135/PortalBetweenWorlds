package io.github.EJR1135.portalbetweenworlds.gen.dim;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.world.dimension.PortalForcer;

public class bareotherWorldTravelAgent extends PortalForcer {
    @Override
    public boolean teleportToValidPortal(World level, Entity entity) {
        return true;
    }

    @Override
    public boolean createPortal(World level, Entity entity) {
        return true;
    }
}