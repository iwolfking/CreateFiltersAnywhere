package xyz.iwolfking.createfiltersanywhere.api.integration.handlers;



import me.desht.modularrouters.api.matching.IItemMatcher;
import me.desht.modularrouters.item.smartfilter.SmartFilterItem;
import me.desht.modularrouters.logic.settings.ModuleFlags;
import net.minecraft.world.item.ItemStack;

public class MRFilterHandler {
    public static boolean isSupportedFilterItem(ItemStack filterStack) {
        return filterStack.getItem() instanceof SmartFilterItem;
    }

    public static boolean checkFilter(ItemStack stack, ItemStack filterStack) {
        if(filterStack.getItem() instanceof SmartFilterItem smartFilterItem) {
            IItemMatcher matcher = smartFilterItem.compile(filterStack, stack);
            return matcher.matchItem(stack, new ModuleFlags(false, true, false, false, false), null);
        }

        return false;
    }
}
