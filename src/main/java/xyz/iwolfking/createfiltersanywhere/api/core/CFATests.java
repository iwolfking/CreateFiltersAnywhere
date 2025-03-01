package xyz.iwolfking.createfiltersanywhere.api.core;

import appeng.api.stacks.AEItemKey;
import com.simibubi.create.content.logistics.filter.FilterItemStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.telemetry.events.WorldLoadEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.event.level.LevelEvent;
import xyz.iwolfking.createfiltersanywhere.CreateFiltersAnywhere;

import java.lang.invoke.MethodHandle;

public class CFATests {
    public static String filterKey = "hashes";
    private static MethodHandle testMethodHandle;
    private static Level level;

    public static boolean checkFilter(ItemStack stack, Object filterStack, boolean useCache, Level level) {
        if (!useCache) {
            return basicFilterTest(stack,filterStack,level);
        }

        Item stackItem = stack.getItem();

        return CFACache.getOrCreateFilter(stack,filterStack,level);
    }
    public static boolean checkFilter(AEItemKey stack, Object filterStack, boolean useCache, Level level) {
        if (!useCache) {
            return basicFilterTest(stack.toStack(),filterStack,level);
        }
        return CFACache.getOrCreateFilter(stack,filterStack,level);
    }

    public static boolean basicFilterTest(ItemStack stack, Object filterStack, Level level) {
        if (level == null) {
            if (FMLLoader.getDist() == Dist.CLIENT) {
                level = CFATests.getClientLevel();
            }
            else {
                level = CFATests.level;
            }
        }

        if (true) {
            if (filterStack instanceof ItemStack stackFilter) {
                return FilterItemStack.of(stackFilter).test(level, stack);
            } else if (filterStack instanceof FilterItemStack filterItemStack) {
                return filterItemStack.test(level, stack);
            } else if (filterStack instanceof AEItemKey aeItemKey) {
                return FilterItemStack.of(aeItemKey.toStack()).test(level,stack);
            }
            CreateFiltersAnywhere.LOGGER.debug("[0.5.1.f] invalid filter entered");
            return false;
        }

//        if (CreateVersion.getLoadedVersion() == CreateVersion.LEGACY) {
//            return basicFilterTestLegacy(filterStack, stack, level);
//        }
        return false;
    }

    public static boolean noCacheDetailedTest(ItemStack stack, Object filterStack, Level level) {
        return CFATests.basicFilterTest(stack, filterStack, level);
    }


    @SubscribeEvent
    public static void onWorldLoad(LevelEvent event) {
        MinecraftServer server = event.getLevel().getServer();
        if (server != null) {
            level = server.getLevel(Level.OVERWORLD);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static Level getClientLevel() {
        return Minecraft.getInstance().level;
    }
}
