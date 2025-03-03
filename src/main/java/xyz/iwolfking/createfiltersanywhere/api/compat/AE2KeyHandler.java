package xyz.iwolfking.createfiltersanywhere.api.compat;

import appeng.api.stacks.AEItemKey;
import com.simibubi.create.content.logistics.filter.FilterItemStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import xyz.iwolfking.createfiltersanywhere.api.core.CFACache;
import xyz.iwolfking.createfiltersanywhere.api.core.CFATests;

public class AE2KeyHandler {
    public static boolean handleAEKey(ItemStack stack, Object filterStack, Level level) {
        if(filterStack instanceof AEItemKey aeItemKey) {
            return FilterItemStack.of(aeItemKey.toStack()).test(level,stack);
        }
        return false;
    }

    public static boolean checkFilter(AEItemKey stack, Object filterStack, boolean useCache, Level level) {
        if (!useCache) {
            return CFATests.basicFilterTest(stack.toStack(),filterStack,level);
        }
        return getOrCreateFilter(stack,filterStack,level);
    }

    public static boolean getOrCreateFilter(AEItemKey stack, Object filterStack, Level level) {
        int itemHash = stack.hashCode();
        CFACache cache = CFACache.ITEM_CACHES.get(itemHash);
        if (cache == null) {
            boolean result = CFATests.noCacheDetailedTest(stack.toStack(),filterStack,level);
            CFACache.ITEM_CACHES.put(itemHash, new CFACache(itemHash).addFilter(filterStack.hashCode(), result));
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
}
