package xyz.iwolfking.createfiltersanywhere;

import com.mojang.logging.LogUtils;
import com.simibubi.create.api.registry.CreateBuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.LoadingModList;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.*;
import org.slf4j.Logger;
import xyz.iwolfking.createfiltersanywhere.api.CreateAttributeRegistry;
import xyz.iwolfking.createfiltersanywhere.api.core.CFAFilterSelector;
import xyz.iwolfking.createfiltersanywhere.api.lib.FilterType;
import xyz.iwolfking.createfiltersanywhere.api.util.apotheosis.ApotheosisUtil;
import xyz.iwolfking.createfiltersanywhere.attributes.impl.apotheosis.ApotheosisAttributes;
import xyz.iwolfking.createfiltersanywhere.attributes.impl.sophisticatedbackpacks.SophisticatedBackpackAttributes;
import xyz.iwolfking.createfiltersanywhere.api.core.CFATests;
import xyz.iwolfking.createfiltersanywhere.data.CFAComponents;

import java.util.Arrays;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(CreateFiltersAnywhere.MODID)
public class CreateFiltersAnywhere {

    public static final String MODID = "createfiltersanywhere";

    public static final Logger LOGGER = LogUtils.getLogger();


    //Checking if certain mods are loaded:
    public static boolean isCreateLoaded = false;
    public static boolean isFTBFilterSystemLoaded = false;
    public static boolean isModularRoutersLoaded = false;
    public static boolean isTomsStorageLoaded = false;

    public CreateFiltersAnywhere(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerAttributes);
        NeoForge.EVENT_BUS.register(this);

        NeoForge.EVENT_BUS.register(CFATests.class);

        CFAComponents.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.SERVER, Config.SPEC);
    }


    public void registerAttributes(RegisterEvent event) {
        event.register(CreateBuiltInRegistries.ITEM_ATTRIBUTE_TYPE.key(), registry -> {
            if(LoadingModList.get().getModFileById("sophisticatedbackpacks") != null) {
                CreateAttributeRegistry.register("has_backpack_uuid", SophisticatedBackpackAttributes.HAS_BACKPACK_UUID);
                CreateAttributeRegistry.register("has_backpack_upgrade", SophisticatedBackpackAttributes.HAS_BACKPACK_UPGRADE);
            }

            if(LoadingModList.get().getModFileById("apotheosis") != null) {
                CreateAttributeRegistry.register("apoth_gem_purity", ApotheosisAttributes.APOTH_GEM_PURITY);
                CreateAttributeRegistry.singleton("apoth_gem_unique", ApotheosisUtil::isUniqueGem);
                CreateAttributeRegistry.register("apoth_gem_bonus_type", ApotheosisAttributes.APOTH_GEM_BONUS_TYPE);
                CreateAttributeRegistry.register("apoth_gem_id", ApotheosisAttributes.APOTH_GEM_ID);
                CreateAttributeRegistry.register("apoth_loot_rarity", ApotheosisAttributes.APOTH_LOOT_RARITY);
                CreateAttributeRegistry.register("apoth_has_affix", ApotheosisAttributes.APOTH_HAS_AFFIX);
                CreateAttributeRegistry.register("apoth_socket_count", ApotheosisAttributes.APOTH_SOCKET_COUNT);
                CreateAttributeRegistry.register("apoth_socket_count_empty", ApotheosisAttributes.APOTH_SOCKET_COUNT_EMPTY);
            }
        });
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        isCreateLoaded = LoadingModList.get().getModFileById("create") != null;
        isFTBFilterSystemLoaded = LoadingModList.get().getModFileById("ftbfiltersystem") != null;
        isModularRoutersLoaded = LoadingModList.get().getModFileById("modularrouters") != null;
        isTomsStorageLoaded = LoadingModList.get().getModFileById("toms_storage") != null;
        CFAFilterSelector.LOADED_FILTER_TYPES.addAll(Arrays.asList(FilterType.values()));
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }


    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
