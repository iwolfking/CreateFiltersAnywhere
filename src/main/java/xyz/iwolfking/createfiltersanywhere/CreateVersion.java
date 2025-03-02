package xyz.iwolfking.createfiltersanywhere;

import net.neoforged.fml.loading.LoadingModList;
import net.neoforged.fml.loading.moddiscovery.ModFileInfo;

public enum CreateVersion {
    CREATE_06;

    private static CreateVersion loadedVersion;

    private static CreateVersion getCreateVersion() {
        String createVersion = "";
        ModFileInfo createInfo = LoadingModList.get().getModFileById("create");
        if (createInfo != null) {
            createVersion = createInfo.versionString();
        }

        if (createVersion.startsWith("6.0")
        ) { // it has suffix in dev
            loadedVersion = CREATE_06;
        }

        System.out.println("Create-Filters-Anywhere - Detected create: " + loadedVersion + " - " + createVersion); // logger not available so early
        return loadedVersion;
    }


    public static CreateVersion getLoadedVersion() {
        return loadedVersion == null
                ? getCreateVersion()
                : loadedVersion;
    }
}