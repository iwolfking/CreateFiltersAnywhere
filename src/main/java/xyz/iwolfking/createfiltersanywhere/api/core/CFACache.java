package xyz.iwolfking.createfiltersanywhere.api.core;

import appeng.api.stacks.AEItemKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import xyz.iwolfking.createfiltersanywhere.Config;

import java.util.concurrent.ConcurrentHashMap;

import static xyz.iwolfking.createfiltersanywhere.api.core.CFAAsync.asyncIterateCache;

public class CFACache {
    private static final ConcurrentHashMap<Integer, CFACache> ITEM_CACHES = new ConcurrentHashMap<>();
    private static int ticks = 0;

    private final int itemHash;
    private final ConcurrentHashMap<Integer, Boolean> filterMap;
    private int ttk; // ttk = time to kill

    public CFACache(int itemHash) {
        this.itemHash = itemHash;
        this.filterMap = new ConcurrentHashMap<>();
        this.ttk = Config.CACHE_TTK.get();
    }

    public CFACache addFilter(int filterHash, boolean result) {
        filterMap.put(filterHash, result);
        resetTTK();
        return this;
    }

    public Boolean result(int filterHash) {
        Boolean result = filterMap.get(filterHash);
        if (result != null) {
            resetTTK();
        }
        return result;
    }

    public void resetTTK() {
        this.ttk = Config.CACHE_TTK.get();
    }

    public void tick() {
        if (this.ttk == 0) {
            ITEM_CACHES.remove(this.itemHash);
            return;
        }
        this.ttk--;
    }

    public static boolean getOrCreateFilter(ItemStack stack, Object filterStack, Level level) {
        int itemHash = stack.hashCode();
        CFACache cache = ITEM_CACHES.get(itemHash);
        if (cache == null) {
            boolean result = CFATests.noCacheDetailedTest(stack,filterStack,level);
            ITEM_CACHES.put(itemHash, new CFACache(itemHash).addFilter(filterStack.hashCode(), result));
            return result;
        }
        int filterHash = filterStack.hashCode();
        Boolean cachedResult = cache.result(filterHash);
        if (cachedResult != null) {
            return cachedResult;
        }
        boolean result = CFATests.basicFilterTest(stack, filterStack, level);
        cache.addFilter(filterHash, result);
        return result;
    }
    public static boolean getOrCreateFilter(AEItemKey stack, Object filterStack, Level level) {
        int itemHash = stack.hashCode();
        CFACache cache = ITEM_CACHES.get(itemHash);
        if (cache == null) {
            boolean result = CFATests.noCacheDetailedTest(stack.toStack(),filterStack,level);
            ITEM_CACHES.put(itemHash, new CFACache(itemHash).addFilter(filterStack.hashCode(), result));
            return result;
        }
        int filterHash = filterStack.hashCode();
        Boolean cachedResult = cache.result(filterHash);
        if (cachedResult != null) {
            return cachedResult;
        }
        boolean result = CFATests.basicFilterTest(stack.toStack(), filterStack, level);
        cache.addFilter(filterHash, result);
        return result;
    }
    public static void iterateCache() {
        ITEM_CACHES.values().forEach(CFACache::tick);
    }
    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        if (++ticks >= 60 * 20 ) { // 60 seconds * 20 tps
            asyncIterateCache();
            ticks = 0;
        }
    }
}
