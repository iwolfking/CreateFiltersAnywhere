package xyz.iwolfking.createfiltersanywhere.api.integration.handlers;

import net.minecraft.world.item.ItemStack;

public class FilterHandler {
    public static boolean checkFilter(ItemStack stack, ItemStack filterStack) {
        return false;
    }

    public static Boolean isSupportedFilterItem(ItemStack itemStack) {
        return false;
    }
}
