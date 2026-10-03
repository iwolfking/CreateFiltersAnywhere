package xyz.iwolfking.createfiltersanywhere.api.integration.ftbquests;

import dev.ftb.mods.ftbquests.api.FTBQuestsAPI;
import net.neoforged.fml.loading.LoadingModList;

public class FTBQuestsIntegration {

    public static void register() {
        if (LoadingModList.get().getModFileById("ftbquests") != null) {
            FTBQuestsAPI.api().registerFilterAdapter(new CreateItemFilterAdapter());
        }
    }
}