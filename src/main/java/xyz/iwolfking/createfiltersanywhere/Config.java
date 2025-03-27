package xyz.iwolfking.createfiltersanywhere;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Config {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.ConfigValue<Integer> CACHE_TTK;
    public static final ModConfigSpec.ConfigValue<Boolean> MR_COMPAT;
    public static final ModConfigSpec.ConfigValue<Boolean> RS_COMPAT;
    public static final ModConfigSpec.ConfigValue<Boolean> BACKPACKS_COMPAT;
    public static final ModConfigSpec.ConfigValue<Boolean> AE2_COMPAT;
    public static final ModConfigSpec.ConfigValue<Boolean> TOMS_COMPAT;
    public static final ModConfigSpec.ConfigValue<Boolean> LASERIO_COMPAT;
    public static final ModConfigSpec.ConfigValue<Boolean> CACHE_DATAFIX;

    static {
        BUILDER.push("Create Filters Anywhere Server Config");

        CACHE_TTK = BUILDER.comment("\nHow long till an unused cache entry gets cleared" +
                "\nin minutes" +
                "\nMinimum 2" +
                "\nDefault: 5").defineInRange("Cache time to kill",5,2,100);

        MR_COMPAT = BUILDER.comment("\nEnable compatibility for list and attribute filters inside Modular Router modules" +
                "\nDefault:true").define("Modular Routers Compatibility",true);

        RS_COMPAT = BUILDER.comment("\nEnable compatibility for list and attribute filters inside Refined Storage exporters and grid filters" +
                "\nDefault:true").define("Refined Storage Compatibility", true);

        BACKPACKS_COMPAT = BUILDER.comment("\nEnable compatibility for list and attribute filters inside sophisticated backpacks upgrades" +
                "\nDefault:true").define("Backpacks Compatibility",true);

        AE2_COMPAT = BUILDER.comment("\nEnable compatibility for list and attribute filters inside AE2 exporter buses and terminal filters" +
                "\nDefault:true").define("AE2 Compatibility", true);

        TOMS_COMPAT = BUILDER.comment("\nEnable compatibility for list and attribute filters inside Tom's basic filter item" +
                "\nDefault:true").define("Tom's Simple Storage Compatibility", true);

        LASERIO_COMPAT = BUILDER.comment("\nEnable compatibility for list and attribute filters inside LaserIO's Basic Filter card." +
                "\nDefault:true").define("LaserIO Compatibility", true);

        CACHE_DATAFIX = BUILDER.comment("\nDelete old cache entries from items when they're filtered through" +
                "\nDefault:false").define("Old cache data fixer", false);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
