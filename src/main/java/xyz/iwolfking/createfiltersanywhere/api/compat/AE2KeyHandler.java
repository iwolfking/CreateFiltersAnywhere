package xyz.iwolfking.createfiltersanywhere.api.compat;

import appeng.api.stacks.AEItemKey;
import com.simibubi.create.content.logistics.filter.FilterItemStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import xyz.iwolfking.createfiltersanywhere.api.core.CFACache;
import xyz.iwolfking.createfiltersanywhere.api.core.CFAFilterSelector;
import xyz.iwolfking.createfiltersanywhere.api.core.CFATests;

public class AE2KeyHandler {
    public static boolean handleAEKey(ItemStack stack, Object filterStack, Level level) {
        if(filterStack instanceof AEItemKey aeItemKey) {
            return FilterItemStack.of(aeItemKey.toStack()).test(level,stack);
        }
        return false;
    }

    public static boolean checkFilter(AEItemKey stack, Object filterStack, boolean useCache, Level level) {
        if(filterStack instanceof AEItemKey aeItemKey) {
            return CFAFilterSelector.doFilterTest(stack.toStack(), aeItemKey.toStack());
        }

        return false;
    }
}
