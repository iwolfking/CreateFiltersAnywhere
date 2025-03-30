package xyz.iwolfking.createfiltersanywhere;

import com.mojang.logging.LogUtils;
import com.simibubi.create.api.registry.CreateBuiltInRegistries;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttributeType;
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
import xyz.iwolfking.createfiltersanywhere.api.util.apotheosis.ApotheosisUtil;
import xyz.iwolfking.createfiltersanywhere.attributes.impl.apotheosis.ApotheosisAttributes;
import xyz.iwolfking.createfiltersanywhere.attributes.impl.sophisticatedbackpacks.SophisticatedBackpackAttributes;
import xyz.iwolfking.createfiltersanywhere.api.core.CFACache;
import xyz.iwolfking.createfiltersanywhere.api.core.CFATests;
import xyz.iwolfking.createfiltersanywhere.data.CFAComponents;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(CreateFiltersAnywhere.MODID)
public class CreateFiltersAnywhere {

    public static final String MODID = "createfiltersanywhere";

    public static final Logger LOGGER = LogUtils.getLogger();

    public CreateFiltersAnywhere(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerAttributes);
        NeoForge.EVENT_BUS.register(this);

        NeoForge.EVENT_BUS.register(CFACache.class);
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
            }
        });
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
