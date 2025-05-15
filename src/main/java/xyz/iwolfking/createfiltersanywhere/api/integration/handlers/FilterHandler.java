package xyz.iwolfking.createfiltersanywhere.api.integration.handlers;

import net.minecraft.world.item.ItemStack;
import xyz.iwolfking.createfiltersanywhere.CreateFiltersAnywhere;
import xyz.iwolfking.createfiltersanywhere.api.core.CFACache;
import xyz.iwolfking.createfiltersanywhere.api.lib.FilterType;

public class FilterHandler {
    public static FilterType getFilterType(ItemStack filterStack) {
        if(CreateFiltersAnywhere.isCreateLoaded) {
            if(CreateFilterHandler.isSupportedFilterItem(filterStack)) {
                return FilterType.CREATE;
            }
        }
        else if(CreateFiltersAnywhere.isFTBFilterSystemLoaded) {
            if(FFSFilterHandler.isSupportedFilterItem(filterStack)) {
                return FilterType.FTB;
            }
        }

        return FilterType.INVALID;
    }

    public static boolean checkFilter(ItemStack stack, ItemStack filterStack) {
        return false;
    }
}
