package io.github.EJR1135.portalbetweenworlds;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.mod.InitEvent;

public class portalbetweenworlds implements ModInitializer {

    public static boolean OLDSTAPI = false;

    @Override
    public void onInitialize() {
        String stapi_version = String.valueOf(FabricLoader.getInstance().getModContainer("station-api-base").get().getMetadata().getVersion());
        if (stapi_version.equals("2.0-alpha.2-1.0.0") || stapi_version.equals("2.0-alpha.1.1-1.0.0") || stapi_version.equals("2.0-alpha.1-1.0.0")) {
            OLDSTAPI = true;
        }
    }
}