package io.github.EJR1135.portalbetweenworlds.client.texture;

import io.github.EJR1135.portalbetweenworlds.block.*;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;

import static net.modificationstation.stationapi.api.util.Identifier.of;

public class otherWorldTextures {
    @Entrypoint.Namespace
    public static Namespace MOD_ID = Null.get();

    @EventListener
    public static void registerTextures(TextureRegisterEvent event) {
        setBlockTextures();
        setItemTextures();
    }

    public static void setBlockTextures() {
        ExpandableAtlas terrain = Atlases.getTerrain();

        otherWorldPortal.spr = terrain.addTexture(of(MOD_ID, "block/Portal")).index;
        otherWorldBlocks.portalFrame.textureId = terrain.addTexture(of(MOD_ID, "block/PortalFrame")).index;
    }

    public static void setItemTextures(){}
}