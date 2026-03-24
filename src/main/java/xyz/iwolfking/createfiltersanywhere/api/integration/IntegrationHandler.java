package xyz.iwolfking.createfiltersanywhere.api.integration;

import net.neoforged.fml.loading.LoadingModList;

import java.util.HashMap;
import java.util.Map;

public class IntegrationHandler {
    public static Map<String, Boolean> MOD_LOADED_MAP = new HashMap<>();

    public static boolean isModLoaded(String modId) {
        if(MOD_LOADED_MAP.containsKey(modId)) {
            return MOD_LOADED_MAP.get(modId);
        }

        boolean isLoaded = LoadingModList.get().getModFileById(modId) != null;

        MOD_LOADED_MAP.put(modId, isLoaded);

        return isLoaded;
    }
}
