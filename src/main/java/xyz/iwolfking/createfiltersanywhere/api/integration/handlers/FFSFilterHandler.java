package xyz.iwolfking.createfiltersanywhere.api.integration.handlers;

import dev.ftb.mods.ftbfiltersystem.api.filter.SmartFilter;
import dev.ftb.mods.ftbfiltersystem.registry.item.SmartFilterItem;
import net.minecraft.world.item.ItemStack;

public class FFSFilterHandler {
    public static boolean isSupportedFilterItem(ItemStack filterStack) {
        return filterStack.getItem() instanceof SmartFilterItem;
    }

    public static boolean checkFilter(ItemStack stack, ItemStack filterStack) {
        SmartFilter filter = SmartFilterItem.getFilter(filterStack);
        return filter.test(stack);
    }
}
