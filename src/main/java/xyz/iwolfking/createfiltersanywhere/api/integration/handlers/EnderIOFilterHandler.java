package xyz.iwolfking.createfiltersanywhere.api.integration.handlers;

import com.enderio.enderio.api.EnderIOCapabilities;
import com.enderio.enderio.content.filters.AbstractFilterItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import xyz.iwolfking.createfiltersanywhere.CreateFiltersAnywhere;

public class EnderIOFilterHandler {
    public static boolean isSupportedFilterItem(Item filterItem) {
        return filterItem instanceof AbstractFilterItem<?>;
    }

    public static boolean checkFilter(ItemStack stack, ItemStack filterStack) {
        if(filterStack.getItem() instanceof AbstractFilterItem<?>) {
            if(filterStack.getCapability(EnderIOCapabilities.ITEM_FILTER) != null) {
                try {
                    return !filterStack.getCapability(EnderIOCapabilities.ITEM_FILTER).test(null, stack).isEmpty();
                }
                catch (Exception e) {
                    CreateFiltersAnywhere.LOGGER.error(e.toString());
                    return false;
                }
            }
        }

        return false;
    }
}
