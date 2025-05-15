package xyz.iwolfking.createfiltersanywhere.api.integration.handlers;



import me.desht.modularrouters.api.matching.IItemMatcher;
import me.desht.modularrouters.item.smartfilter.SmartFilterItem;
import me.desht.modularrouters.logic.settings.ModuleFlags;
import net.minecraft.world.item.ItemStack;

public class MRFilterHandler {

    public static final ModuleFlags DEFAULT_FLAGS =  new ModuleFlags(true, true, false, false, false);

    public static boolean isSupportedFilterItem(ItemStack filterStack) {
        return filterStack.getItem() instanceof SmartFilterItem;
    }

    public static boolean checkFilter(ItemStack stack, ItemStack filterStack) {
        if(filterStack.getItem() instanceof SmartFilterItem smartFilterItem) {
            IItemMatcher matcher = smartFilterItem.compile(stack, filterStack);
            return matcher.matchItem(stack, DEFAULT_FLAGS);
        }

        return false;
    }
}
