package io.github.EJR1135.portalbetweenworlds.gen.dim;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.world.dimension.Dimension;
import net.modificationstation.stationapi.api.event.registry.DimensionRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.DimensionContainer;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class otherWorldDimensions {
    @Entrypoint.Namespace
    public static Namespace MOD_ID = Null.get();
    public static Identifier THE_otherWorld;

    @EventListener
    public static void registerDimensions(DimensionRegistryEvent event) {
        DimensionRegistry r = event.registry;
        r.register(THE_otherWorld = Identifier.of(MOD_ID, "the_otherworld"), new DimensionContainer<Dimension>(otherWorldDimension::new));
    }
}