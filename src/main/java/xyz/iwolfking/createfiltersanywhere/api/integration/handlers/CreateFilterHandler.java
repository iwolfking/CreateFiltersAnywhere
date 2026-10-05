package xyz.iwolfking.createfiltersanywhere.api.integration.handlers;

import com.simibubi.create.content.logistics.filter.FilterItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import xyz.iwolfking.createfiltersanywhere.api.core.CFACache;

public class CreateFilterHandler {
    public static boolean isSupportedFilterItem(Item filterItem) {
        return filterItem instanceof FilterItem;
    }

    public static boolean checkFilter(ItemStack stack, ItemStack filterStack) {
        return CFACache.getOrCreateFilter(stack,filterStack,null);
    }
}
